import {NgModule} from "@angular/core";
import {SharedModule} from "../shared/shared.module";
import {DgConnectPortalRoutingModule} from "./dgconnportal-routing.module";
import {DgConnPortalComponent} from "./dgconnportal.component";
import {DgConnPublicationComponent} from "./+publication/publication.component";
import {DgConnVoucherComponent} from "app/+dgconn-portal/+voucher/voucher.component";
import {DgConnStatisticsComponent} from "./+statistics/statistics.component";
import {DgConnBeneficiaryRegistrationsComponent} from "./+beneficiary-registrations/beneficiary-registrations.component";
import {DgConnSupplierRegistrationsComponent} from "./+supplier-registrations/supplier-registrations.component";
import {DgConnApplicantRegistrationsComponent} from "./+applicant-registrations/applicant-registrations.component";
import {DgConnBeneficiaryRegistrationsDetailsComponent} from "./+beneficiary-registrations/+details/beneficiary-registrations-details.component";
import {DgConnSupplierRegistrationsDetailsComponent} from "./+supplier-registrations/details/supplier-registrations-details.component";
import {DgConnApplicantRegistrationsDetailsComponent} from "./+applicant-registrations/+details/applicant-registrations-details.component";
import {DgConnManageLauComponent} from "./+manage-lau/manage-lau.component";
import {DgConnDiscussionComponent} from "./+discussion/discussion.component";
import { PaginatorModule, OverlayPanelModule, TooltipModule } from "primeng/primeng";
import {SharedModule as SharedNgModule} from 'primeng/primeng';

@NgModule({
    imports: [
        SharedModule, DgConnectPortalRoutingModule, SharedNgModule, OverlayPanelModule, TooltipModule
    ],
    declarations: [
        DgConnPortalComponent,
        DgConnPublicationComponent,
        DgConnVoucherComponent,
        DgConnStatisticsComponent,
        DgConnBeneficiaryRegistrationsComponent,
        DgConnSupplierRegistrationsComponent,
        DgConnApplicantRegistrationsComponent,
        DgConnBeneficiaryRegistrationsDetailsComponent,
        DgConnSupplierRegistrationsDetailsComponent,
        DgConnApplicantRegistrationsDetailsComponent,
        DgConnManageLauComponent,
        DgConnDiscussionComponent
    ],
    bootstrap: [DgConnPortalComponent]
})
export class DgConnPortalModule {
}