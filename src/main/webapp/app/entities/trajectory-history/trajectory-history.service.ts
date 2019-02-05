import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITrajectoryHistory } from 'app/shared/model/trajectory-history.model';

type EntityResponseType = HttpResponse<ITrajectoryHistory>;
type EntityArrayResponseType = HttpResponse<ITrajectoryHistory[]>;

@Injectable({ providedIn: 'root' })
export class TrajectoryHistoryService {
    public resourceUrl = SERVER_API_URL + 'api/trajectory-histories';

    constructor(protected http: HttpClient) {}

    create(trajectoryHistory: ITrajectoryHistory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(trajectoryHistory);
        return this.http
            .post<ITrajectoryHistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(trajectoryHistory: ITrajectoryHistory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(trajectoryHistory);
        return this.http
            .put<ITrajectoryHistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITrajectoryHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITrajectoryHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(trajectoryHistory: ITrajectoryHistory): ITrajectoryHistory {
        const copy: ITrajectoryHistory = Object.assign({}, trajectoryHistory, {
            startDate:
                trajectoryHistory.startDate != null && trajectoryHistory.startDate.isValid() ? trajectoryHistory.startDate.toJSON() : null,
            endDate: trajectoryHistory.endDate != null && trajectoryHistory.endDate.isValid() ? trajectoryHistory.endDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
            res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((trajectoryHistory: ITrajectoryHistory) => {
                trajectoryHistory.startDate = trajectoryHistory.startDate != null ? moment(trajectoryHistory.startDate) : null;
                trajectoryHistory.endDate = trajectoryHistory.endDate != null ? moment(trajectoryHistory.endDate) : null;
            });
        }
        return res;
    }
}
