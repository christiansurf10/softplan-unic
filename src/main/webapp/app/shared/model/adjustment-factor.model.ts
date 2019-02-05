import { ITrajectory } from 'app/shared/model/trajectory.model';

export interface IAdjustmentFactor {
    id?: number;
    name?: string;
    condition?: string;
    conditionOperator?: string;
    cost?: number;
    trajectories?: ITrajectory[];
}

export class AdjustmentFactor implements IAdjustmentFactor {
    constructor(
        public id?: number,
        public name?: string,
        public condition?: string,
        public conditionOperator?: string,
        public cost?: number,
        public trajectories?: ITrajectory[]
    ) {}
}
