export interface SubscriptionDto{
    id: number;
    serviceId: number;
    userId: number;
    startDate: Date;
    endDate: Date;
    totalCost: number;
    isActive: boolean;
    subscriptionDuration: number;
}