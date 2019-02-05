/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { TrajectoryHistoryUpdateComponent } from 'app/entities/trajectory-history/trajectory-history-update.component';
import { TrajectoryHistoryService } from 'app/entities/trajectory-history/trajectory-history.service';
import { TrajectoryHistory } from 'app/shared/model/trajectory-history.model';

describe('Component Tests', () => {
    describe('TrajectoryHistory Management Update Component', () => {
        let comp: TrajectoryHistoryUpdateComponent;
        let fixture: ComponentFixture<TrajectoryHistoryUpdateComponent>;
        let service: TrajectoryHistoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [TrajectoryHistoryUpdateComponent]
            })
                .overrideTemplate(TrajectoryHistoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TrajectoryHistoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrajectoryHistoryService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new TrajectoryHistory(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.trajectoryHistory = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new TrajectoryHistory();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.trajectoryHistory = entity;
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
