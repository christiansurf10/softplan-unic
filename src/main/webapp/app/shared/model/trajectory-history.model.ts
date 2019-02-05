import { Moment } from 'moment';
import { ITrajectory } from 'app/shared/model/trajectory.model';

export interface ITrajectoryHistory {
    id?: number;
    startDate?: Moment;
    endDate?: Moment;
    trajectory?: ITrajectory;
}

export class TrajectoryHistory implements ITrajectoryHistory {
    constructor(public id?: number, public startDate?: Moment, public endDate?: Moment, public trajectory?: ITrajectory) {}
}
