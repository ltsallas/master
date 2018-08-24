import { Component, OnInit } from '@angular/core';
import { UserApi } from "../shared/swagger/api/UserApi";
import { UserDTOBase, ResponseDTOBase, ResponseDTO } from '../shared/swagger';
import { LocalStorageService } from "angular-2-local-storage";
import { NutsApi } from "../shared/swagger/api/NutsApi";
import { NutsDTOBase } from "../shared/swagger/model/NutsDTO";
import { SharedService } from "../shared/shared.service";
import { Router } from "@angular/router";

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

  constructor(private sharedService: SharedService, private localStorageService: LocalStorageService, private userApi: UserApi, private nutsApi: NutsApi, private router: Router) { 
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
      this.userApi.completeInvitateContactDetails(this.userInvited).subscribe(
        (response: ResponseDTOBase) => {
            if (response.success){
              this.sharedService.growlTranslation('Your registration was successfully updated.', 'shared.registration.update.success', 'success');
              /*
              if (this.user && this.user.userInvitedFor == 3){
                this.router.navigateByUrl('/beneficiary-portal/profile');
              } else {
                this.router.navigateByUrl('/supplier-portal/profile');
              }
              */
             var port = window.location.port ? ':' + window.location.port : '';
             window.location.href = window.location.protocol + "//" + window.location.hostname + port+'/wifi4eu/index.html'
            } else {
              this.sharedService.growlTranslation('An error occurred and your registration could not be updated.', 'shared.registration.update.error', 'success');
            }
        },
        error => {
          this.sharedService.growlTranslation('An error occurred and your registration could not be updated.', 'shared.registration.update.error', 'success');
        }
      );
    } else {
      this.sharedService.growlTranslation('Please, fill all the fields to proceed with the registration', '', 'error');
    }
  }



}
