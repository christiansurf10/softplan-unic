/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { AdjustmentFactorDetailComponent } from 'app/entities/adjustment-factor/adjustment-factor-detail.component';
import { AdjustmentFactor } from 'app/shared/model/adjustment-factor.model';

describe('Component Tests', () => {
    describe('AdjustmentFactor Management Detail Component', () => {
        let comp: AdjustmentFactorDetailComponent;
        let fixture: ComponentFixture<AdjustmentFactorDetailComponent>;
        const route = ({ data: of({ adjustmentFactor: new AdjustmentFactor(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [AdjustmentFactorDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AdjustmentFactorDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AdjustmentFactorDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.adjustmentFactor).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
