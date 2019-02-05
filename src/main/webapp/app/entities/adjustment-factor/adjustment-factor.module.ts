import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SoftplanSharedModule } from 'app/shared';
import {
    AdjustmentFactorComponent,
    AdjustmentFactorDetailComponent,
    AdjustmentFactorUpdateComponent,
    AdjustmentFactorDeletePopupComponent,
    AdjustmentFactorDeleteDialogComponent,
    adjustmentFactorRoute,
    adjustmentFactorPopupRoute
} from './';

const ENTITY_STATES = [...adjustmentFactorRoute, ...adjustmentFactorPopupRoute];

@NgModule({
    imports: [SoftplanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AdjustmentFactorComponent,
        AdjustmentFactorDetailComponent,
        AdjustmentFactorUpdateComponent,
        AdjustmentFactorDeleteDialogComponent,
        AdjustmentFactorDeletePopupComponent
    ],
    entryComponents: [
        AdjustmentFactorComponent,
        AdjustmentFactorUpdateComponent,
        AdjustmentFactorDeleteDialogComponent,
        AdjustmentFactorDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoftplanAdjustmentFactorModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
