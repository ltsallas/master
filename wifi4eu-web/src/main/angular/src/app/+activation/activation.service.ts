import {Injectable} from "@angular/core";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import {UxService} from "@ec-digit-uxatec/eui-angular2-ux-commons";
import {Observable} from "rxjs";
import {ActivationDetails} from "./activation-details.model";

@Injectable()
export class ActivationService {

    private activationUrl: string;

    constructor(private http: Http, private uxService: UxService) {
        this.activationUrl = 'api/user/activateaccount'
    }

    private extractData(response: Response) {
        let body = response.json();
        return body.data || body.error || {};
    }

    addNewPassword(body: ActivationDetails): Observable <ActivationDetails> {
        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({headers: headers});

        return this.http.post(this.activationUrl, body , options)
            .map(this.extractData)
            .catch(this.uxService.handleError);
    }


}