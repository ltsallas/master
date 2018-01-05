import {Component, EventEmitter, Input, Output, OnInit} from "@angular/core";
import {UserDTOBase} from "../../shared/swagger/model/UserDTO";
import {MayorDTOBase} from "../../shared/swagger/model/MayorDTO";
import {MunicipalityDTOBase} from "../../shared/swagger/model/MunicipalityDTO";
import {SharedService} from "../../shared/shared.service";


@Component({
    selector: 'beneficiary-registration-step3',
    templateUrl: 'beneficiary-registration-step3.component.html'
})

export class BeneficiaryRegistrationStep3Component {

    @Input('initialUser') private initialUser: UserDTOBase;
    @Input('multipleMunicipalities') private multipleMunicipalities: boolean;
    @Input('mayors') private mayors: MayorDTOBase[];
    @Input('municipalities') private municipalities: MunicipalityDTOBase[];
    @Input('sameDetails') private sameDetails: boolean;

    private imMayor: boolean;
    private repeatEmail: string;
    private userEmailMatches: boolean;

    @Output() private onNext: EventEmitter<any>;
    @Output() private onBack: EventEmitter<any>;

    private css_class_email: string = '';
    private emailPattern = '^[a-zA-Z0-9](\\.?[a-zA-Z0-9_-]){0,}@[a-zA-Z0-9-]+\\.([a-zA-Z]{1,6}\\.)?[a-zA-Z]{2,6}$';

    constructor(private sharedService: SharedService) {
        this.onNext = new EventEmitter<any>();
        this.onBack = new EventEmitter<any>();
        this.imMayor = false;
        this.userEmailMatches = false;
        this.sameDetails = false;
        this.sharedService.cleanEmitter.subscribe(() => {this.reset()});
    }

    private fillMayorData() {
        if (!this.imMayor) {
            this.imMayor = true;
            this.initialUser.name = this.mayors[0].name;
            this.initialUser.surname = this.mayors[0].surname;
            this.initialUser.address = this.municipalities[0].address;
            this.initialUser.addressNum = this.municipalities[0].addressNum;
            this.initialUser.postalCode = this.municipalities[0].postalCode;
            this.initialUser.email = this.mayors[0].email;
            this.userEmailMatches = true;
        } else {
            this.imMayor = false;
            this.initialUser.name = '';
            this.initialUser.surname = '';
            this.initialUser.address = '';
            this.initialUser.addressNum = '';
            this.initialUser.postalCode = '';
            this.initialUser.email = '';
            this.repeatEmail = '';
        }
    }

    private checkEmailsMatch() {
        this.userEmailMatches = false;
        if (this.initialUser.email === this.repeatEmail && this.repeatEmail.length > 0) {
            this.userEmailMatches = true;
            this.css_class_email = 'isValid';
        } else {
            this.css_class_email = 'notValid';
        }
    }

    private back() {
        this.onBack.emit();
        this.repeatEmail = '';
        this.reset();
    }

    private reset() {
        if (this.imMayor) {
            this.sameDetails = this.imMayor = false;
            this.initialUser.name = '';
            this.initialUser.surname = '';
            this.initialUser.address = '';
            this.initialUser.addressNum = '';
            this.initialUser.postalCode = '';
            this.initialUser.email = '';
            this.repeatEmail = '';
        }
    }

    private submit() {
        this.onNext.emit();
        this.repeatEmail = '';
    }

    private preventPaste(event: any) {
        return false;
    }
}