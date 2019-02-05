/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SoftplanTestModule } from '../../../test.module';
import { RoadTypeComponent } from 'app/entities/road-type/road-type.component';
import { RoadTypeService } from 'app/entities/road-type/road-type.service';
import { RoadType } from 'app/shared/model/road-type.model';

describe('Component Tests', () => {
    describe('RoadType Management Component', () => {
        let comp: RoadTypeComponent;
        let fixture: ComponentFixture<RoadTypeComponent>;
        let service: RoadTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SoftplanTestModule],
                declarations: [RoadTypeComponent],
                providers: []
            })
                .overrideTemplate(RoadTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoadTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoadTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new RoadType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.roadTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
