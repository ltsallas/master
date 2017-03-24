import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {SupplierPortalComponent} from "./supplier-portal.component";
import {SupplierInstallationComponent} from "./installation/supplier-installation.component";
import {SupplierMunicipalitiesComponent} from "./municipalities/supplier-municipalities.component";
import {SupplierProfileComponent} from "./profile/profile.component";

@NgModule({
    imports: [RouterModule.forChild([
        {
            path: '',
            component: SupplierPortalComponent,
        }, {
            path: 'installation',
            component: SupplierInstallationComponent,
        }, {
            path: 'municipalities',
            component: SupplierMunicipalitiesComponent,
        }, {
            path: 'profile',
            component: SupplierProfileComponent,
        }
    ])],
    exports: [RouterModule]
})
export class SupplierPortalRoutingModule {
}