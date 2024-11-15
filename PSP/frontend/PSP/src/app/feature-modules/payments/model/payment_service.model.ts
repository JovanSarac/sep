export interface PaymentService {
    id: number;
    name: string;
    description: string;
    monthlyFee: number;
    supportedPaymentMethods: string[];
    active: boolean;
}
  