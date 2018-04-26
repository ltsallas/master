import { Component, Input, OnInit } from '@angular/core';
import { UxService } from '@eui/ux-commons';
import { Router, ActivatedRoute } from "@angular/router";
import { BeneficiaryDisplayedListDTOBase, BeneficiaryDisplayedListDTO, ResponseDTOBase } from '../../../../shared/swagger';
import { BeneficiaryService } from '../../../../core/services/beneficiary-service';
import { InstallationSiteBase } from '../../../../shared/swagger/model/InstallationSite';
import { InstallationsiteApi } from '../../../../shared/swagger/api/InstallationsiteApi';
import { Location } from '@angular/common';

@Component({
    templateUrl: './installation-details.component.html'
})
export class InstallationDetailsComponent implements OnInit {

    beneficiary: BeneficiaryDisplayedListDTOBase = new BeneficiaryDisplayedListDTOBase;

    private installationSite: InstallationSiteBase = new InstallationSiteBase();

    constructor(private uxService: UxService, private router: Router, private route: ActivatedRoute,
         private beneficiaryService: BeneficiaryService, private location: Location, private installationSiteApi: InstallationsiteApi) {
        if (this.beneficiaryService.beneficiarySelected != undefined) {
            this.beneficiary = this.beneficiaryService.beneficiarySelected;
        } else {
            router.navigate(['screen/installation-report']);
        }
    }

    ngOnInit() {
        this.route.data.subscribe((data: {installationSite: InstallationSiteBase})=> {
            this.installationSite = data.installationSite;
        });
    }

    openUpdateInstallationSite() {
        this.uxService.openModal('updateInstallationSite');
    }

    openRemoveInstallationSite() {
        this.uxService.openModal('removeInstallationSite');
    }

    submitRemoveInstallationSite() {
        this.installationSiteApi.removeInstallSite(this.installationSite.id).subscribe((response : ResponseDTOBase)=> {
            if(response.success){
                this.location.back();
            }
        });
        this.closeRemoveInstallationSite();
    }

    closeRemoveInstallationSite() {
        this.uxService.closeModal('removeInstallationSite');

    }
}
