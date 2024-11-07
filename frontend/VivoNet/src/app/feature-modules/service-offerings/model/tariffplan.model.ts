export interface TarrifPlan{
    id?: number;
    name: string;
    description: string;
    price: number;
    durationContract: number;
    internetGb? : number;
    numberMinute? : number;
    numberSMS? : number;
    numberChannel? :number;
    HDChannel? : boolean;
    speedInternetMbps? :number
}
