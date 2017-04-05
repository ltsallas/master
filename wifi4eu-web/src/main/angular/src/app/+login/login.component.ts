import {Component} from "@angular/core";
import {UxService} from "@ec-digit-uxatec/eui-angular2-ux-commons";
import {UserDTO, UserDTOBase} from "../shared/swagger/model/UserDTO";
import {Router} from "@angular/router";
import {UserApi} from "../shared/swagger/api/UserApi";
import {LocalStorageModule} from "angular-2-local-storage";
@Component({
    selector: 'login-component',
    templateUrl: 'login.component.html',
    providers: [UserApi]
})
export class LoginComponent {

    private displayConfirmingData: boolean;
    private userDTO: UserDTO;

    constructor(private userApi: UserApi, private uxService: UxService, private router: Router, private localStorageModule: LocalStorageModule) {
    this.displayConfirmingData = false;
    this.userDTO = new UserDTOBase();
    }

    onSubmit() {
        this.displayConfirmingData = true;

        this.userApi.login(this.userDTO).subscribe(
            data => {
                this.displayConfirmingData = false;
                if (data['success']) {
                    this.uxService.growl({
                        severity: 'success',
                        summary: 'SUCCESS',
                        detail: 'Login success'

                    });
                    console.log('SUCCESS: Login success');
                    this.router.navigateByUrl("beneficiary-portal/voucher")
                } else {
                    this.uxService.growl({
                        severity: 'error',
                        summary: 'ERROR',
                        detail: 'Could not login, with these user and password'
                    });
                    console.log('ERROR: Could not login, with these user password');

                }
            },
            error => {
                this.displayConfirmingData = false;
                this.uxService.growl({
                    severity: 'warn',
                    summary: 'WARNING',
                    detail: 'Could not login, ignore this when NG is working in offline mode'
                });
                console.log('WARNING: Could not login', error);
            }
        );
    }
}