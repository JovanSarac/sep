declare module 'qrcode' {
    export type QRCodeErrorCorrectionLevel = 'L' | 'M' | 'Q' | 'H';
    const toDataURL: any;
    export { toDataURL };
  }