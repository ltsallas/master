import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SupplierPortalComponent } from "./supplier-portal.component";
import { SupplierInstallationComponent } from "./installation/supplier-installation.component";
import { SupplierProfileComponent } from "./profile/profile.component";
import { AdditionalInfoComponent } from "./+additional-info/additional-info.component";
import {DiscussionComponent} from "./+discussion/discussion.component";

@NgModule({
    imports: [RouterModule.forChild([
        {
            path: '',
            component: SupplierPortalComponent,
        }, {
            path: 'installation/:id',
            component: SupplierInstallationComponent,
        }, {
            path: 'discussion-forum/:threadId',
            component: DiscussionComponent
        }, {
            path: 'profile',
            component: SupplierProfileComponent,
        }, {
            path: 'additional-info',
            component: AdditionalInfoComponent
        }
    ])],
    exports: [RouterModule]
})
export class SupplierPortalRoutingModule {
}