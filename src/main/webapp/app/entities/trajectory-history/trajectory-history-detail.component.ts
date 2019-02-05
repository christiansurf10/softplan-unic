import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrajectoryHistory } from 'app/shared/model/trajectory-history.model';

@Component({
    selector: 'jhi-trajectory-history-detail',
    templateUrl: './trajectory-history-detail.component.html'
})
export class TrajectoryHistoryDetailComponent implements OnInit {
    trajectoryHistory: ITrajectoryHistory;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ trajectoryHistory }) => {
            this.trajectoryHistory = trajectoryHistory;
        });
    }

    previousState() {
        window.history.back();
    }
}
