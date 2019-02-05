/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { AdjustmentFactorUpdateComponent } from 'app/entities/adjustment-factor/adjustment-factor-update.component';
import { AdjustmentFactorService } from 'app/entities/adjustment-factor/adjustment-factor.service';
import { AdjustmentFactor } from 'app/shared/model/adjustment-factor.model';

describe('Component Tests', () => {
    describe('AdjustmentFactor Management Update Component', () => {
        let comp: AdjustmentFactorUpdateComponent;
        let fixture: ComponentFixture<AdjustmentFactorUpdateComponent>;
        let service: AdjustmentFactorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [AdjustmentFactorUpdateComponent]
            })
                .overrideTemplate(AdjustmentFactorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AdjustmentFactorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdjustmentFactorService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new AdjustmentFactor(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.adjustmentFactor = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new AdjustmentFactor();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.adjustmentFactor = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
