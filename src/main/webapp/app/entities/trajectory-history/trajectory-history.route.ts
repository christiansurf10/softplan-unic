import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TrajectoryHistory } from 'app/shared/model/trajectory-history.model';
import { TrajectoryHistoryService } from './trajectory-history.service';
import { TrajectoryHistoryComponent } from './trajectory-history.component';
import { TrajectoryHistoryDetailComponent } from './trajectory-history-detail.component';
import { TrajectoryHistoryUpdateComponent } from './trajectory-history-update.component';
import { TrajectoryHistoryDeletePopupComponent } from './trajectory-history-delete-dialog.component';
import { ITrajectoryHistory } from 'app/shared/model/trajectory-history.model';

@Injectable({ providedIn: 'root' })
export class TrajectoryHistoryResolve implements Resolve<ITrajectoryHistory> {
    constructor(private service: TrajectoryHistoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITrajectoryHistory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<TrajectoryHistory>) => response.ok),
                map((trajectoryHistory: HttpResponse<TrajectoryHistory>) => trajectoryHistory.body)
            );
        }
        return of(new TrajectoryHistory());
    }
}

export const trajectoryHistoryRoute: Routes = [
    {
        path: '',
        component: TrajectoryHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.trajectoryHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: TrajectoryHistoryDetailComponent,
        resolve: {
            trajectoryHistory: TrajectoryHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.trajectoryHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: TrajectoryHistoryUpdateComponent,
        resolve: {
            trajectoryHistory: TrajectoryHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.trajectoryHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: TrajectoryHistoryUpdateComponent,
        resolve: {
            trajectoryHistory: TrajectoryHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.trajectoryHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const trajectoryHistoryPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: TrajectoryHistoryDeletePopupComponent,
        resolve: {
            trajectoryHistory: TrajectoryHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.trajectoryHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
