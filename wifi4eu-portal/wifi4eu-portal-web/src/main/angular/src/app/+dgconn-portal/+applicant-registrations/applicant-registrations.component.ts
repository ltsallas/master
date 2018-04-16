import { Component } from "@angular/core";
import { animate, style, transition, trigger } from "@angular/animations";
import { ApplicationApi } from "../../shared/swagger/api/ApplicationApi";
import { ApplicantListItemDTOBase } from "../../shared/swagger/model/ApplicantListItemDTO";
import { ResponseDTOBase } from "../../shared/swagger/model/ResponseDTO";

@Component({
    templateUrl: 'applicant-registrations.component.html',
    providers: [ApplicationApi],
    animations: [
        trigger(
            'enterSpinner', [
                transition(':enter', [
                    style({opacity: 0}),
                    animate('200ms', style({opacity: 1}))
                ]),
                transition(':leave', [
                    style({opacity: 1}),
                    animate('200ms', style({opacity: 0}))
                ])
            ]
        )
    ]
})

export class DgConnApplicantRegistrationsComponent {
    private inputSearch: string = '';
    private applicantListItems: ApplicantListItemDTOBase[] = [];
    private totalItems: number = 0;
    private page: number = 0;
    private itemsPerPage: number = 5;
    private totalPages: number = 1;
    private rowsPerPageOptions: number[] = [5, 10, 20];
    private sortField: string = 'name';
    private sortOrder: number = 1;
    private loadingData: boolean = false;

    constructor(private applicationApi: ApplicationApi) {
    }

    private inputSearchApplicants() {
        this.page = 0;
        this.searchApplicants();
    }

    private searchApplicants() {
        this.loadingData = true;
        if (this.inputSearch.trim().length > 0) {
            this.applicationApi.findDgconnApplicantsListSearchingName(this.inputSearch.trim(), this.itemsPerPage * this.page, this.itemsPerPage, this.sortField, this.sortOrder).subscribe(
                (response: ResponseDTOBase) => {
                    this.loadingData = false;
                    if (response.success) {
                        this.totalItems = response.xtotalCount;
                        this.totalPages = this.totalItems / this.itemsPerPage;
                        this.applicantListItems = response.data;
                    }
                }
            );
        } else {
            this.applicationApi.findDgconnApplicantsList(this.itemsPerPage * this.page, this.itemsPerPage, this.sortField, this.sortOrder).subscribe(
                (response: ResponseDTOBase) => {
                    this.loadingData = false;
                    if (response.success) {
                        this.totalItems = response.xtotalCount;
                        this.totalPages = this.totalItems / this.itemsPerPage;
                        this.applicantListItems = response.data;
                    }
                }
            );
        }
    }

    private loadData(event) {
        if (event['rows'] != null)
            this.itemsPerPage = event['rows'];
        if (event['first'] != null)
            this.page = event['first'] / this.itemsPerPage;
        if (event['pageCount'] != null)
            this.totalPages = event['pageCount'];
        if (event['sortField'] != null)
            this.sortField = event['sortField'];
        if (event['sortOrder'] != null)
            this.sortOrder = event['sortOrder'];
        this.searchApplicants();
    }
}