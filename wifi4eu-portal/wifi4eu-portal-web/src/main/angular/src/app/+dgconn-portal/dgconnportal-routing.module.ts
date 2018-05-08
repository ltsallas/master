import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {DgConnPortalComponent} from "./dgconnportal.component";
import {DgConnPublicationComponent} from "./+publication/publication.component";
import {DgConnVoucherComponent} from "app/+dgconn-portal/+voucher/voucher.component";
import {DgConnStatisticsComponent} from "./+statistics/statistics.component";
import {DgConnSupplierRegistrationsComponent} from "./+supplier-registrations/supplier-registrations.component";
import {DgConnBeneficiaryRegistrationsComponent} from "./+beneficiary-registrations/beneficiary-registrations.component";
import {DgConnApplicantRegistrationsComponent} from "./+applicant-registrations/applicant-registrations.component";
import {DgConnManageLauComponent} from "./+manage-lau/manage-lau.component";
import {DgConnDiscussionComponent} from "./+discussion/discussion.component";
import {DgConnBeneficiaryRegistrationsDetailsComponent} from "./+beneficiary-registrations/+details/beneficiary-registrations-details.component";
import {DgConnSupplierRegistrationsDetailsComponent} from "./+supplier-registrations/details/supplier-registrations-details.component";
import {DgConnApplicantRegistrationsDetailsComponent} from "./+applicant-registrations/+details/applicant-registrations-details.component";
import {DgConnExportImportComponent} from "./+exportImport/exportImport.component";

@NgModule({
    imports: [RouterModule.forChild([
        {
            path: '',
            component: DgConnPortalComponent,
        }, {
            path: 'publication',
            component: DgConnPublicationComponent,
        }, {
            path: 'voucher',
            component: DgConnVoucherComponent,
        }, {
            path: 'statistics',
            component: DgConnStatisticsComponent,
        }, {
            path: 'supplier-registrations',
            component: DgConnSupplierRegistrationsComponent,
        }, {
            path: 'supplier-registrations/:id',
            component: DgConnSupplierRegistrationsDetailsComponent
        }, {
            path: 'beneficiary-registrations',
            component: DgConnBeneficiaryRegistrationsComponent,
        }, {
            path: 'beneficiary-registrations/:id',
            component: DgConnBeneficiaryRegistrationsDetailsComponent,
        }, {
            path: 'manage-lau',
            component: DgConnManageLauComponent,
        }, {
            path: 'discussion',
            component: DgConnDiscussionComponent,
       }, {
           path: 'dgconn-portal/exportImport',
           component: DgConnExportImportComponent,
       }


    ])],
    exports: [RouterModule]
})
export class DgConnectPortalRoutingModule {
}