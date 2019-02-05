import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoadType } from 'app/shared/model/road-type.model';
import { RoadTypeService } from './road-type.service';

@Component({
    selector: 'jhi-road-type-delete-dialog',
    templateUrl: './road-type-delete-dialog.component.html'
})
export class RoadTypeDeleteDialogComponent {
    roadType: IRoadType;

    constructor(protected roadTypeService: RoadTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roadTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roadTypeListModification',
                content: 'Deleted an roadType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-road-type-delete-popup',
    template: ''
})
export class RoadTypeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roadType }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoadTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.roadType = roadType;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/road-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/road-type', { outlets: { popup: null } }]);
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
