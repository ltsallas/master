import {Component} from '@angular/core';
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import {Observable} from "rxjs";
import * as FileSaver from 'file-saver';
import {FinancialApi} from "../shared/swagger/api/FinancialApi";
import {ResponseDTO} from "../shared/swagger/model/ResponseDTO";


@Component({
	templateUrl: './abac.component.html',
	providers: [FinancialApi]
})
export class AbacComponent {
	private importUrl: string;
	private exportUrl: string;
	private exportEnabled: boolean;
	private jsonFile: File;


	constructor(private http: Http, private financialApi: FinancialApi) {
		this.importUrl = 'api/financial/importJson';
		this.exportUrl = 'api/financial/exportJson';
		this.exportEnabled = false;
	}

	exportJson() {
		this.http.get(this.exportUrl).subscribe(
			response => {
				let abacResponse = JSON.parse(response['_body']);
				if (abacResponse['success'] & abacResponse!['success']) {

					let blob = new Blob([abacResponse['data']], {type: 'application/json'});
					FileSaver.saveAs(blob, 'export.json');
				}
				window.alert(abacResponse['message']);
			}, error => {
				console.log(error);
				window.alert('Something went wrong');
			}
		);

	}

    onFileSelection(event) {
        this.exportEnabled = false;
        if (event && event.target && event.target.files && event.target.files.length == 1) {
            this.jsonFile = event.target.files['0'];
            let reader = new FileReader();

            reader.onload = (e) => {
            	this.financialApi.importJson(reader.result).subscribe(
					(response : ResponseDTO) => {
						if (response.success) {
                            this.exportEnabled = true;
                            window.alert("Import succesful!");
                        } else {
                            window.alert("Import failed!");
						}
					}, error => {
            			console.log("Error");
            			console.log(error);
                        window.alert("Import failed! Make sure that the file you are uploading has the correct format.");
					}
				);
            };
            reader.readAsText(this.jsonFile);
        }
    }

}



