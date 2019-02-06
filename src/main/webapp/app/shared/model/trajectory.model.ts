import { IVehicle } from 'app/shared/model/vehicle.model';
import { IRoadType } from 'app/shared/model/road-type.model';
import { IAdjustmentFactor } from 'app/shared/model/adjustment-factor.model';

export interface ITrajectory {
    id?: number;
    name?: string;
    unitFirstRoad?: number;
    unitSecondRoad?: number;
    loadWeight?: number;
    totalCost?: number;
    vehicle?: IVehicle;
    roadTypes?: IRoadType[];
    adjustementFactors?: IAdjustmentFactor[];
}

export class Trajectory implements ITrajectory {
    constructor(
        public id?: number,
        public name?: string,
        public unitFirstRoad?: number,
        public unitSecondRoad?: number,
        public loadWeight?: number,
        public totalCost?: number,
        public vehicle?: IVehicle,
        public roadTypes?: IRoadType[],
        public adjustementFactors?: IAdjustmentFactor[]
    ) {}
}
