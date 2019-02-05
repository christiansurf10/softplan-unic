/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SoftplanTestModule } from '../../../test.module';
import { AdjustmentFactorComponent } from 'app/entities/adjustment-factor/adjustment-factor.component';
import { AdjustmentFactorService } from 'app/entities/adjustment-factor/adjustment-factor.service';
import { AdjustmentFactor } from 'app/shared/model/adjustment-factor.model';

describe('Component Tests', () => {
    describe('AdjustmentFactor Management Component', () => {
        let comp: AdjustmentFactorComponent;
        let fixture: ComponentFixture<AdjustmentFactorComponent>;
        let service: AdjustmentFactorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [AdjustmentFactorComponent],
                providers: []
            })
                .overrideTemplate(AdjustmentFactorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AdjustmentFactorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdjustmentFactorService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new AdjustmentFactor(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.adjustmentFactors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
