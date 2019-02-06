import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'trajectory',
                loadChildren: './trajectory/trajectory.module#SoftplanTrajectoryModule'
            },
            {
                path: 'vehicle',
                loadChildren: './vehicle/vehicle.module#SoftplanVehicleModule'
            },
            {
                path: 'road-type',
                loadChildren: './road-type/road-type.module#SoftplanRoadTypeModule'
            },
            {
                path: 'adjustment-factor',
                loadChildren: './adjustment-factor/adjustment-factor.module#SoftplanAdjustmentFactorModule'
            },
            {
                path: 'trajectory-history',
                loadChildren: './trajectory-history/trajectory-history.module#SoftplanTrajectoryHistoryModule'
            },
            {
                path: 'trajectory',
                loadChildren: './trajectory/trajectory.module#SoftplanTrajectoryModule'
            },
            {
                path: 'road-type',
                loadChildren: './road-type/road-type.module#SoftplanRoadTypeModule'
            },
            {
                path: 'adjustment-factor',
                loadChildren: './adjustment-factor/adjustment-factor.module#SoftplanAdjustmentFactorModule'
            },
            {
                path: 'trajectory',
                loadChildren: './trajectory/trajectory.module#SoftplanTrajectoryModule'
            },
            {
                path: 'vehicle',
                loadChildren: './vehicle/vehicle.module#SoftplanVehicleModule'
            },
            {
                path: 'road-type',
                loadChildren: './road-type/road-type.module#SoftplanRoadTypeModule'
            },
            {
                path: 'adjustment-factor',
                loadChildren: './adjustment-factor/adjustment-factor.module#SoftplanAdjustmentFactorModule'
            },
            {
                path: 'trajectory',
                loadChildren: './trajectory/trajectory.module#SoftplanTrajectoryModule'
            },
            {
                path: 'trajectory',
                loadChildren: './trajectory/trajectory.module#SoftplanTrajectoryModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoftplanEntityModule {}
