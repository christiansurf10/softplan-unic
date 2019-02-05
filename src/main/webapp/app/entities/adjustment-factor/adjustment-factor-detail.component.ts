import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';

@Component({
    selector: 'jhi-adjustment-factor-detail',
    templateUrl: './adjustment-factor-detail.component.html'
})
export class AdjustmentFactorDetailComponent implements OnInit {
    adjustmentFactor: IAdjustmentFactor;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ adjustmentFactor }) => {
            this.adjustmentFactor = adjustmentFactor;
        });
    }

    previousState() {
        window.history.back();
    }
}
