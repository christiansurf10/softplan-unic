import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SoftplanSharedModule } from 'app/shared';
import {
    RoadTypeComponent,
    RoadTypeDetailComponent,
    RoadTypeUpdateComponent,
    RoadTypeDeletePopupComponent,
    RoadTypeDeleteDialogComponent,
    roadTypeRoute,
    roadTypePopupRoute
} from './';

const ENTITY_STATES = [...roadTypeRoute, ...roadTypePopupRoute];

@NgModule({
    imports: [SoftplanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoadTypeComponent,
        RoadTypeDetailComponent,
        RoadTypeUpdateComponent,
        RoadTypeDeleteDialogComponent,
        RoadTypeDeletePopupComponent
    ],
    entryComponents: [RoadTypeComponent, RoadTypeUpdateComponent, RoadTypeDeleteDialogComponent, RoadTypeDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoftplanRoadTypeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
