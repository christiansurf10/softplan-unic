import { ITrajectory } from 'app/shared/model/trajectory.model';

export interface IRoadType {
    id?: number;
    name?: string;
    cost?: number;
    unit?: string;
    trajectories?: ITrajectory[];
}

export class RoadType implements IRoadType {
    constructor(
        public id?: number,
        public name?: string,
        public cost?: number,
        public unit?: string,
        public trajectories?: ITrajectory[]
    ) {}
}
