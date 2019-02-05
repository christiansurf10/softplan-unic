import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SoftplanSharedModule } from 'app/shared';
import {
    TrajectoryComponent,
    TrajectoryDetailComponent,
    TrajectoryUpdateComponent,
    TrajectoryDeletePopupComponent,
    TrajectoryDeleteDialogComponent,
    trajectoryRoute,
    trajectoryPopupRoute
} from './';

const ENTITY_STATES = [...trajectoryRoute, ...trajectoryPopupRoute];

@NgModule({
    imports: [SoftplanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TrajectoryComponent,
        TrajectoryDetailComponent,
        TrajectoryUpdateComponent,
        TrajectoryDeleteDialogComponent,
        TrajectoryDeletePopupComponent
    ],
    entryComponents: [TrajectoryComponent, TrajectoryUpdateComponent, TrajectoryDeleteDialogComponent, TrajectoryDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoftplanTrajectoryModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
