import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoadType } from 'app/shared/model/road-type.model';

@Component({
    selector: 'jhi-road-type-detail',
    templateUrl: './road-type-detail.component.html'
})
export class RoadTypeDetailComponent implements OnInit {
    roadType: IRoadType;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roadType }) => {
            this.roadType = roadType;
        });
    }

    previousState() {
        window.history.back();
    }
}
