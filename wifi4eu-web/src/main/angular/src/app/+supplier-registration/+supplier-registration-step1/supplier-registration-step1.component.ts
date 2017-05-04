import {Component, Input, Output, EventEmitter} from "@angular/core";
import {SupplierDTOBase} from "../../shared/swagger/model/SupplierDTO";
import {FileUploadModule} from 'primeng/primeng';

@Component({
    selector: 'supplier-registration-step1-component',
    templateUrl: 'supplier-registration-step1.component.html'
})
export class SupplierRegistrationComponentStep1 {
    @Input('supplierDTO') supplierDTO: SupplierDTOBase;

    @Output() onNext = new EventEmitter<number>();
    @Output() onLogoSubmit = new EventEmitter<any>();

    //private webPattern: string = "@^(http\:\/\/|https\:\/\/)?([a-z0-9][a-z0-9\-]*\.)+[a-z0-9][a-z0-9\-]*$@i";
    private webPattern: string = "(https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|www\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9]\.[^\s]{2,}|www\.[a-zA-Z0-9]\.[^\s]{2,})";
    private uploadedFiles: any[] = [];

    onSubmit(step: number) {
        this.onNext.emit(step);
    }

    onSelect(event) {
		console.log("onSelect: ");
		console.log(event);
        console.log(event.files);
        if (event && event.files && event.files.length > 0) {
            console.log(event.files.length);
            console.log(event.files["0"]);

            this.onLogoSubmit.emit(event.files["0"]);
        }
	}
}
