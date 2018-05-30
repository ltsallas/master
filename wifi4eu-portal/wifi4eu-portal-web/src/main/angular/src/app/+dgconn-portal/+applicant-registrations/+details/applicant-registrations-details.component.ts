import { Component } from "@angular/core";
import { Location } from "@angular/common";
import { animate, style, transition, trigger } from "@angular/animations";
import { DomSanitizer } from "@angular/platform-browser";
import { ActivatedRoute } from "@angular/router";
import { SharedService } from "app/shared/shared.service";
import { ApplicationApi } from "../../../shared/swagger/api/ApplicationApi";
import { BeneficiaryApi } from "../../../shared/swagger/api/BeneficiaryApi";
import { MayorApi } from "../../../shared/swagger/api/MayorApi";
import { MunicipalityApi } from "../../../shared/swagger/api/MunicipalityApi";
import { RegistrationApi } from "../../../shared/swagger/api/RegistrationApi";
import { ThreadApi } from "../../../shared/swagger/api/ThreadApi";
import { UserApi } from "../../../shared/swagger/api/UserApi";
import { ApplicationDTOBase } from "../../../shared/swagger/model/ApplicationDTO";
import { MayorDTOBase } from "../../../shared/swagger/model/MayorDTO";
import { MunicipalityDTOBase } from "../../../shared/swagger/model/MunicipalityDTO";
import { RegistrationDTOBase } from "../../../shared/swagger/model/RegistrationDTO";
import { ThreadDTOBase } from "../../../shared/swagger/model/ThreadDTO";
import { ThreadMessageDTOBase } from "../../../shared/swagger/model/ThreadMessageDTO";
import { ResponseDTOBase } from "../../../shared/swagger/model/ResponseDTO";
import { UserDTOBase } from "../../../shared/swagger/model/UserDTO";
import { LegalFileDTOBase } from "../../../shared/swagger/model/LegalFileDTO";
import { TranslateService } from "ng2-translate";
import * as FileSaver from "file-saver";

