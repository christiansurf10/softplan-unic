import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrajectoryHistory } from 'app/shared/model/trajectory-history.model';
import { TrajectoryHistoryService } from './trajectory-history.service';

@Component({
    selector: 'jhi-trajectory-history-delete-dialog',
    templateUrl: './trajectory-history-delete-dialog.component.html'
})
export class TrajectoryHistoryDeleteDialogComponent {
    trajectoryHistory: ITrajectoryHistory;

    constructor(
        protected trajectoryHistoryService: TrajectoryHistoryService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.trajectoryHistoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'trajectoryHistoryListModification',
                content: 'Deleted an trajectoryHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-trajectory-history-delete-popup',
    template: ''
})
export class TrajectoryHistoryDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ trajectoryHistory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TrajectoryHistoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.trajectoryHistory = trajectoryHistory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/trajectory-history', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/trajectory-history', { outlets: { popup: null } }]);
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
