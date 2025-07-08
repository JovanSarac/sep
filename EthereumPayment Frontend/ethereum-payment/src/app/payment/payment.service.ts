import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { environment } from "../../env/environment";

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    ethAmount: number = 0;
    constructor(private http: HttpClient) { }

    convertUsdToEth(usdAmount: number): Observable<number> {
        return this.http.get<any>('https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd')
            .pipe(
                map(response => {
                    const ethPrice = response.ethereum.usd;
                    this.ethAmount = usdAmount / ethPrice;
                    console.log(`$${usdAmount} = ${this.ethAmount} ETH`);

                    return this.ethAmount;
                })
            );
    }

    getWalletIds(): Observable<string[]>{
        return this.http.get<string[]>(environment.pspHost + 'psp/requests/sendRequestCrypto');
    }
}
