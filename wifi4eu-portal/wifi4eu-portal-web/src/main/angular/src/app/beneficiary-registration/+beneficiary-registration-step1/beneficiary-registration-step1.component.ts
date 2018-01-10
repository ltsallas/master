import { Component, EventEmitter, Input, Output } from "@angular/core";
import { NutsDTOBase } from "../../shared/swagger/model/NutsDTO";
import { OrganizationDTOBase } from "../../shared/swagger/model/OrganizationDTO";
import { MunicipalityDTOBase } from "../../shared/swagger/model/MunicipalityDTO";
import { UserDTOBase } from "../../shared/swagger/model/UserDTO";
import { MayorDTOBase } from "../../shared/swagger/model/MayorDTO";
import { LauDTOBase } from "../../shared/swagger/model/LauDTO";
import { SharedService } from "../../shared/shared.service";

@Component({
  selector: 'beneficiary-registration-step1', templateUrl: 'beneficiary-registration-step1.component.html'
})

export class BeneficiaryRegistrationStep1Component {
  @Input('countries') private countries: NutsDTOBase[];
  @Input('country') private country: NutsDTOBase;
  @Input('organizations') private organizations: OrganizationDTOBase[];
  @Input('multipleMunicipalities') private multipleMunicipalities: boolean;

  @Output() private countryChange: EventEmitter<NutsDTOBase>;
  @Output() private multipleMunicipalitiesChange: EventEmitter<boolean>;
  @Output() private onNext: EventEmitter<any>;

  private countrySelected: boolean = false;
  private singleMunicipalityCheckbox: boolean = false;
  private multipleMunicipalityCheckbox: boolean = false;

  @Input('organization') private organization: OrganizationDTOBase;
  @Output() private organizationChange: EventEmitter<OrganizationDTOBase>;
  private organizationSelected: boolean = false;

  constructor(private sharedService: SharedService) {
    this.countryChange = new EventEmitter<NutsDTOBase>();
    this.organizationChange = new EventEmitter<OrganizationDTOBase>();
    this.multipleMunicipalitiesChange = new EventEmitter<boolean>();
    this.onNext = new EventEmitter<any>();
  }

  private selectCountry(event: any) {
    this.singleMunicipalityCheckbox = false;
    this.multipleMunicipalityCheckbox = false;
    if (this.country != null) {
      this.countrySelected = true;
      this.countryChange.emit(this.country);
      this.sharedService.clean();
    }
  }

  private chooseSingleMunicipality(checked: boolean) {
    this.singleMunicipalityCheckbox = false;
    this.multipleMunicipalityCheckbox = false;
    if (checked) {
      this.singleMunicipalityCheckbox = true;
    }
  }

  private chooseMultipleMunicipality(checked: boolean) {
    this.singleMunicipalityCheckbox = false;
    this.multipleMunicipalityCheckbox = false;
    if (checked) {
      this.multipleMunicipalityCheckbox = true;
    }
  }

  private submit() {
    if (this.singleMunicipalityCheckbox) {
      this.multipleMunicipalities = false;
    } else if (this.multipleMunicipalityCheckbox) {
      this.multipleMunicipalities = true;
    }
    this.countryChange.emit(this.country);
    this.organizationChange.emit(this.organization);
    this.multipleMunicipalitiesChange.emit(this.multipleMunicipalities);
    this.onNext.emit();
  }

  private selectOrganization(event: any) {
    if (this.organization != null) {
      this.organizationSelected = true;
      if(this.organization == "municipality"){
        this.chooseSingleMunicipality(true);
      }
      else{
        this.chooseMultipleMunicipality(true);
      }
    }
  }
}