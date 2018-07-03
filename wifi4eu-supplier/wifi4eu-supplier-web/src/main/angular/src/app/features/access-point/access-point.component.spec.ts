/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { CoreModule } from '../../core/core.module';
import { AccessPointComponent } from './access-point.component';

describe('AccessPointComponent', () => {
    let component: AccessPointComponent;
    let fixture: ComponentFixture<AccessPointComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                AccessPointComponent
            ],
            imports: [
                CoreModule,
            ],
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AccessPointComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
