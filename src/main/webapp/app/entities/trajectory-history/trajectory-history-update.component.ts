import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITrajectoryHistory } from 'app/shared/model/trajectory-history.model';
import { TrajectoryHistoryService } from './trajectory-history.service';
import { ITrajectory } from 'app/shared/model/trajectory.model';
import { TrajectoryService } from 'app/entities/trajectory';

@Component({
    selector: 'jhi-trajectory-history-update',
    templateUrl: './trajectory-history-update.component.html'
})
export class TrajectoryHistoryUpdateComponent implements OnInit {
    trajectoryHistory: ITrajectoryHistory;
    isSaving: boolean;

    trajectories: ITrajectory[];
    startDate: string;
    endDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected trajectoryHistoryService: TrajectoryHistoryService,
        protected trajectoryService: TrajectoryService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ trajectoryHistory }) => {
            this.trajectoryHistory = trajectoryHistory;
            this.startDate = this.trajectoryHistory.startDate != null ? this.trajectoryHistory.startDate.format(DATE_TIME_FORMAT) : null;
            this.endDate = this.trajectoryHistory.endDate != null ? this.trajectoryHistory.endDate.format(DATE_TIME_FORMAT) : null;
        });
        this.trajectoryService
            .query({ filter: 'trajectoryhistory-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<ITrajectory[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITrajectory[]>) => response.body)
            )
            .subscribe(
                (res: ITrajectory[]) => {
                    if (!this.trajectoryHistory.trajectory || !this.trajectoryHistory.trajectory.id) {
                        this.trajectories = res;
                    } else {
                        this.trajectoryService
                            .find(this.trajectoryHistory.trajectory.id)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<ITrajectory>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<ITrajectory>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: ITrajectory) => (this.trajectories = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.trajectoryHistory.startDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        this.trajectoryHistory.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        if (this.trajectoryHistory.id !== undefined) {
            this.subscribeToSaveResponse(this.trajectoryHistoryService.update(this.trajectoryHistory));
        } else {
            this.subscribeToSaveResponse(this.trajectoryHistoryService.create(this.trajectoryHistory));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrajectoryHistory>>) {
        result.subscribe((res: HttpResponse<ITrajectoryHistory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
