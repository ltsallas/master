import { Component, OnInit } from '@angular/core';
import { UserApi } from "../shared/swagger/api/UserApi";
import { UserDTOBase } from '../shared/swagger';
import { LocalStorageService } from "angular-2-local-storage";
import { NutsApi } from "../shared/swagger/api/NutsApi";
import { NutsDTOBase } from "../shared/swagger/model/NutsDTO";
import { SharedService } from "../shared/shared.service";

@Component({
  selector: 'app-invited-contact-details',
  templateUrl: './invited-contact-details.component.html',
  styleUrls: ['./invited-contact-details.component.scss'],
  providers: [UserApi, NutsApi]
})

export class InvitedContactDetailsComponent {

  private user: UserDTOBase;
  private enableButton: boolean = false;
  private userInvited: UserDTOBase = new UserDTOBase();
  private countries: NutsDTOBase[] = [];

  constructor(private sharedService: SharedService, private localStorageService: LocalStorageService, private userApi: UserApi, private nutsApi: NutsApi) { 
    let storedUser = this.localStorageService.get('user');
    this.user = storedUser ? JSON.parse(storedUser.toString()) : null;
    this.nutsApi.getNutsByLevel(0).subscribe(
      (nuts: NutsDTOBase[]) => {
          this.countries = nuts;
      }, error => {
          console.log(error);
      }
    );
  }

  private checkButtonEnabled(){
    this.enableButton = this.userInvited.country != null && this.userInvited.country.trim() != "" 
    && this.userInvited.city != null && this.userInvited.city.trim() != "" 
    && this.userInvited.postalCode != null && this.userInvited.postalCode.trim() != "" 
    && this.userInvited.address != null && this.userInvited.address.trim() != ""
    && this.userInvited.addressNum != null && this.userInvited.addressNum.trim() != "";
  }

  private sendInvitedContactDetails(){
    if (this.userInvited.country != null && this.userInvited.country.trim() != "" 
    && this.userInvited.city != null && this.userInvited.city.trim() != "" 
    && this.userInvited.postalCode != null && this.userInvited.postalCode.trim() != "" 
    && this.userInvited.address != null && this.userInvited.address.trim() != ""
    && this.userInvited.addressNum != null && this.userInvited.addressNum.trim() != ""){
      
    } else {
      this.sharedService.growlTranslation('Please, fill all the fields to proceed with the registration', '', 'error');
    }
  }



}
