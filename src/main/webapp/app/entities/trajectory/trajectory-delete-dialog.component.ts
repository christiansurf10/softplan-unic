import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrajectory } from 'app/shared/model/trajectory.model';
import { TrajectoryService } from './trajectory.service';

@Component({
    selector: 'jhi-trajectory-delete-dialog',
    templateUrl: './trajectory-delete-dialog.component.html'
})
export class TrajectoryDeleteDialogComponent {
    trajectory: ITrajectory;

    constructor(
        protected trajectoryService: TrajectoryService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.trajectoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'trajectoryListModification',
                content: 'Deleted an trajectory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-trajectory-delete-popup',
    template: ''
})
export class TrajectoryDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ trajectory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TrajectoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.trajectory = trajectory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/trajectory', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/trajectory', { outlets: { popup: null } }]);
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
