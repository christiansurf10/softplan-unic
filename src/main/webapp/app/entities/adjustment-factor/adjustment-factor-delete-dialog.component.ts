import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';
import { AdjustmentFactorService } from './adjustment-factor.service';

@Component({
    selector: 'jhi-adjustment-factor-delete-dialog',
    templateUrl: './adjustment-factor-delete-dialog.component.html'
})
export class AdjustmentFactorDeleteDialogComponent {
    adjustmentFactor: IAdjustmentFactor;

    constructor(
        protected adjustmentFactorService: AdjustmentFactorService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.adjustmentFactorService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'adjustmentFactorListModification',
                content: 'Deleted an adjustmentFactor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-adjustment-factor-delete-popup',
    template: ''
})
export class AdjustmentFactorDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ adjustmentFactor }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AdjustmentFactorDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.adjustmentFactor = adjustmentFactor;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/adjustment-factor', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/adjustment-factor', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
