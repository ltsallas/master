import {NgModule} from "@angular/core";
import {SharedModule} from "../shared/shared.module";
import {DgConnectPortalRoutingModule} from "./dgconnportal-routing.module";
import {DgConnPortalComponent} from "./dgconnportal.component";
import {DgConnTimelineComponent} from "./+timeline/timeline.component";
import {DgConnPublicationComponent} from "./+publication/publication.component";

@NgModule({
    imports: [
        SharedModule, DgConnectPortalRoutingModule
    ],
    declarations: [
        DgConnPortalComponent, DgConnTimelineComponent, DgConnPublicationComponent
    ],
    bootstrap: [DgConnPortalComponent]
})
export class DgConnPortalModule {
}