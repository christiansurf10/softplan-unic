/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { TrajectoryUpdateComponent } from 'app/entities/trajectory/trajectory-update.component';
import { TrajectoryService } from 'app/entities/trajectory/trajectory.service';
import { Trajectory } from 'app/shared/model/trajectory.model';

describe('Component Tests', () => {
    describe('Trajectory Management Update Component', () => {
        let comp: TrajectoryUpdateComponent;
        let fixture: ComponentFixture<TrajectoryUpdateComponent>;
        let service: TrajectoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [TrajectoryUpdateComponent]
            })
                .overrideTemplate(TrajectoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TrajectoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrajectoryService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Trajectory(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.trajectory = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Trajectory();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.trajectory = entity;
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
