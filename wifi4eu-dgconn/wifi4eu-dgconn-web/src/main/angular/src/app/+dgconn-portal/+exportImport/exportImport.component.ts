import { Component } from '@angular/core';
import { animate, style, transition, trigger } from '@angular/animations';
import { Http } from '@angular/http';
import { ExportImportApi } from '../../shared/swagger/api/ExportImportApi';
import { SharedService } from '../../shared/shared.service';
import { ApplicationApi } from '../../shared/swagger/api/ApplicationApi';
import { CallApi } from '../../shared/swagger/api/CallApi';
import { TranslateService } from 'ng2-translate';
import * as FileSaver from 'file-saver';
import { ActivatedRoute, Router } from '@angular/router';
import { LegalEntitiesService } from '../../services/legal-entities-service';

@Component({
    templateUrl: 'exportImport.component.html',
    styleUrls: ['./exportImport.component.scss'],
    providers: [CallApi, ApplicationApi, ExportImportApi, LegalEntitiesService],
    preserveWhitespaces: false,
    animations: [
        trigger(
            'enterSpinner', [
                transition(':enter', [
                    style({ opacity: 0 }),
                    animate('200ms', style({ opacity: 1 }))
                ]),
                transition(':leave', [
                    style({ opacity: 1 }),
                    animate('200ms', style({ opacity: 0 }))
                ])
            ]
        )
    ]
})
export class DgConnExportImportComponent {

    processingOperation: boolean = false;

    constructor(private http: Http, private exportImportApi: ExportImportApi, private sharedService: SharedService,
                private translateService: TranslateService, private legalEntitiesService: LegalEntitiesService,
                private router: Router, private route: ActivatedRoute) {
    }

    navigateToImportLefFile(): void {
        this.router.navigate(['lef'], { relativeTo: this.route });
    }

    navigateToImportBcFile(): void {
        this.router.navigate(['bc'], { relativeTo: this.route });
    }

    navigateToImportLcFile(): void {
        this.router.navigate(['lc'], { relativeTo: this.route });
    }

    exportBeneficiaryInformation() {
        this.processingOperation = true;
        this.exportImportApi.exportBeneficiaryInformation().subscribe(
            (response) => {
                this.saveFile('portal_exportBeneficiaryInformation.zip', response, 'text/zip');

                this.processingOperation = false;
            }, error => {
                this.sharedService.growlTranslation('An error occurred while trying to retrieve the data from the server. Please, try again later.', 'shared.error.api.generic', 'error');
                this.processingOperation = false;
            }
        );
    }

    exportBudgetaryCommitment() {
        this.processingOperation = true;
        this.exportImportApi.exportBudgetaryCommitment().subscribe(
            (response) => {
                this.saveFile('portal_exportBudgetaryCommitment.csv', response, 'text/csv');

                this.processingOperation = false;
            }, error => {
                this.sharedService.growlTranslation('An error occurred while trying to retrieve the data from the server. Please, try again later.', 'shared.error.api.generic', 'error');
                this.processingOperation = false;
            }
        );
    }

    exportLegalCommitment() {
        this.processingOperation = true;
        this.legalEntitiesService.exportLegalCommitment().subscribe(
            (response) => {
                this.saveFile('exportLegalCommitment.zip', response, 'text/zip');

                this.processingOperation = false;
            }, error => {
                this.sharedService.growlTranslation('An error occurred while trying to retrieve the data from the server. Please, try again later.', 'shared.error.api.generic', 'error');
                this.processingOperation = false;
            }
        );
    }

    private saveFile(fileName, fileData, fileType): void {
        const blob = new Blob([fileData], { type: fileType });
        FileSaver.saveAs(blob, fileName);

        this.sharedService.growlTranslation('Your file have been exported correctly!', 'dgconn.dashboard.card.messageExport', 'success');
    }

}