import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoadType } from 'app/shared/model/road-type.model';

type EntityResponseType = HttpResponse<IRoadType>;
type EntityArrayResponseType = HttpResponse<IRoadType[]>;

@Injectable({ providedIn: 'root' })
export class RoadTypeService {
    public resourceUrl = SERVER_API_URL + 'api/road-types';

    constructor(protected http: HttpClient) {}

    create(roadType: IRoadType): Observable<EntityResponseType> {
        return this.http.post<IRoadType>(this.resourceUrl, roadType, { observe: 'response' });
    }

    update(roadType: IRoadType): Observable<EntityResponseType> {
        return this.http.put<IRoadType>(this.resourceUrl, roadType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRoadType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRoadType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
