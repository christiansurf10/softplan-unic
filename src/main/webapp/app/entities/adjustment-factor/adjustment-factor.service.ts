import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';

type EntityResponseType = HttpResponse<IAdjustmentFactor>;
type EntityArrayResponseType = HttpResponse<IAdjustmentFactor[]>;

@Injectable({ providedIn: 'root' })
export class AdjustmentFactorService {
    public resourceUrl = SERVER_API_URL + 'api/adjustment-factors';

    constructor(protected http: HttpClient) {}

    create(adjustmentFactor: IAdjustmentFactor): Observable<EntityResponseType> {
        return this.http.post<IAdjustmentFactor>(this.resourceUrl, adjustmentFactor, { observe: 'response' });
    }

    update(adjustmentFactor: IAdjustmentFactor): Observable<EntityResponseType> {
        return this.http.put<IAdjustmentFactor>(this.resourceUrl, adjustmentFactor, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAdjustmentFactor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAdjustmentFactor[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
