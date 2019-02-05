import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SoftplanSharedModule } from 'app/shared';
import {
    TrajectoryHistoryComponent,
    TrajectoryHistoryDetailComponent,
    TrajectoryHistoryUpdateComponent,
    TrajectoryHistoryDeletePopupComponent,
    TrajectoryHistoryDeleteDialogComponent,
    trajectoryHistoryRoute,
    trajectoryHistoryPopupRoute
} from './';

const ENTITY_STATES = [...trajectoryHistoryRoute, ...trajectoryHistoryPopupRoute];

@NgModule({
    imports: [SoftplanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TrajectoryHistoryComponent,
        TrajectoryHistoryDetailComponent,
        TrajectoryHistoryUpdateComponent,
        TrajectoryHistoryDeleteDialogComponent,
        TrajectoryHistoryDeletePopupComponent
    ],
    entryComponents: [
        TrajectoryHistoryComponent,
        TrajectoryHistoryUpdateComponent,
        TrajectoryHistoryDeleteDialogComponent,
        TrajectoryHistoryDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoftplanTrajectoryHistoryModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
