// tslint:disable:no-unused-variable member-ordering
// noinspection ES6UnusedImports

import {serialize, deserialize, deserializeArray, plainToClass, classToPlain} from 'class-transformer';
import {Inject, Injectable, Optional, OpaqueToken} from '@angular/core';
import {
  Http,
  Headers,
  URLSearchParams,
  RequestMethod,
  RequestOptions,
  RequestOptionsArgs,
  Response,
  ResponseContentType
} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import * as models from '../model/models';
import {BASE_PATH} from '../variables';
import {Configuration} from '../configuration';
import {ClassType} from '../classType';


export const IUserApi = new OpaqueToken('IUserApi');

export interface IUserApi {


    /**
     * Activate account or reset password
     * 
     * @param c 
     * @param body 
     */
    activateAccount<T extends models.ResponseDTO>(body?: models.ActivateAccountDTO, c?: ClassType<T>): Observable<T>;
    /**
     * Service to change password of a user
     * 
     * @param c 
     * @param userId 
     * @param body 
     */
    changePassword<T extends models.ResponseDTO>(userId: number, body?: string, c?: ClassType<T>): Observable<T>;
    /**
     * Send forgot password mail with a link to reset password
     * 
     * @param c 
     * @param body 
     */
    forgotPassword<T extends models.ResponseDTO>(body?: string, c?: ClassType<T>): Observable<T>;
    /**
     * Service to do Login with a user email and SHA512 password
     * 
     * @param c 
     * @param body 
     */
    login<T extends models.ResponseDTO>(body?: models.UserDTO, c?: ClassType<T>): Observable<T>;
    /**
     * Service to logout
     * 
     * @param c 
     * @param body 
     */
    logout<T extends models.ResponseDTO>(body?: string, c?: ClassType<T>): Observable<T>;
    /**
     * Service to refresh the Authentication token
     * 
     * @param c 
     * @param body 
     */
    refreshToken<T extends models.ResponseDTO>(body?: string, c?: ClassType<T>): Observable<T>;
    /**
     * Service to resend email with a link to activate account
     * 
     * @param c 
     * @param body 
     */
    resendEmail<T extends models.ResponseDTO>(body?: models.BeneficiaryDTO, c?: ClassType<T>): Observable<T>;

}

@Injectable()
export class UserApi implements IUserApi {
    protected basePath = '/';
    public defaultHeaders: Headers = new Headers();
    public configuration: Configuration = new Configuration();

    constructor(protected http: Http, @Optional() @Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration){
        this.basePath=document.querySelector("html head base").getAttribute("href");
        if (configuration) {
            this.configuration = configuration;
        }
    }



    /**
     * Activate account or reset password
     * 
     * @param c
     * @param body 
     */
    activateAccount<T extends models.ResponseDTO>(body?: models.ActivateAccountDTO, c?: ClassType<T>): Observable<T> {
        // noinspection TypeScriptValidateTypes
        return this.activateAccountWithHttpInfo(body)
                .map((response: Response) => {
                    if (response.status === 204) {
                        return undefined;
                    } else if (c) {
                        return deserialize(c, response.text());
                    } else {
                        return response.json();
                    }
                });
        }




    /**
     * Service to change password of a user
     * 
     * @param c
     * @param userId 
     * @param body 
     */
    changePassword<T extends models.ResponseDTO>(userId: number, body?: string, c?: ClassType<T>): Observable<T> {
        // noinspection TypeScriptValidateTypes
        return this.changePasswordWithHttpInfo(userId, body)
                .map((response: Response) => {
                    if (response.status === 204) {
                        return undefined;
                    } else if (c) {
                        return deserialize(c, response.text());
                    } else {
                        return response.json();
                    }
                });
        }




    /**
     * Send forgot password mail with a link to reset password
     * 
     * @param c
     * @param body 
     */
    forgotPassword<T extends models.ResponseDTO>(body?: string, c?: ClassType<T>): Observable<T> {
        // noinspection TypeScriptValidateTypes
        return this.forgotPasswordWithHttpInfo(body)
                .map((response: Response) => {
                    if (response.status === 204) {
                        return undefined;
                    } else if (c) {
                        return deserialize(c, response.text());
                    } else {
                        return response.json();
                    }
                });
        }




    /**
     * Service to do Login with a user email and SHA512 password
     * 
     * @param c
     * @param body 
     */
    login<T extends models.ResponseDTO>(body?: models.UserDTO, c?: ClassType<T>): Observable<T> {
        // noinspection TypeScriptValidateTypes
        return this.loginWithHttpInfo(body)
                .map((response: Response) => {
                    if (response.status === 204) {
                        return undefined;
                    } else if (c) {
                        return deserialize(c, response.text());
                    } else {
                        return response.json();
                    }
                });
        }




