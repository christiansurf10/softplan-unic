import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Trajectory } from 'app/shared/model/trajectory.model';
import { TrajectoryService } from './trajectory.service';
import { TrajectoryComponent } from './trajectory.component';
import { TrajectoryDetailComponent } from './trajectory-detail.component';
import { TrajectoryUpdateComponent } from './trajectory-update.component';
import { TrajectoryDeletePopupComponent } from './trajectory-delete-dialog.component';
import { ITrajectory } from 'app/shared/model/trajectory.model';

@Injectable({ providedIn: 'root' })
export class TrajectoryResolve implements Resolve<ITrajectory> {
    constructor(private service: TrajectoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITrajectory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Trajectory>) => response.ok),
                map((trajectory: HttpResponse<Trajectory>) => trajectory.body)
            );
        }
        return of(new Trajectory());
    }
}

export const trajectoryRoute: Routes = [
    {
        path: '',
        component: TrajectoryComponent,
        data: {
            authorities: [],
            pageTitle: 'softplanApp.trajectory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: TrajectoryDetailComponent,
        resolve: {
            trajectory: TrajectoryResolve
        },
        data: {
            authorities: [],
            pageTitle: 'softplanApp.trajectory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: TrajectoryUpdateComponent,
        resolve: {
            trajectory: TrajectoryResolve
        },
        data: {
            authorities: [],
            pageTitle: 'softplanApp.trajectory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: TrajectoryUpdateComponent,
        resolve: {
            trajectory: TrajectoryResolve
        },
        data: {
            authorities: [],
            pageTitle: 'softplanApp.trajectory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const trajectoryPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: TrajectoryDeletePopupComponent,
        resolve: {
            trajectory: TrajectoryResolve
        },
        data: {
            authorities: [],
            pageTitle: 'softplanApp.trajectory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
