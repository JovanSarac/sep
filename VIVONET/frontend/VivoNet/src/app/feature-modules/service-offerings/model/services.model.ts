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
    Personal,
    Business
}

export enum TypeService{
    Mobile,
    Landline,
    Internet,
    TV
}