    /**
     * Service to logout
     * 
     * @param c
     * @param body 
     */
    logout<T extends models.ResponseDTO>(body?: string, c?: ClassType<T>): Observable<T> {
        // noinspection TypeScriptValidateTypes
        return this.logoutWithHttpInfo(body)
                .map((response: Response) => {
                    if (response.status === 204) {
                        return undefined;
                    } else if (c) {
                        return deserialize(c, response.text());
                    } else {
                        return response.json();
                    }
                });
        }




    /**
     * Service to refresh the Authentication token
     * 
     * @param c
     * @param body 
     */
    refreshToken<T extends models.ResponseDTO>(body?: string, c?: ClassType<T>): Observable<T> {
        // noinspection TypeScriptValidateTypes
        return this.refreshTokenWithHttpInfo(body)
                .map((response: Response) => {
                    if (response.status === 204) {
                        return undefined;
                    } else if (c) {
                        return deserialize(c, response.text());
                    } else {
                        return response.json();
                    }
                });
        }




    /**
     * Service to resend email with a link to activate account
     * 
     * @param c
     * @param body 
     */
    resendEmail<T extends models.ResponseDTO>(body?: models.BeneficiaryDTO, c?: ClassType<T>): Observable<T> {
        // noinspection TypeScriptValidateTypes
        return this.resendEmailWithHttpInfo(body)
                .map((response: Response) => {
                    if (response.status === 204) {
                        return undefined;
                    } else if (c) {
                        return deserialize(c, response.text());
                    } else {
                        return response.json();
                    }
                });
        }



    /**
     * Activate account or reset password
     * 
     * @param body 
     */
    private activateAccountWithHttpInfo(body?: models.ActivateAccountDTO ): Observable<Response> {
        const path = this.basePath + `/user/activateaccount`;


        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845


        headers.set('Content-Type', 'application/json');


        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : /*JSON.stringify*/classToPlain(body), // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            responseType: ResponseContentType.Json
        });

        return this.http.request(path, requestOptions);
    }

    /**
     * Service to change password of a user
     * 
     * @param userId 
     * @param body 
     */
    private changePasswordWithHttpInfo(userId: number,  body?: string ): Observable<Response> {
        const path = this.basePath + `/user/changePassword/${userId}`;
//        .replace('{' + 'userId' + '}', String(userId));  
// not needed as long as the Angular2Typescript language generates the path as TypeScript template string 
// (https://basarat.gitbooks.io/typescript/content/docs/template-strings.html)

        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'userId' is not null or undefined
        if (userId === null || userId === undefined) {
            throw new Error('Required parameter userId was null or undefined when calling changePassword.');
        }


        headers.set('Content-Type', 'application/json');


        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : /*JSON.stringify*/classToPlain(body), // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            responseType: ResponseContentType.Json
        });

        return this.http.request(path, requestOptions);
    }

    /**
     * Send forgot password mail with a link to reset password
     * 
     * @param body 
     */
    private forgotPasswordWithHttpInfo(body?: string ): Observable<Response> {
        const path = this.basePath + `/user/forgotpassword`;


        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845


        headers.set('Content-Type', 'application/json');


        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : /*JSON.stringify*/classToPlain(body), // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            responseType: ResponseContentType.Json
        });

        return this.http.request(path, requestOptions);
    }

    /**
     * Service to do Login with a user email and SHA512 password
     * 
     * @param body 
     */
    private loginWithHttpInfo(body?: models.UserDTO ): Observable<Response> {
        const path = this.basePath + `/user/login`;


        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845


        headers.set('Content-Type', 'application/json');


        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : /*JSON.stringify*/classToPlain(body), // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            responseType: ResponseContentType.Json
        });

        return this.http.request(path, requestOptions);
    }

    /**
     * Service to logout
     * 
     * @param body 
     */
    private logoutWithHttpInfo(body?: string ): Observable<Response> {
        const path = this.basePath + `/user/logout`;


        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845


        headers.set('Content-Type', 'application/json');


        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : /*JSON.stringify*/classToPlain(body), // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            responseType: ResponseContentType.Json
        });

        return this.http.request(path, requestOptions);
    }

    /**
     * Service to refresh the Authentication token
     * 
     * @param body 
     */
    private refreshTokenWithHttpInfo(body?: string ): Observable<Response> {
        const path = this.basePath + `/user/refresh`;


        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845


        headers.set('Content-Type', 'application/json');


        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : /*JSON.stringify*/classToPlain(body), // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            responseType: ResponseContentType.Json
        });

        return this.http.request(path, requestOptions);
    }

    /**
     * Service to resend email with a link to activate account
     * 
     * @param body 
     */
    private resendEmailWithHttpInfo(body?: models.BeneficiaryDTO ): Observable<Response> {
        const path = this.basePath + `/user/resendemail`;


        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845


        headers.set('Content-Type', 'application/json');


        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : /*JSON.stringify*/classToPlain(body), // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            responseType: ResponseContentType.Json
        });

        return this.http.request(path, requestOptions);
    }

}
