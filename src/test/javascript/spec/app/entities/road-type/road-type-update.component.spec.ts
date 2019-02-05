/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { RoadTypeUpdateComponent } from 'app/entities/road-type/road-type-update.component';
import { RoadTypeService } from 'app/entities/road-type/road-type.service';
import { RoadType } from 'app/shared/model/road-type.model';

describe('Component Tests', () => {
    describe('RoadType Management Update Component', () => {
        let comp: RoadTypeUpdateComponent;
        let fixture: ComponentFixture<RoadTypeUpdateComponent>;
        let service: RoadTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [RoadTypeUpdateComponent]
            })
                .overrideTemplate(RoadTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoadTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoadTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoadType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roadType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoadType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roadType = entity;
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
