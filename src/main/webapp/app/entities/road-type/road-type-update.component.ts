import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRoadType } from 'app/shared/model/road-type.model';
import { RoadTypeService } from './road-type.service';
import { ITrajectory } from 'app/shared/model/trajectory.model';
import { TrajectoryService } from 'app/entities/trajectory';

@Component({
    selector: 'jhi-road-type-update',
    templateUrl: './road-type-update.component.html'
})
export class RoadTypeUpdateComponent implements OnInit {
    roadType: IRoadType;
    isSaving: boolean;

    trajectories: ITrajectory[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roadTypeService: RoadTypeService,
        protected trajectoryService: TrajectoryService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roadType }) => {
            this.roadType = roadType;
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
        if (this.roadType.id !== undefined) {
            this.subscribeToSaveResponse(this.roadTypeService.update(this.roadType));
        } else {
            this.subscribeToSaveResponse(this.roadTypeService.create(this.roadType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoadType>>) {
        result.subscribe((res: HttpResponse<IRoadType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
