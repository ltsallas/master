import { Component } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizer } from "@angular/platform-browser";
import { SharedService } from "../../../shared/shared.service";
import { SupplierApi } from "../../../shared/swagger/api/SupplierApi";
import { SupplierDTOBase } from "../../../shared/swagger/model/SupplierDTO";
import { ResponseDTOBase } from "../../../shared/swagger/model/ResponseDTO";
import { animate, style, transition, trigger } from "@angular/animations";

@Component({
    templateUrl: 'supplier-registrations-details.component.html',
    providers: [SupplierApi],
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

export class DgConnSupplierRegistrationsDetailsComponent {
    private supplier: SupplierDTOBase = null;
    private similarSuppliers: SupplierDTOBase[] = [];
    private displayInvalidate: boolean = false;
    private selectedMainSupplier: boolean = null;
    private selectedIndex: number = null;
    private processingRequest: boolean = false;
    private loadingData: boolean = false;

    constructor(private route: ActivatedRoute, private sanitizer: DomSanitizer, private sharedService: SharedService, private supplierApi: SupplierApi) {
        this.loadingData = true;
        this.route.params.subscribe(
            params => {
                let supplierId = params['id'];
                this.supplierApi.getSupplierById(supplierId).subscribe(
                    (supplier: SupplierDTOBase) => {
                        if (supplier != null) {
                            this.supplierApi.findSimilarSuppliers(supplierId).subscribe(
                                (suppliers: SupplierDTOBase[]) => {
                                    if (suppliers.length != 0) {
                                        this.similarSuppliers = suppliers;
                                    }
                                    this.supplier = supplier;
                                    this.loadingData = false;
                                }
                            );
                        } else {
                            this.loadingData = false;
                        }
                    }
                );
            }
        );
    }

    private getLegalFileUrl(mainSupplier: boolean, fileNumber: number, index?: number) {
        if (mainSupplier) {
            switch (fileNumber) {
                case 1:
                    return this.sanitizer.bypassSecurityTrustUrl(this.supplier.legalFile1);
                case 2:
                    return this.sanitizer.bypassSecurityTrustUrl(this.supplier.legalFile2);
            }
        } else {
            switch (fileNumber) {
                case 1:
                    return this.sanitizer.bypassSecurityTrustUrl(this.similarSuppliers[index].legalFile1);
                case 2:
                    return this.sanitizer.bypassSecurityTrustUrl(this.similarSuppliers[index].legalFile2);
            }
        }
    }

    private requestLegalDocuments(mainSupplier: boolean, index?: number) {
        if (mainSupplier) {
            this.supplierApi.requestLegalDocuments(this.supplier.id).subscribe(
                (response: ResponseDTOBase) => {
                    if (response.success) {
                        this.sharedService.growlTranslation('An email has been sent to the contact person to supply the legal documents for the registration.', 'dgConn.supplierDetails.requestLegalDocuments.success', 'success');
                    } else {
                        this.sharedService.growlTranslation('An error occurred while trying to request the legal documents of the supplier. Please, try again later.', 'dgConn.supplierDetails.requestLegalDocuments.error', 'error');
                    }
                }
            );
        } else {
            if (index != null) {
                this.supplierApi.requestLegalDocuments(this.similarSuppliers[index].id).subscribe(
                    (response: ResponseDTOBase) => {
                        if (response.success) {
                            this.sharedService.growlTranslation('An email has been sent to the contact person to supply the legal documents for the registration.', 'dgConn.supplierDetails.requestLegalDocuments.success', 'success');
                        } else {
                            this.sharedService.growlTranslation('An error occurred while trying to request the legal documents of the supplier. Please, try again later.', 'dgConn.supplierDetails.requestLegalDocuments.error', 'error');
                        }
                    }
                );
            }
        }
    }

    private displayInvalidateModal(mainSupplier: boolean, index?: number) {
        this.selectedMainSupplier = mainSupplier;
        if (this.selectedMainSupplier) {
            if (this.supplier.status != 1) {
                this.displayInvalidate = true;
            }
        } else {
            if (index != null) {
                if (this.similarSuppliers[index].status != 1) {
                    this.selectedIndex = index;
                    this.displayInvalidate = true;
                }
            }
        }
    }

    private closeModal() {
        this.selectedMainSupplier = null;
        this.selectedIndex = null;
        this.displayInvalidate = false;
        this.processingRequest = false;
    }

    private invalidateSupplier() {
        if (!this.processingRequest) {
            this.processingRequest = true;
            if (this.selectedMainSupplier) {
                let modifiedSupplier = this.supplier;
                modifiedSupplier.status = 0;
                this.supplierApi.invalidateSupplier(modifiedSupplier).subscribe(
                    (response: ResponseDTOBase) => {
                        if (response.success) {
                            if (response.data != null) {
                                this.supplier = response.data;
                                this.sharedService.growlTranslation('You successfully invalidated the supplier.', 'dgConn.supplierDetails.invalidateSupplier.success', 'success');
                            }
                        } else {
                            this.sharedService.growlTranslation('An error occurred while trying to invalidate the supplier. Please, try again later.', 'dgConn.supplierDetails.invalidateSupplier.error', 'error');
                        }
                        this.closeModal();
                    }, error => {
                        this.sharedService.growlTranslation('An error occurred while trying to invalidate the supplier. Please, try again later.', 'dgConn.supplierDetails.invalidateSupplier.error', 'error');
                        this.closeModal();
                    }
                );
            } else {
                if (this.selectedIndex != null) {
                    let modifiedSupplier = this.similarSuppliers[this.selectedIndex];
                    modifiedSupplier.status = 0;
                    this.supplierApi.invalidateSupplier(modifiedSupplier).subscribe(
                        (response: ResponseDTOBase) => {
                            if (response.success) {
                                if (response.data != null) {
                                    this.similarSuppliers[this.selectedIndex] = response.data;
                                    this.sharedService.growlTranslation('You successfully invalidated the supplier.', 'dgConn.supplierDetails.invalidateSupplier.success', 'success');
                                }
                            } else {
                                this.sharedService.growlTranslation('An error occurred while trying to invalidate the supplier. Please, try again later.', 'dgConn.supplierDetails.invalidateSupplier.error', 'error');
                            }
                            this.closeModal();
                        }, error => {
                            this.sharedService.growlTranslation('An error occurred while trying to invalidate the supplier. Please, try again later.', 'dgConn.supplierDetails.invalidateSupplier.error', 'error');
                            this.closeModal();
                        }
                    );
                }
            }
        }
    }
}