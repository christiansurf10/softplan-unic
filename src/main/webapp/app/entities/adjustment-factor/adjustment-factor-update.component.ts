import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';
import { AdjustmentFactorService } from './adjustment-factor.service';
import { ITrajectory } from 'app/shared/model/trajectory.model';
import { TrajectoryService } from 'app/entities/trajectory';

@Component({
    selector: 'jhi-adjustment-factor-update',
    templateUrl: './adjustment-factor-update.component.html'
})
export class AdjustmentFactorUpdateComponent implements OnInit {
    adjustmentFactor: IAdjustmentFactor;
    isSaving: boolean;

    trajectories: ITrajectory[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected adjustmentFactorService: AdjustmentFactorService,
        protected trajectoryService: TrajectoryService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ adjustmentFactor }) => {
            this.adjustmentFactor = adjustmentFactor;
        });
        this.trajectoryService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITrajectory[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITrajectory[]>) => response.body)
            )
            .subscribe((res: ITrajectory[]) => (this.trajectories = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.adjustmentFactor.id !== undefined) {
            this.subscribeToSaveResponse(this.adjustmentFactorService.update(this.adjustmentFactor));
        } else {
            this.subscribeToSaveResponse(this.adjustmentFactorService.create(this.adjustmentFactor));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdjustmentFactor>>) {
        result.subscribe((res: HttpResponse<IAdjustmentFactor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackTrajectoryById(index: number, item: ITrajectory) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
