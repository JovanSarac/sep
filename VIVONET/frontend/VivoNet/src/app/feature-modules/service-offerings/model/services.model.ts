import { TarrifPlan } from "./tariffplan.model";

export interface Service{
    id?: number;
    name: string;
    description: string;
    typeUser: TypeUser;
    typeService: TypeService;
    tariffPlans: TarrifPlan[]
}

export enum TypeUser{
    PERSONAL,
    BUSINESS
}

export enum TypeService{
    MOBILE,
    LANDLINE,
    INTERNET,
    TV
}