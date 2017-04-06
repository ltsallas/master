import {Component, Input, OnInit} from "@angular/core";
import {UxService} from "@ec-digit-uxatec/eui-angular2-ux-commons";
import {ActivatedRoute, Router} from "@angular/router";
import {UserApi} from "../shared/swagger/api/UserApi";
import {ActivateAccountDTO, ActivateAccountDTOBase} from "../shared/swagger/model/ActivateAccountDTO";

@Component({templateUrl: 'activation.component.html', providers: [UserApi]})
export class ActivationComponent implements OnInit {

    @Input('activationDTO') activationDTO: ActivateAccountDTO;
    private repeatPassword: string;

    constructor(private userApi: UserApi, private uxService: UxService, private route: ActivatedRoute, private router: Router) {
        this.activationDTO = new ActivateAccountDTOBase();
    }

    ngOnInit() {
        this.route.params.subscribe(params => this.activationDTO.token = params['token']);
    }

    checkPassword() {
        return this.activationDTO.password === this.repeatPassword;
    }

    onSubmit() {
        this.userApi.activateAccount(this.activationDTO).subscribe(
            data => {
                this.uxService.growl({
                    severity: 'success',
                    summary: 'SUCCESS',
                    detail: 'User activation success'
                });
                console.log('SUCCESS: User activation success');
                this.router.navigateByUrl("/login");
            },
            error => {
                this.uxService.growl({
                    severity: 'warn',
                    summary: 'WARNING',
                    detail: 'Could not get activate account, ignore this when NG is working in offline mode'
                });
                console.log('WARNING: Could not get activate account', error);
            }
        );
    }

}
