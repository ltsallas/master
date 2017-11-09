import {Component, Input, Output, EventEmitter} from "@angular/core";
import {UserDTOBase} from "../../shared/swagger/model/UserDTO";
import {MunicipalityDTOBase} from "../../shared/swagger/model/MunicipalityDTO";
import {NutsDTOBase} from "../../shared/swagger/model/NutsDTO";


@Component({
    selector: 'beneficiary-registration-step4', templateUrl: 'beneficiary-registration-step4.component.html'
})

export class BeneficiaryRegistrationStep4Component {
    @Input('initialUser') private initialUser: UserDTOBase;
    @Input('country') private country: NutsDTOBase;
    @Input('municipalities') private municipalities: MunicipalityDTOBase[];
    @Input('users') private users: UserDTOBase[];
    @Input('multipleMunicipalities') private multipleMunicipalities: boolean;
    @Input('userAddress') private userAddress: string;
    @Input('addressNum') private addressNum: string;
    @Input('postalCode') private postalCode: string;
    @Output() private onNext: EventEmitter<any>;
    @Output() private onBack: EventEmitter<any>;
    @Output() private onEdit: EventEmitter<number>;
    private displayModal: boolean;
    private legalChecks: boolean[] = [false, false, false];
    private successCaptcha: boolean = false;

    constructor() {
        this.onNext = new EventEmitter<any>();
        this.onBack = new EventEmitter<any>();
        this.onEdit = new EventEmitter<any>();
        this.displayModal = false;
    }

    completeCaptcha(response: any) {
        this.successCaptcha = response.success;
    }

    openModal() {
        this.displayModal = true;
    }

    submit() {
        if (this.legalChecks && this.successCaptcha) {
            this.onNext.emit();
        }
    }

    back() {
        this.onBack.emit();
    }

    edit(step: number) {
        this.onEdit.emit(step);
    }
}