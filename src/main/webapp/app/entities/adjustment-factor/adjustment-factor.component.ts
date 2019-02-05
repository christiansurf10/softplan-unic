import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';
import { AccountService } from 'app/core';
import { AdjustmentFactorService } from './adjustment-factor.service';

@Component({
    selector: 'jhi-adjustment-factor',
    templateUrl: './adjustment-factor.component.html'
})
export class AdjustmentFactorComponent implements OnInit, OnDestroy {
    adjustmentFactors: IAdjustmentFactor[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected adjustmentFactorService: AdjustmentFactorService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.adjustmentFactorService
            .query()
            .pipe(
                filter((res: HttpResponse<IAdjustmentFactor[]>) => res.ok),
                map((res: HttpResponse<IAdjustmentFactor[]>) => res.body)
            )
            .subscribe(
                (res: IAdjustmentFactor[]) => {
                    this.adjustmentFactors = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAdjustmentFactors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAdjustmentFactor) {
        return item.id;
    }

    registerChangeInAdjustmentFactors() {
        this.eventSubscriber = this.eventManager.subscribe('adjustmentFactorListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
