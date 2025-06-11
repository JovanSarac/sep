export interface paymentQRDataDto {
    qrData: string;
    paymentId: bigint;
    paymentUrl: string;
    amount: number;
    successUrl: string;
    failedUrl: string;
    errorUrl: string;
    qrPaymentId: string;
}