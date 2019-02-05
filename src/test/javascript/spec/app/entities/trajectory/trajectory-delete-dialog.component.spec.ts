/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SoftplanTestModule } from '../../../test.module';
import { TrajectoryDeleteDialogComponent } from 'app/entities/trajectory/trajectory-delete-dialog.component';
import { TrajectoryService } from 'app/entities/trajectory/trajectory.service';

describe('Component Tests', () => {
    describe('Trajectory Management Delete Component', () => {
        let comp: TrajectoryDeleteDialogComponent;
        let fixture: ComponentFixture<TrajectoryDeleteDialogComponent>;
        let service: TrajectoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [TrajectoryDeleteDialogComponent]
            })
                .overrideTemplate(TrajectoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TrajectoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrajectoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
