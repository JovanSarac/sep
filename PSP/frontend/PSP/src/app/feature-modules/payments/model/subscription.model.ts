import { PaymentService } from "./payment_service.model";

export interface SubscriptionDto{
    id: number;
    service: PaymentService;
    userId: number;
    startDate: Date;
    endDate: Date;
    totalCost: number;
    isActive: boolean;
    subscriptionDuration: number;
    merchantId?: string;
    merchantPassword?: string;
    paymenterviceId?: number;
}