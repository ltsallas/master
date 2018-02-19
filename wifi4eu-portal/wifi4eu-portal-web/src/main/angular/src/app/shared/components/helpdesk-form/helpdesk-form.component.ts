import { Component, Input, Inject } from "@angular/core";
import { ChangeDetectorRef } from "@angular/core";
import { ElementRef } from "@angular/core";
import { LocalStorageService } from "angular-2-local-storage";
import { SharedService } from "../../shared.service";
import { HelpdeskissuesApi } from "../../swagger/api/HelpdeskissuesApi";
import { HelpdeskIssueDTOBase } from "../../swagger/model/HelpdeskIssueDTO";
import { NutsApi } from "../../swagger/api/NutsApi";
import { NutsDTOBase } from "../../swagger/model/NutsDTO";
import { ResponseDTOBase } from "../../swagger/model/ResponseDTO";
import {UserDTOBase} from "../../swagger/model/UserDTO";

@Component({
    selector: 'helpdesk-form-component',
    templateUrl: 'helpdesk-form.component.html',
    providers: [HelpdeskissuesApi, NutsApi]
})
export class HelpdeskFormComponent {
    private helpdeskIssue: HelpdeskIssueDTOBase = new HelpdeskIssueDTOBase();
    private user: UserDTOBase = null;
    private expanded: boolean = false;
    private success: boolean = false;
    private countries: NutsDTOBase[] = [];
    @Input('portal') portal: string;

    constructor(private sharedService: SharedService, private localStorageService: LocalStorageService, private nutsApi: NutsApi, private helpdeskApi: HelpdeskissuesApi,
                @Inject(ElementRef) private elementRef: ElementRef,
                @Inject(ChangeDetectorRef) private changeDetectorRef: ChangeDetectorRef) {
        let storedUser = this.localStorageService.get('user');
        this.user = storedUser ? JSON.parse(storedUser.toString()) : null;
        this.nutsApi.getNutsByLevel(0).subscribe(
            nuts => {
                this.countries = nuts;
            },
            error => {
                console.log('WARNING: Could not get nuts', error);
            }
        );
    }

    private gotoHelpdeskForm () {
        this.expanded = true;
        this.changeDetectorRef.detectChanges();
        const child = this.elementRef.nativeElement.querySelector('#helpdesk-form');
        child.scrollIntoView({ behavior: 'smooth' });
    }

    private closeHelpdeskForm(){
        const parent = this.elementRef.nativeElement.querySelector('.container');
        const link = this.elementRef.nativeElement.querySelector('.label');
        window.scrollBy({
            top: -(parent.offsetHeight - link.offsetHeight),
            behavior: 'smooth'
        });
        let that = this;
        setTimeout(function(){ that.expanded = false; }, 400);
    }

    private sendIssue() {
        if (this.user != null) {
            this.helpdeskIssue.portal = this.portal;
            this.helpdeskIssue.createDate = Date.now();
            this.helpdeskIssue.assignedTo = 'Member State';
            this.helpdeskIssue.status = 0;
            this.helpdeskIssue.fromEmail = this.user.ecasEmail;
            this.helpdeskApi.createHelpdeskIssue(this.helpdeskIssue).subscribe(
                (response: ResponseDTOBase) => {
                    if (response.success) {
                        this.success = true;
                        this.sharedService.growlTranslation('Your issue was sent successfully sent to your Member State! We\'ll contact you as soon as possible to give you a solution to your problem.', 'helpdesk.helpdeskform.success', 'success');
                    }
                }, error => {
                    this.sharedService.growlTranslation('An error occurred while trying to retrieve the data from the server. Please, try again later.', 'shared.error.api.generic', 'error');
                }
            );
        } else {
            this.sharedService.growlTranslation('An error occurred while trying to retrieve the data from the server. Please, try again later.', 'shared.error.api.generic', 'error');
        }
    }
}