@Component({
    templateUrl: 'applicant-registrations-details.component.html',
    providers: [ApplicationApi, BeneficiaryApi, MayorApi, MunicipalityApi, RegistrationApi, ThreadApi, UserApi],
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

export class DgConnApplicantRegistrationsDetailsComponent {
    private lauId: number = null;
    private callId: number = null;
    private municipalities: MunicipalityDTOBase[] = [];
    private mayors: MayorDTOBase[] = [];
    private registrations: RegistrationDTOBase[] = [];
    private applications: ApplicationDTOBase[] = [];
    private users: UserDTOBase[] = [];
    private discussionThread: ThreadDTOBase = null;
    private displayedMessages: ThreadMessageDTOBase[] = [];
    private legalFiles: LegalFileDTOBase[][] = [];
    private selectedFilesTypes: number[][] = [];
    private selectedReasonTypes: number[][] = [];
    private correctRequestLegalFilesModal: boolean = false;
    private selectedReasonsCount: number = 0;
    private typeFilesList: string[] = ['', '', '', ''];
    private invalidateReasonsList: string[] = ['', '', '', '', '', ''];
    private searchMessagesQuery: string = '';
    private registrationIssues: number[] = [];
    private selectedIndex = null;
    private invalidateReason: string = '';
    private displayValidate = false;
    private displayInvalidate = false;
    private displayRequestCorrection = false;
    private loadingData = false;
    private processingRequest = false;
    private registration: RegistrationDTOBase;

    constructor(private sanitizer: DomSanitizer, private route: ActivatedRoute, private sharedService: SharedService, private applicationApi: ApplicationApi, private beneficiaryApi: BeneficiaryApi, private registrationApi: RegistrationApi, private threadApi: ThreadApi, private userApi: UserApi, private municipalityApi: MunicipalityApi, private mayorApi: MayorApi, private translateService: TranslateService, private location: Location) {
        this.loadingData = true;
        this.route.params.subscribe(
            params => {
                this.lauId = params['lauId'];
                this.callId = params['callId'];
                this.getApplicationDetailsInfo();
            }
        );
        this.translatePageStrings();
    }

    private getApplicationDetailsInfo() {
        if (this.lauId && this.callId) {
            this.clearPageInfo();
            this.loadingData = true;
            this.applicationApi.getApplicationsByCallIdAndLauId(this.callId, this.lauId, new Date().getTime()).subscribe(
                (applications: ApplicationDTOBase[]) => {
                    let failCount = 0;
                    let correctCount = 0;
                    for (let i = 0; i < applications.length; i++) {
                        let application = applications[i];
                        this.registrationApi.getRegistrationById(application.registrationId).subscribe(
                            (registration: RegistrationDTOBase) => {
                                this.registration = registration;
                                if (registration) {
                                    this.userApi.getUserById(registration.userId).subscribe(
                                        (user: UserDTOBase) => {
                                            if (user) {
                                                this.municipalityApi.getMunicipalityById(registration.municipalityId).subscribe(
                                                    (municipality: MunicipalityDTOBase) => {
                                                        if (municipality) {
                                                            this.mayorApi.getMayorByMunicipalityId(municipality.id).subscribe(
                                                                (mayor: MayorDTOBase) => {
                                                                    this.registrationApi.getLegalFilesByRegistrationId(registration.id, new Date().getTime()).subscribe(
                                                                        (legalFiles: LegalFileDTOBase[]) => {
                                                                            if (mayor) {
                                                                                this.mayors[i] = mayor;
                                                                            } else {
                                                                                let mayor = new MayorDTOBase();
                                                                                mayor.id = -1;
                                                                                mayor.municipalityId = municipality.id;
                                                                                mayor.name = '-';
                                                                                mayor.surname = '-';
                                                                                mayor.email = '-';
                                                                                this.mayors[i] = mayor;
                                                                            }
                                                                            this.selectedFilesTypes[i] = [];
                                                                            this.selectedReasonTypes[i] = [];
                                                                            this.legalFiles[i] = this.createFrontEndLegalFiles(registration, legalFiles);
                                                                            this.applications[i] = application;
                                                                            this.registrations[i] = registration;
                                                                            this.users[i] = user;
                                                                            this.municipalities[i] = municipality;
                                                                            if (this.registrations.length == this.municipalities.length) {
                                                                                this.registrationIssues[i] = 0;
                                                                                this.setRegistrationIssue(registration, (this.registrationIssues.length - 1));
                                                                            }
                                                                            correctCount++;
                                                                            if (correctCount == (applications.length - failCount)) {
                                                                                this.loadingData = false;
                                                                            }
                                                                        }
                                                                    );
                                                                }, (error) => {
                                                                    this.loadingData = false;
                                                                }
                                                            );
                                                        } else {
                                                            failCount++;
                                                        }
                                                    }, (error) => {
                                                        this.loadingData = false;
                                                    }
                                                );
                                            } else {
                                                failCount++;
                                            }
                                        }, (error) => {
                                            this.loadingData = false;
                                        }
                                    );
                                } else {
                                    failCount++;
                                }
                            }, (error) => {
                                this.loadingData = false;
                            }
                        );
                    }
                }, (error) => {
                    this.loadingData = false;
                }
            );
            this.threadApi.getThreadByTypeAndReason(1, String(this.lauId)).subscribe(
                (thread: ThreadDTOBase) => {
                    if (thread) {
                        this.discussionThread = thread;
                        this.displayedMessages = thread.messages;
                    }
                }
            );
        } else {
            this.loadingData = false;
        }
    }

    private setRegistrationIssue(registration: RegistrationDTOBase, index: number) {
        this.registrationApi.getRegistrationIssue(registration).subscribe(
            (response: ResponseDTOBase) => {
                if (response.success) {
                    this.registrationIssues[index] = response.data;
                }
            }
        );
    }

    // TODO: Temporaly, the BLOB data will be read from the registration itself.
    private downloadLegalFile(index: number, typeNumber: number) {
        if (index != null) {
            for (let legalFile of this.legalFiles[index]) {
                if (legalFile.type == typeNumber) {
                    let lfBlobString = legalFile.data;
                    let lfMimeType = this.sharedService.getMimeType(lfBlobString);
                    let lfExtension = this.sharedService.getFileExtension(lfBlobString);
                    let lfData = this.sharedService.getBase64Data(lfBlobString);
                    let byteCharacters = atob(lfData);
                    let byteNumbers = new Array(byteCharacters.length);
                    for (let i = 0; i < byteCharacters.length; i++) {
                        byteNumbers[i] = byteCharacters.charCodeAt(i);
                    }
                    let byteArray = new Uint8Array(byteNumbers);
                    let blob = new Blob([byteArray], {type: lfMimeType});
                    FileSaver.saveAs(blob, 'legalfile' + typeNumber + lfExtension);
                }
            }
        }
    }

    private displayValidateModal(index: number) {
        if (index != null) {
            if (this.applications[index].status != 2) {
                this.selectedIndex = index;
                this.displayValidate = true;
            }
        }
    }

    private displayInvalidateModal(index: number) {
        if (index != null) {
            if (this.applications[index].status != 1) {
                this.selectedIndex = index;
                this.displayInvalidate = true;
            }
        }
    }

    private displayRequestCorrectionModal(index: number) {
        if (index != null) {
            if (this.selectedFilesTypes[index].length > 0) {
                for (let i = 0; i < this.selectedFilesTypes[index].length; i++) {
                    switch (this.selectedFilesTypes[index][i]) {
                        case 1:
                            for (let lf of this.legalFiles[index]) {
                                if (lf.type == 1 && lf.requestCorrection) {
                                    this.selectedReasonTypes[index][i] = lf.correctionReason;
                                }
                            }
                            break;
                        case 2:
                            for (let lf of this.legalFiles[index]) {
                                if (lf.type == 2 && lf.requestCorrection) {
                                    this.selectedReasonTypes[index][i] = lf.correctionReason;
                                }
                            }
                            break;
                        case 3:
                            for (let lf of this.legalFiles[index]) {
                                if (lf.type == 3 && lf.requestCorrection) {
                                    this.selectedReasonTypes[index][i] = lf.correctionReason;
                                }
                            }
                            break;
                        case 4:
                            for (let lf of this.legalFiles[index]) {
                                if (lf.type == 4 && lf.requestCorrection) {
                                    this.selectedReasonTypes[index][i] = lf.correctionReason;
                                }
                            }
                            break;
                    }
                }
                this.selectedIndex = index;
                this.displayRequestCorrection = true;
            }
        }
    }

    private closeModal() {
        this.selectedReasonTypes[this.selectedIndex] = [];
        this.selectedIndex = null;
        this.invalidateReason = '';
        this.correctRequestLegalFilesModal = false;
        this.displayValidate = false;
        this.displayInvalidate = false;
        this.displayRequestCorrection = false;
        this.processingRequest = false;
    }

    private selectCorrectionReason() {
        if (this.selectedReasonTypes[this.selectedIndex].length == this.selectedFilesTypes[this.selectedIndex].length) {
            let allReasonsCorrect = true;
            for (let selectedReason of this.selectedReasonTypes[this.selectedIndex]) {
                if (selectedReason == null) {
                    this.correctRequestLegalFilesModal = false;
                    break;
                }
            }
            if (allReasonsCorrect)
                this.correctRequestLegalFilesModal = true;
        }
    }

    private validateApplication() {
        if (!this.processingRequest) {
            if (this.selectedIndex != null && this.registration.allFilesFlag == 1) {
                this.processingRequest = true;
                this.applicationApi.validateApplication(this.applications[this.selectedIndex]).subscribe(
                    (response: ResponseDTOBase) => {
                        if (response.success) {
                            if (response.data != null) {
                                this.applications[this.selectedIndex].status = 2;
                                this.getApplicationDetailsInfo();
                                this.sharedService.growlTranslation('You successfully validated the municipality.', 'dgConn.duplicatedBeneficiaryDetails.validateMunicipality.success', 'success');
                            } else {
                                this.sharedService.growlTranslation('An error occurred while trying to validate the municipality. Please, try again later.', 'dgConn.duplicatedBeneficiaryDetails.validateMunicipality.error', 'error');
                            }
                        } else {
                            this.sharedService.growlTranslation('An error occurred while trying to validate the municipality. Please, try again later.', 'dgConn.duplicatedBeneficiaryDetails.validateMunicipality.error', 'error');
                        }
                        this.closeModal();
                    }, error => {
                        this.sharedService.growlTranslation('An error occurred while trying to validate the municipality. Please, try again later.', 'dgConn.duplicatedBeneficiaryDetails.validateMunicipality.error', 'error');
                        this.closeModal();
                    }
                );
            }
        }
    }

    private invalidateApplication() {
        if (!this.processingRequest) {
            if (this.selectedIndex != null && this.invalidateReason.trim().length > 0) {
                this.processingRequest = true;
                this.applications[this.selectedIndex].invalidateReason = this.invalidateReason;
                this.applicationApi.invalidateApplication(this.applications[this.selectedIndex]).subscribe(
                    (response: ResponseDTOBase) => {
                        if (response.success) {
                            if (response.data != null) {
                                this.applications[this.selectedIndex].status = 1;
                                this.getApplicationDetailsInfo();
                                this.sharedService.growlTranslation('You successfully invalidated the municipality.', 'dgConn.duplicatedBeneficiaryDetails.invalidateMunicipality.success', 'success');
                            } else {
                                this.sharedService.growlTranslation('An error occurred while trying to invalidate the municipality. Please, try again later.', 'dgConn.duplicatedBeneficiaryDetails.invalidateMunicipality.error', 'error');
                            }
                        } else {
                            this.sharedService.growlTranslation('An error occurred while trying to invalidate the municipality. Please, try again later.', 'dgConn.duplicatedBeneficiaryDetails.invalidateMunicipality.error', 'error');
                        }
                        this.closeModal();
                    }, error => {
                        this.sharedService.growlTranslation('An error occurred while trying to invalidate the municipality. Please, try again later.', 'dgConn.duplicatedBeneficiaryDetails.invalidateMunicipality.error', 'error');
                        this.closeModal();
                    }
                );
            }
        }
    }

    private requestLegalFilesCorrection() {
        if (!this.processingRequest) {
            if (this.selectedIndex != null) {
                this.processingRequest = true;

                let savedFilesCount = 0;
                for (let i = 0; i < this.selectedFilesTypes[this.selectedIndex].length; i++) {
                    let updatedLegalFile = new LegalFileDTOBase();
                    let fileType = this.selectedFilesTypes[this.selectedIndex][i];
                    for (let legalFile of this.legalFiles[this.selectedIndex]) {
                        if (legalFile.type == fileType) {
                            updatedLegalFile = legalFile;
                            updatedLegalFile.data = null;
                            updatedLegalFile.requestCorrection = true;
                            updatedLegalFile.correctionReason = this.selectedReasonTypes[this.selectedIndex][i];
                        }
                    }
                    this.registrationApi.saveLegalFile(updatedLegalFile).subscribe(
                        (resLegalFile: LegalFileDTOBase) => {
                            savedFilesCount++;
                            if (resLegalFile) {
                                if (savedFilesCount == this.selectedFilesTypes[this.selectedIndex].length) {
                                    this.applicationApi.sendLegalDocumentsCorrection(this.applications[this.selectedIndex]).subscribe(
                                        (response : ResponseDTOBase) => {
                                            this.selectedFilesTypes[this.selectedIndex] = [];
                                            this.getApplicationDetailsInfo();
                                            this.closeModal();
                                        }, error => {
                                            this.selectedFilesTypes[this.selectedIndex] = [];
                                            this.getApplicationDetailsInfo();
                                            this.closeModal();
                                        }
                                    );
                                }
                            }
                        }, error => {
                            savedFilesCount++;
                            if (savedFilesCount == this.selectedFilesTypes[this.selectedIndex].length) {
                                this.applicationApi.sendLegalDocumentsCorrection(this.applications[this.selectedIndex]).subscribe(
                                    (response : ResponseDTOBase) => {
                                        this.selectedFilesTypes[this.selectedIndex] = [];
                                        this.getApplicationDetailsInfo();
                                        this.closeModal();
                                    }, error => {
                                        this.selectedFilesTypes[this.selectedIndex] = [];
                                        this.getApplicationDetailsInfo();
                                        this.closeModal();
                                    }
                                );
                            }
                        }
                    );
                }
            }
        }
    }

    private searchMessages() {
        if (this.searchMessagesQuery.length > 0) {
            this.displayedMessages = [];
            for (let message of this.discussionThread.messages) {
                if (message.message.toLowerCase().indexOf(this.searchMessagesQuery.toLowerCase()) != -1) {
                    this.displayedMessages.push(message);
                }
            }
        } else {
            this.displayedMessages = this.discussionThread.messages;
        }
    }

    private clearPageInfo() {
        this.municipalities = [];
        this.mayors = [];
        this.registrations = [];
        this.applications = [];
        this.users = [];
        this.registrationIssues = []
    }

    private translatePageStrings() {
        this.translateService.get('dgConn.applicantDetails.legalFile.name.type1').subscribe(
            (translatedString: string) => {
                this.typeFilesList[0] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.name.type3').subscribe(
            (translatedString: string) => {
                this.typeFilesList[1] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.name.type2').subscribe(
            (translatedString: string) => {
                this.typeFilesList[2] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.name.type4').subscribe(
            (translatedString: string) => {
                this.typeFilesList[3] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.reason.cause1').subscribe(
            (translatedString: string) => {
                this.invalidateReasonsList[0] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.reason.cause2').subscribe(
            (translatedString: string) => {
                this.invalidateReasonsList[1] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.reason.cause3').subscribe(
            (translatedString: string) => {
                this.invalidateReasonsList[2] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.reason.cause4').subscribe(
            (translatedString: string) => {
                this.invalidateReasonsList[3] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.reason.cause5').subscribe(
            (translatedString: string) => {
                this.invalidateReasonsList[4] = translatedString;
            }
        );
        this.translateService.get('dgConn.applicantDetails.legalFile.reason.cause6').subscribe(
            (translatedString: string) => {
                this.invalidateReasonsList[5] = translatedString;
            }
        );
    }

    private createFrontEndLegalFiles(registration: RegistrationDTOBase, legalFiles?: LegalFileDTOBase[]) {
        let finalLegalFiles = [];
        let lf1 = new LegalFileDTOBase();
        let lf1AlreadyExists = false;
        let lf2 = new LegalFileDTOBase();
        let lf2AlreadyExists = false;
        let lf3 = new LegalFileDTOBase();
        let lf3AlreadyExists = false;
        let lf4 = new LegalFileDTOBase();
        let lf4AlreadyExists = false;
        if (legalFiles != null) {
            for (let legalFile of legalFiles) {
                switch (legalFile.type) {
                    case 1:
                        lf1 = legalFile;
                        lf1AlreadyExists = true;
                        break;
                    case 2:
                        lf2 = legalFile;
                        lf2AlreadyExists = true;
                        break;
                    case 3:
                        lf3 = legalFile;
                        lf3AlreadyExists = true;
                        break;
                    case 4:
                        lf4 = legalFile;
                        lf4AlreadyExists = true;
                        break;
                }
            }
        }
        if (registration.legalFile1 != null) {
            lf1.data = registration.legalFile1;
            if (!lf1AlreadyExists) {
                lf1.registrationId = registration.id;
                lf1.type = 1;
                lf1.uploadTime = registration.uploadTime;
            }
            finalLegalFiles.push(lf1);
        }
        if (registration.legalFile2 != null) {
            lf2.data = registration.legalFile2;
            if (!lf2AlreadyExists) {
                lf2.registrationId = registration.id;
                lf2.type = 2;
                lf2.uploadTime = registration.uploadTime;
            }
            finalLegalFiles.push(lf2);
        }
        if (registration.legalFile3 != null) {
            lf3.data = registration.legalFile3;
            if (!lf3AlreadyExists) {
                lf3.registrationId = registration.id;
                lf3.type = 3;
                lf3.uploadTime = registration.uploadTime;
            }
            finalLegalFiles.push(lf3);
        }
        if (registration.legalFile4 != null) {
            lf4.data = registration.legalFile4;
            if (!lf4AlreadyExists) {
                lf4.registrationId = registration.id;
                lf4.type = 4;
                lf4.uploadTime = registration.uploadTime;
            }
            finalLegalFiles.push(lf4);
        }
        return finalLegalFiles;
    }

    private goBack() {
        this.location.back();
    }
}