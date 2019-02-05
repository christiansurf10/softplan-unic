/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { TrajectoryHistoryDetailComponent } from 'app/entities/trajectory-history/trajectory-history-detail.component';
import { TrajectoryHistory } from 'app/shared/model/trajectory-history.model';

describe('Component Tests', () => {
    describe('TrajectoryHistory Management Detail Component', () => {
        let comp: TrajectoryHistoryDetailComponent;
        let fixture: ComponentFixture<TrajectoryHistoryDetailComponent>;
        const route = ({ data: of({ trajectoryHistory: new TrajectoryHistory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [TrajectoryHistoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TrajectoryHistoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TrajectoryHistoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.trajectoryHistory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
