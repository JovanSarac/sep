import { TypeService, TypeUser } from "./services.model";

export interface CartItem {
    id?: number;
    name: string;
    description: string;
    price: number;
    durationContract: number;
    internetGb?: number;
    numberMinute?: number;
    numberSMS?: number;
    numberChannel?: number;
    HDChannel?: boolean;
    speedInternetMbps?: number;
    typeUser: TypeUser;
    typeService: TypeService;
    mobileServiceId: number;
}
