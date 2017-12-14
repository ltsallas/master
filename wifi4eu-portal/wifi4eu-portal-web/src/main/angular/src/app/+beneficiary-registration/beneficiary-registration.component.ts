import {Component} from "@angular/core";
import {UserDTOBase} from "../shared/swagger/model/UserDTO";
import {MunicipalityDTOBase} from "../shared/swagger/model/MunicipalityDTO";
import {BeneficiaryDTOBase} from "../shared/swagger/model/BeneficiaryDTO";
import {OrganizationDTOBase} from "../shared/swagger/model/OrganizationDTO";
import {BeneficiaryApi} from "../shared/swagger/api/BeneficiaryApi";
import {ResponseDTOBase} from "../shared/swagger/model/ResponseDTO";
import {NutsDTOBase} from "../shared/swagger/model/NutsDTO";
import {NutsApi} from "../shared/swagger/api/NutsApi";
import {OrganizationApi} from "../shared/swagger/api/OrganizationApi";
import {Subscription} from "rxjs/Subscription";
import {MayorDTOBase} from "../shared/swagger/model/MayorDTO";
import {LauDTOBase} from "../shared/swagger/model/LauDTO";
import {Router} from "@angular/router";

@Component({
    selector: 'beneficiary-registration',
    templateUrl: 'beneficiary-registration.component.html',
    providers: [BeneficiaryApi, NutsApi, OrganizationApi]
})

export class BeneficiaryRegistrationComponent {
    private successRegistration: boolean = false;
    private failureRegistration: boolean = false;
    private completed: boolean[] = [false, false, false, false];
    private active: boolean[] = [true, false, false, false];
    private initialUser: UserDTOBase = new UserDTOBase();
    private mayors: MayorDTOBase[] = [new MayorDTOBase()];
    private municipalities: MunicipalityDTOBase[] = [new MunicipalityDTOBase()];
    private laus: LauDTOBase[] = [];
    private finalBeneficiary: BeneficiaryDTOBase = new BeneficiaryDTOBase();
    private country: NutsDTOBase = null;
    private multipleMunicipalities: boolean = false;
    private organizations: OrganizationDTOBase[] = [];
    private countries: NutsDTOBase[] = [];
    private organizationsSubscription: Subscription = new Subscription();
    private alreadyRegistered: boolean = false;
    private organization: OrganizationDTOBase = null;

    constructor(private beneficiaryApi: BeneficiaryApi, private nutsApi: NutsApi, private organizationApi: OrganizationApi, private router: Router) {
        this.nutsApi.getNutsByLevel(0).subscribe(
            (nuts: NutsDTOBase[]) => {
                this.countries = nuts;
            }, error => {
                console.log(error);
            }
        );
    }

    private selectCountry(country: NutsDTOBase) {
        if (this.country != country) {
            this.country = country;
            this.organization = null;
            this.organizationsSubscription = this.organizationApi.getOrganizationsByCountry(country.countryCode).subscribe(
                (organizations: OrganizationDTOBase[]) => {
                    this.organizations = organizations;
                }
            );
            this.laus = [];
            this.municipalities = [new MunicipalityDTOBase()];
            this.mayors = [new MayorDTOBase()];
            this.initialUser = new UserDTOBase();
        }
    }

    private navigate(step: number) {
        switch (step) {
            case 1:
                this.completed = [false, false, false, false];
                this.active = [true, false, false, false];
                break;
            case 2:
                this.completed = [true, false, false, false];
                this.active = [false, true, false, false];
                break;
            case 3:
                this.completed = [true, true, false, false];
                this.active = [false, false, true, false];
                break;
            case 4:
                this.completed = [true, true, true, false];
                this.active = [false, false, false, true];
                break;
        }
    }

    private submitRegistration() {
        this.finalBeneficiary.municipalities = [];
        for (let municipality of this.municipalities) {
            this.finalBeneficiary.municipalities.push(municipality);
        }
        this.finalBeneficiary.mayors = [];
        for (let mayor of this.mayors) {
            this.finalBeneficiary.mayors.push(mayor);
        }
        this.initialUser.type = 3;
        this.finalBeneficiary.user = this.initialUser;
        this.alreadyRegistered = true;
        this.beneficiaryApi.submitBeneficiaryRegistration(this.finalBeneficiary).subscribe(
            (data: ResponseDTOBase) => {
                if (data.success) {
                    this.successRegistration = true;
                    this.router.navigateByUrl('/beneficiary-portal');
                } else {
                    this.failureRegistration = true;
                    this.alreadyRegistered = (data.error.errorMessage === "User already registered.");
                }
            }, error => {
                this.failureRegistration = true;
                this.alreadyRegistered = (error.errorMessage === "User already registered.");
            }
        );
    }
}