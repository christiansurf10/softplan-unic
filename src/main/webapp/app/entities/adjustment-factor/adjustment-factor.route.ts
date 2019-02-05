import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AdjustmentFactor } from 'app/shared/model/adjustment-factor.model';
import { AdjustmentFactorService } from './adjustment-factor.service';
import { AdjustmentFactorComponent } from './adjustment-factor.component';
import { AdjustmentFactorDetailComponent } from './adjustment-factor-detail.component';
import { AdjustmentFactorUpdateComponent } from './adjustment-factor-update.component';
import { AdjustmentFactorDeletePopupComponent } from './adjustment-factor-delete-dialog.component';
import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';

@Injectable({ providedIn: 'root' })
export class AdjustmentFactorResolve implements Resolve<IAdjustmentFactor> {
    constructor(private service: AdjustmentFactorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAdjustmentFactor> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<AdjustmentFactor>) => response.ok),
                map((adjustmentFactor: HttpResponse<AdjustmentFactor>) => adjustmentFactor.body)
            );
        }
        return of(new AdjustmentFactor());
    }
}

export const adjustmentFactorRoute: Routes = [
    {
        path: '',
        component: AdjustmentFactorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.adjustmentFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AdjustmentFactorDetailComponent,
        resolve: {
            adjustmentFactor: AdjustmentFactorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.adjustmentFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AdjustmentFactorUpdateComponent,
        resolve: {
            adjustmentFactor: AdjustmentFactorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.adjustmentFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AdjustmentFactorUpdateComponent,
        resolve: {
            adjustmentFactor: AdjustmentFactorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.adjustmentFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const adjustmentFactorPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AdjustmentFactorDeletePopupComponent,
        resolve: {
            adjustmentFactor: AdjustmentFactorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'softplanApp.adjustmentFactor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
