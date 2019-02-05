/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoftplanTestModule } from '../../../test.module';
import { RoadTypeDetailComponent } from 'app/entities/road-type/road-type-detail.component';
import { RoadType } from 'app/shared/model/road-type.model';

describe('Component Tests', () => {
    describe('RoadType Management Detail Component', () => {
        let comp: RoadTypeDetailComponent;
        let fixture: ComponentFixture<RoadTypeDetailComponent>;
        const route = ({ data: of({ roadType: new RoadType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [RoadTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoadTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoadTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roadType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
