import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import Web3 from 'web3';
import { PaymentService } from './payment.service';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule, MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent {
  walletIds: string[] = [];
  selectedWallet: string = "";
  itemNames: string[] = [];
  amountUSD: number = 0;
  amountETH: number = 0;
  web3: Web3 | undefined;
  senderAccount: string = '';

  constructor(private route: ActivatedRoute,
    private paymentService: PaymentService) {
    if (typeof (window as any).ethereum !== 'undefined') {
      this.web3 = new Web3((window as any).ethereum);
    }

    this.enableMetaMask();
    this.getSenderAccount();
  }

  async enableMetaMask(): Promise<void> {
    if (typeof window.ethereum !== 'undefined') {
      console.log('MetaMask is installed!');
    } else {
      console.log('MetaMask is not installed!');
      return;
    }

    try {
      await window.ethereum.request({ method: 'eth_requestAccounts' });
      console.log('MetaMask account access granted');
    } catch (error) {
      console.error('User denied account access');
    }
  }

  async getSenderAccount(): Promise<void>{
    if (!this.web3) {
      console.error("Web3 not initialized");
      return;
    }

    const acounts = await this.web3.eth.getAccounts();
    this.senderAccount = this.maskAddress(acounts[0]);
  }

  maskAddress(address: string): string {
    if (!address || address.length < 10) return address;
    return address.substring(0, 4) + '...' + address.substring(address.length - 4);
  }

  async sendEth(to: string): Promise<void> {
    if (!this.web3) {
      console.error("Web3 not initialized");
      return;
    }

    try {
      const acounts = await this.web3.eth.getAccounts();
      const from = acounts[0];
      const value = this.web3.utils.toWei(this.amountETH, 'ether');

      const response = await this.web3.eth.sendTransaction({
        from,
        to,
        value
      })

      console.log(response);
      // You can replace these with Angular Material snackbars or other UI notifications:
      alert(`Transaction successful: ${response.transactionHash}`);
    } catch (error: any) {
      console.error(error);
      alert(`Transaction failed: ${error.message || error}`);
    }
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const itemNamesRaw = params.get('itemNames');
      const amountRaw = params.get('amount');

      this.itemNames = itemNamesRaw ? itemNamesRaw.split(',') : [];
      this.amountUSD = amountRaw ? parseFloat(amountRaw) : 0;
      this.paymentService.convertUsdToEth(this.amountUSD).subscribe({
        next: result => this.amountETH = result,
        error: (error: any) => console.log(error),
        complete: (): any => {}
      });
    })

    this.paymentService.getWalletIds().subscribe({
      next: (result) => { this.walletIds = result},
      error: (error: any) => console.log(error),
      complete: ():any => {}
    })

  }

  onSubmit() {
    this.sendEth('destination-address')
  }
}
