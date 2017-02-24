import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {HomeComponent} from "./home/home.component";
import {VoucherComponent} from "./+beneficiary-portal/+voucher/voucher.component";
import {MapComponent} from "./+map/map.component";
import {ActivationComponent} from "./+activation/activation.component";
import {LoginComponent} from "./+login/login.component";
import {BeneficiaryProfileComponent} from "./+beneficiary-portal/profile/profile.component";
import {ForgotComponent} from "./+forgot/forgot.component";
import {DgConnPortalComponent} from "./dgconn-portal/dgconnportal.component";
import {DgConnTimelineComponent} from "./dgconn-portal/timeline/timeline.component";

@NgModule({
    imports: [RouterModule.forRoot([
        {
            path: '',
            redirectTo: 'home',
            pathMatch: 'full'
        }, {
            path: 'index.jsp',
            redirectTo: 'home'
        }, {
            path: 'home',
            component: HomeComponent
        }, {
            path: 'map',
            component: MapComponent
        }, {
            path: 'activation',
            component: ActivationComponent
        }, {
            path: 'login',
            component: LoginComponent
        }, {
            path: 'forgot',
            component: ForgotComponent
        }, {
            path: 'registration',
            loadChildren: 'app/+beneficiary-registration/registration.module#RegistrationModule'
        }, {
            path: 'beneficiary-portal',
            loadChildren: 'app/+beneficiary-portal/beneficiary-portal.module#BeneficiaryPortalModule'
        }, {
            path: 'dgconn-portal',
            component: DgConnPortalComponent
        }, {
            path: 'dgconn-portal/timeline',
            component: DgConnTimelineComponent
        }
    ], {useHash: true})],
    exports: [RouterModule]
})
export class AppRoutingModule {
}