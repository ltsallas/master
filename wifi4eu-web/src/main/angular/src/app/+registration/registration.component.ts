import {Component} from "@angular/core";
import {EntityDetails} from "../+entity/entity-details.model";
import {BeneficiaryDetails} from "../+beneficiary/beneficiary-details.model";

@Component({templateUrl: 'registration.component.html'})
export class RegistrationComponent {
    private entityDetails: EntityDetails;
    private beneficiaryDetails: BeneficiaryDetails;
    private completedSteps: boolean[];
    private activeSteps: boolean[];
    private currentStep: number;

    private completed: boolean[];
    private active: boolean[];

    private successRegistration: boolean = false;
    private failureRegistration: boolean = false;

    constructor() {
        console.log("Constructor");
        this.entityDetails = new EntityDetails();
        this.beneficiaryDetails = new BeneficiaryDetails();
        this.completed = [false, false, false];
        this.active = [true, false, false];
    }

    onNext(step: number) {
        this.completed[step - 1] = true;
        this.active[step - 1] = false;
        this.active[step] = true;
        console.log("Completed", this.completed);
        console.log("Active", this.active);
    }
    
    gotoStep(step: number) {
        switch (step) {
            case 1:
                this.completed = [false, false, false];
                this.active = [true, false, false];
                break;
            case 2:
                this.completed = [true, false, false];
                this.active = [false, true, false];
                break;
            case 3:
                this.completed = [true, true, false];
                this.active = [false, false, true];
                break;
        }

    onBack(step: number) {
        this.completed[step - 1] = false;
        this.active[step - 1] = true;
        this.active[step] = false;
        console.log("Completed", this.completed);
        console.log("Active", this.active);
    }
}