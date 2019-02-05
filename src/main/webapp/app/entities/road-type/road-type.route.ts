import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoadType } from 'app/shared/model/road-type.model';
import { RoadTypeService } from './road-type.service';
import { RoadTypeComponent } from './road-type.component';
import { RoadTypeDetailComponent } from './road-type-detail.component';
import { RoadTypeUpdateComponent } from './road-type-update.component';
import { RoadTypeDeletePopupComponent } from './road-type-delete-dialog.component';
import { IRoadType } from 'app/shared/model/road-type.model';

@Injectable({ providedIn: 'root' })
export class RoadTypeResolve implements Resolve<IRoadType> {
    constructor(private service: RoadTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRoadType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoadType>) => response.ok),
                map((roadType: HttpResponse<RoadType>) => roadType.body)
            );
        }
        return of(new RoadType());
    }
}

export const roadTypeRoute: Routes = [
    {
        path: '',
        component: RoadTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: RoadTypeDetailComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: RoadTypeUpdateComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: RoadTypeUpdateComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roadTypePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: RoadTypeDeletePopupComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
