/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SoftplanTestModule } from '../../../test.module';
import { AdjustmentFactorDeleteDialogComponent } from 'app/entities/adjustment-factor/adjustment-factor-delete-dialog.component';
import { AdjustmentFactorService } from 'app/entities/adjustment-factor/adjustment-factor.service';

describe('Component Tests', () => {
    describe('AdjustmentFactor Management Delete Component', () => {
        let comp: AdjustmentFactorDeleteDialogComponent;
        let fixture: ComponentFixture<AdjustmentFactorDeleteDialogComponent>;
        let service: AdjustmentFactorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [AdjustmentFactorDeleteDialogComponent]
            })
                .overrideTemplate(AdjustmentFactorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AdjustmentFactorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdjustmentFactorService);
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
