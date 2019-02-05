import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrajectory } from 'app/shared/model/trajectory.model';

@Component({
    selector: 'jhi-trajectory-detail',
    templateUrl: './trajectory-detail.component.html'
})
export class TrajectoryDetailComponent implements OnInit {
    trajectory: ITrajectory;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ trajectory }) => {
            this.trajectory = trajectory;
        });
    }

    previousState() {
        window.history.back();
    }
}
