import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRoadType } from 'app/shared/model/road-type.model';
import { AccountService } from 'app/core';
import { RoadTypeService } from './road-type.service';

@Component({
    selector: 'jhi-road-type',
    templateUrl: './road-type.component.html'
})
export class RoadTypeComponent implements OnInit, OnDestroy {
    roadTypes: IRoadType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected roadTypeService: RoadTypeService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.roadTypeService
            .query()
            .pipe(
                filter((res: HttpResponse<IRoadType[]>) => res.ok),
                map((res: HttpResponse<IRoadType[]>) => res.body)
            )
            .subscribe(
                (res: IRoadType[]) => {
                    this.roadTypes = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRoadTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRoadType) {
        return item.id;
    }

    registerChangeInRoadTypes() {
        this.eventSubscriber = this.eventManager.subscribe('roadTypeListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
