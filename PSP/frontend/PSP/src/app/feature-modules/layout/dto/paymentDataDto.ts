export interface paymentDataDto {
    paymentId: bigint;
    paymentUrl: string;
    amount: number;
    successUrl: string;
    failedUrl: string;
    errorUrl: string;
}