export interface IVehicle {
    id?: number;
    name?: string;
    cost?: number;
}

export class Vehicle implements IVehicle {
    constructor(public id?: number, public name?: string, public cost?: number) {}
}
