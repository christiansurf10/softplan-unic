import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITrajectory } from 'app/shared/model/trajectory.model';
import { TrajectoryService } from './trajectory.service';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { VehicleService } from 'app/entities/vehicle';
import { IRoadType } from 'app/shared/model/road-type.model';
import { RoadTypeService } from 'app/entities/road-type';
import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';
import { AdjustmentFactorService } from 'app/entities/adjustment-factor';

@Component({
    selector: 'jhi-trajectory-update',
    templateUrl: './trajectory-update.component.html'
})
export class TrajectoryUpdateComponent implements OnInit {
    trajectory: ITrajectory;
    isSaving: boolean;

    vehicles: IVehicle[];

    roadtypes: IRoadType[];

    adjustmentfactors: IAdjustmentFactor[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected trajectoryService: TrajectoryService,
        protected vehicleService: VehicleService,
        protected roadTypeService: RoadTypeService,
        protected adjustmentFactorService: AdjustmentFactorService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ trajectory }) => {
            this.trajectory = trajectory;
        });
        this.vehicleService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IVehicle[]>) => mayBeOk.ok),
                map((response: HttpResponse<IVehicle[]>) => response.body)
            )
            .subscribe((res: IVehicle[]) => (this.vehicles = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.roadTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoadType[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoadType[]>) => response.body)
            )
            .subscribe((res: IRoadType[]) => (this.roadtypes = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.adjustmentFactorService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAdjustmentFactor[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAdjustmentFactor[]>) => response.body)
            )
            .subscribe((res: IAdjustmentFactor[]) => (this.adjustmentfactors = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.trajectory.id !== undefined) {
            this.subscribeToSaveResponse(this.trajectoryService.update(this.trajectory));
        } else {
            this.subscribeToSaveResponse(this.trajectoryService.create(this.trajectory));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrajectory>>) {
        result.subscribe((res: HttpResponse<ITrajectory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackVehicleById(index: number, item: IVehicle) {
        return item.id;
    }

    trackRoadTypeById(index: number, item: IRoadType) {
        return item.id;
    }

    trackAdjustmentFactorById(index: number, item: IAdjustmentFactor) {
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
