import { animate, style, transition, trigger } from "@angular/animations";
import { Location } from "@angular/common";
import { Component, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { SelectItem } from "primeng/primeng";
import { Observable } from "rxjs/Observable";
import { Subscription } from "rxjs/Subscription";
import { SharedService } from "../../../shared/shared.service";
import { NutsApi } from "../../../shared/swagger/api/NutsApi";
import { SupplierApi } from "../../../shared/swagger/api/SupplierApi";
import { NutsDTOBase } from "../../../shared/swagger/model/NutsDTO";
import { SuppliedRegionDTOBase } from "../../../shared/swagger/model/SuppliedRegionDTO";
import { SupplierDTOBase } from "../../../shared/swagger/model/SupplierDTO";
import { UserDTOBase } from "../../../shared/swagger/model/UserDTO";
import { ResponseDTO, ResponseDTOBase } from "../../../shared/swagger";
import { LocalStorageService } from "angular-2-local-storage";

@Component({
    selector: 'supplier-edit-profile',
    templateUrl: 'edit-profile.component.html',
    styleUrls: ['edit-profile.component.scss'],
    providers: [SupplierApi, NutsApi],
    animations: [
        trigger(
            'enterSpinner', [
                transition(':enter', [
                    style({opacity: 0}),
                    animate('200ms', style({opacity: 1}))
                ]),
                transition(':leave', [
                    style({opacity: 1}),
                    animate('200ms', style({opacity: 0}))
                ])
            ]
        )
    ]
})

export class SupplierEditProfileComponent {
    private user: UserDTOBase;
    private supplier: SupplierDTOBase;
    private websitePattern: string = '(([wW][wW][wW]\\.)|([hH][tT][tT][pP][sS]?:\\/\\/([wW][wW][wW]\\.)?))?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)';
    private countryOptions: SelectItem[] = [];
    private regionOptions: SelectItem[][] = [];
    private selectedCountries: NutsDTOBase[] = [];
    private selectedRegions: NutsDTOBase[][] = [];
    private logoUrl: FileReader = new FileReader();
    private logoFile: File;
    @ViewChild('logoInput') private logoInput: any;
//    private allDataFetched: boolean = false;
    private geographicalScopeLoaded: boolean = false;
    private savingData: boolean = false;
    private isLogoUploaded: boolean = false;
    private savingDataSubscription: Subscription = new Subscription();
    private addContact: boolean = false;
    private addUser: boolean = false;
    private emailPattern = new RegExp("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])");
    private newUserEmail: string = '';
 

    constructor(private localStorageService: LocalStorageService, private sharedService: SharedService, private supplierApi: SupplierApi, private nutsApi: NutsApi, private location: Location, private router: Router, private activatedRoute: ActivatedRoute) {
        let allow = true;
        if (this.sharedService.user) {
            this.user = this.sharedService.user;
            this.getSupplierData();
        } else {
            this.sharedService.loginEmitter.map(() => {
                this.user = this.sharedService.user;
                this.getSupplierData();
            });
        }
    }

    private getSupplierData() {
        this.supplierApi.getSupplierByUserId(this.user.id).subscribe(
            (supplier: SupplierDTOBase) => {
                if (supplier != null) {
                    this.supplier = supplier;
                    if (this.supplier.logo != null)
                        this.isLogoUploaded = true;
                    this.nutsApi.getNutsByLevel(0).subscribe(
                        (countries: NutsDTOBase[]) => {
                            for (let i = 0; i < countries.length; i++) {
                                let country = countries[i];
                                let countryName = country.label.substring(0, 1).toUpperCase() + country.label.substring(1, country.label.length).toLowerCase();
                                let countryOption = {
                                    label: countryName,
                                    value: country
                                };
                                this.countryOptions.push(countryOption);
                                this.regionOptions[country.label] = [];
                                this.nutsApi.getNutsByCountryCodeAndLevelOrderByLabelAsc(country.countryCode, 3).subscribe(
                                    (regions: NutsDTOBase[]) => {
                                        for (let j = 0; j < regions.length; j++) {
                                            let region = regions[j];
                                            let regionName = region.label;
                                            let regionOption = {
                                                label: regionName,
                                                value: region
                                            }
                                            this.regionOptions[country.label].push(regionOption);
                                            let countrySuppliedRegions = this.supplier.suppliedRegions.filter(suppliedRegion => suppliedRegion.regionId.id == region.id);
                                            if (countrySuppliedRegions.length > 0) {
                                                if (!this.selectedRegions[country.label]) {
                                                    this.selectedRegions[country.label] = [];
                                                    this.selectedCountries.push(country);
                                                }
                                                this.selectedRegions[country.label].push(region);
                                            }
                                            if (i == (countries.length - 1) && j == (regions.length - 1)) {
                                                this.geographicalScopeLoaded = true;
                                            }
                                        }
                                    }
                                );
                            }
                        }
                    );
                }
            }
        );
    }

    private changeLogo(event): any {
        if (event.target.files.length > 0) {
            this.logoFile = event.target.files[0];
            if (this.logoFile.size > 2560000) {
                this.sharedService.growlTranslation('The file you uploaded is too big. Max file size allowed is 2.5 MB.', 'benefPortal.file.toobig.maxsize', 'warn', {size: '2.5 MB'});
                this.clearLogoFile();
                return;
            }
            if (this.logoFile.type != 'image/png' && this.logoFile.type != 'image/jpg' && this.logoFile.type != 'image/jpeg') {
                this.uploadWrong();
                return;
            }
            let imageStatus = '';
            let image = new Image();
            image.onload = function () {
                imageStatus = 'correct';
            };
            image.onerror = function () {
                imageStatus = 'wrong';
            };
            image.src = URL.createObjectURL(this.logoFile);
            let subscription = Observable.interval(200).subscribe(
                x => {
                    switch (imageStatus) {
                        case 'correct':
                            this.uploadCorrect();
                            subscription.unsubscribe();
                            break;
                        case 'wrong':
                            this.uploadWrong();
                            subscription.unsubscribe();
                            break;
                    }
                }
            );
        }
        return null;
    }

    private uploadCorrect(): any {
        this.logoUrl.readAsDataURL(this.logoFile);
        let subscription = Observable.interval(200).subscribe(
            x => {
                if (this.logoUrl.result != '') {
                    this.supplier.logo = this.logoUrl.result;
                    this.isLogoUploaded = true;
                    subscription.unsubscribe();
                    this.sharedService.growlTranslation('Your file was uploaded successfully!', 'suppPortal.fileUploadSuccess', 'success');
                }
            }
        );
    }

    private uploadWrong(): any {
        this.clearLogoFile();
        this.sharedService.growlTranslation('The file you uploaded is not a valid image file.', 'shared.growl.fileNotValid', 'error');
    }

    private clearLogoFile() {
        this.supplier.logo = null;
        this.logoInput.nativeElement.value = '';
        this.logoUrl = new FileReader();
        this.logoFile = null;
        this.isLogoUploaded = false;
    }

    private goBack() {
        this.savingDataSubscription.unsubscribe();
        this.router.navigate(['..'], {relativeTo: this.activatedRoute});
    }

    private saveSupplierData() {
        let storedUser = this.localStorageService.get('user');
        this.user = storedUser ? JSON.parse(storedUser.toString()) : null;

        this.savingData = true;
        var newRegions = [];

        this.selectedCountries.forEach(selectedCountry => {
            if(typeof this.selectedRegions[selectedCountry.label] !== "undefined") {
                newRegions[selectedCountry.label] = this.selectedRegions[selectedCountry.label];
            }
        });

        this.supplier.suppliedRegions = [];
        for (let selectedCountry in newRegions) {
            for (let selectedRegion of this.selectedRegions[selectedCountry]) {
                let suppliedRegion = new SuppliedRegionDTOBase();
                suppliedRegion.regionId = selectedRegion;
                suppliedRegion.supplierId = this.supplier.id;
                this.supplier.suppliedRegions.push(suppliedRegion);
            }
        }
        this.savingDataSubscription = this.supplierApi.updateSupplier(this.supplier).subscribe(
            (supplier: SupplierDTOBase) => {
                if (supplier != null) {
                    this.savingData = false;
                    this.sharedService.growlTranslation('Your profile data was updated successfully.', 'suppPortal.editProfile.save.success', 'success');
                    this.goBack();
                } else {
                    this.savingData = false;
                    this.sharedService.growlTranslation('An error ocurred while trying to update your profile data. Please, try again later.', 'suppPortal.editProfile.save.error', 'error');
                }
            }, error => {
                this.savingData = false;
                this.sharedService.growlTranslation('An error ocurred while trying to update your profile data. Please, try again later.', 'suppPortal.editProfile.save.error', 'error');
            }
        );

        this.supplierApi.updateContactDetails(this.supplier, this.user).subscribe(
            (user: UserDTOBase) =>{
                

            }, error =>{

            }

        )

    }

    private closeModal(){
        this.addContact = false;
        this.addUser = false;        
        }
    
        private addNewContact(){       
        this.addContact = true; 
           this.supplierApi.sendEmailToNewContact(this.newUserEmail).subscribe(
                (responseDTO: ResponseDTOBase) => {
                    this.sharedService.growlTranslation('Email sent successfully', 'shared.email.sent', 'success');
                    this.closeModal();
                }, error => {
                    this.sharedService.growlTranslation('An error occurred. Please, try again later.', 'shared.email.error', 'error');
                    this.closeModal();
                }
            );
        }
    
    
        
        /* New contact funciontality */
        private sendMailToUser(){
        this.newUserEmail = '';
        this.addUser = true;
        }
        
}