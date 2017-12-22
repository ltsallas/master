import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {Http, XHRBackend, RequestOptions, HttpModule} from "@angular/http";
import {UxHttp} from "@ec-digit-uxatec/eui-angular2-ux-http-interceptor";
import {UxModule, UX_DIRECTIVES} from "@ec-digit-uxatec/eui-angular2-ux-commons";
import {UxLanguageSelectorComponent} from "@ec-digit-uxatec/eui-angular2-ux-language-selector";
import {UxSearchInputComponent} from "@ec-digit-uxatec/eui-angular2-ux-search-input";
import {
    CheckboxModule,
    GrowlModule,
    BlockUIModule,
    AutoCompleteModule,
    DialogModule,
    DataTableModule,
    CalendarModule,
    MultiSelectModule,
    TabMenuModule,
    TabViewModule,
    DropdownModule,
    FileUploadModule
} from "primeng/primeng";
// import {APP_DIRECTIVES} from "./components/index";
import {TimelineComponent} from "./components/timeline/timeline.component";
import {Ng2GoogleRecaptchaModule} from "ng2-google-recaptcha";
import {SuccessComponent} from "./components/success/success.component"
import {FailureComponent} from "./components/failure/failure.component";
import {CustomAccordionBoxComponent} from "./components/custom-accordion-box/custom-accordion-box.component";
import {CustomTimelineAccordionBoxComponent} from "./components/timeline/custom-timeline-accordion-box.component";
import {CustomWizardStepsComponent} from "./components/custom-wizard-steps/custom-wizard-steps";
import {HelpdeskFormComponent} from "../shared/components/helpdesk-form/helpdesk-form.component";
import {TimerComponent} from "../shared/components/timer/timer.component";
import {CustomLayoutHeaderComponent} from "./components/custom-layout-header/custom-layout-header";
import {CustomLayoutNavBarActionItemComponent} from "./components/custom-layout-nav-bar-action-item/custom-layout-nav-bar-action-item";

export function httpFactory(backend: XHRBackend, defaultOptions: RequestOptions) {
    return new UxHttp(backend, defaultOptions);
}

@NgModule({
    imports: [
        RouterModule,
        UxModule,
        CheckboxModule,
        GrowlModule,
        BlockUIModule,
        HttpModule,
        AutoCompleteModule,
        DialogModule,
        DataTableModule,
        CalendarModule,
        MultiSelectModule,
        TabMenuModule,
        TabViewModule,
        DropdownModule,
        FileUploadModule,
        Ng2GoogleRecaptchaModule
    ],
    declarations: [
        UX_DIRECTIVES,
        UxLanguageSelectorComponent,
        UxSearchInputComponent,
        SuccessComponent,
        FailureComponent,
        CustomAccordionBoxComponent,
        CustomTimelineAccordionBoxComponent,
        CustomWizardStepsComponent,
        CustomLayoutHeaderComponent,
        CustomLayoutNavBarActionItemComponent,
        TimelineComponent,
        HelpdeskFormComponent,
        TimerComponent
        // APP_DIRECTIVES
    ],
    exports: [
        UX_DIRECTIVES,
        UxLanguageSelectorComponent,
        UxSearchInputComponent,
        UxModule,
        // APP_DIRECTIVES,
        CheckboxModule,
        GrowlModule,
        BlockUIModule,
        HttpModule,
        AutoCompleteModule,
        DialogModule,
        DataTableModule,
        CalendarModule,
        MultiSelectModule,
        TabMenuModule,
        TabViewModule,
        DropdownModule,
        FileUploadModule,
        SuccessComponent,
        FailureComponent,
        CustomAccordionBoxComponent,
        CustomTimelineAccordionBoxComponent,
        CustomWizardStepsComponent,
        CustomLayoutHeaderComponent,
        CustomLayoutNavBarActionItemComponent,
        Ng2GoogleRecaptchaModule,
        TimelineComponent,
        HelpdeskFormComponent,
        TimerComponent
    ],
    providers: [
        {
            provide: Http,
            useFactory: httpFactory,
            deps: [XHRBackend, RequestOptions]
        }
    ]
})
export class SharedModule {
}