import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  slideImg: string = '../../../../assets/images/mobile.png';
  images: string[] = [
    '../../../../assets/images/mobile.png',
    '../../../../assets/images/landline.png',
    '../../../../assets/images/tv.png',
    '../../../../assets/images/internet.png',
  ];
  headers1: string[] = [
    'Mobile Services',
    'Reliable Fixed Telephony',
    'Digital Television',
    'High-Speed Internet',
  ];
  headers2: string[] = [
    'Stay connected anywhere with the best mobile plans tailored to your needs. Whether you are browsing, calling, or texting, enjoy seamless connectivity and competitive rates to stay in touch with the world.',
    'Experience stable, high-quality communication designed for both home and office. With reliable fixed-line services, enjoy uninterrupted calls with crystal-clear voice quality that meets your personal or business requirements.',
    'Unlock the best selection of channels and entertainment for the whole family. Enjoy movies, sports, news, and kids channels in stunning quality, plus exclusive content and features that make TV viewing a true pleasure.',
    'Surf, stream, and download without limits with our fast, reliable internet plans. With high-speed connectivity, access everything you need online quickly, from browsing and streaming to online gaming, with peace of mind.',
  ];
  header1: string = 'Mobile Services';
  header2: string = 'Stay connected anywhere with the best mobile plans tailored to your needs. Whether you are browsing, calling, or texting, enjoy seamless connectivity and competitive rates to stay in touch with the world.';
  currentIndex: number = 0;
  isFading: boolean = false;

  ngOnInit() {
    window.scrollTo(0, 0);
    this.startSlider();
  }


  startSlider() {
    setInterval(() => {
      this.isFading = true;
      setTimeout(() => {
        this.currentIndex = (this.currentIndex + 1) % this.images.length;
        this.slideImg = this.images[this.currentIndex];
        this.header1 = this.headers1[this.currentIndex];
        this.header2 = this.headers2[this.currentIndex];
        this.isFading = false;
      }, 1500);
    }, 6000);
  }

  setSlide(index: number) {
    this.currentIndex = index;
    this.slideImg = this.images[this.currentIndex];
    this.header1 = this.headers1[this.currentIndex];
    this.header2 = this.headers2[this.currentIndex];
  }
}