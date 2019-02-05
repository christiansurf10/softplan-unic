/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { TrajectoryDetailComponent } from 'app/entities/trajectory/trajectory-detail.component';
import { Trajectory } from 'app/shared/model/trajectory.model';

describe('Component Tests', () => {
    describe('Trajectory Management Detail Component', () => {
        let comp: TrajectoryDetailComponent;
        let fixture: ComponentFixture<TrajectoryDetailComponent>;
        const route = ({ data: of({ trajectory: new Trajectory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [TrajectoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TrajectoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TrajectoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.trajectory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
