import {NgModule} from "@angular/core";
import {SharedModule} from "../shared/shared.module";
import {DgConnectPortalRoutingModule} from "./dgconnportal-routing.module";
import {DgConnPortalComponent} from "./dgconnportal.component";
import {DgConnTimelineComponent} from "./+timeline/timeline.component";
import {DgConnVoucherComponent} from "./+voucher/voucher.component";
import {DgConnPublicationComponent} from "./+publication/publication.component";
import {DgConnRegistrationsComponent} from "./+registrations/registrations.component";

@NgModule({
    imports: [
        SharedModule, DgConnectPortalRoutingModule
    ],
    declarations: [
        DgConnPortalComponent, DgConnTimelineComponent, DgConnPublicationComponent, DgConnVoucherComponent, DgConnRegistrationsComponent
    ],
    bootstrap: [DgConnPortalComponent]
})
export class DgConnPortalModule {
}