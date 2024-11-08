import { Injectable } from '@angular/core';
import { Service, TypeService, TypeUser } from './model/services.model';

@Injectable({
  providedIn: 'root'
})
export class ServiceOfferingsService {

  constructor() { }

  getMobileServices(): Service[] {
    // Mock podaci
    const mobileServices: Service[] = [
      // Personal plans
      {
        id: 1,
        name: 'Mobile Basic Plan',
        description: 'Basic mobile plan with limited calls and data.',
        typeUser: TypeUser.Personal,
        typeService: TypeService.Mobile,
        tariffPlans: [
          {
            id: 1,
            name: 'Basic Data Plan',
            description: 'Provides 2GB of data and 100 minutes of calls.',
            price: 10,
            durationContract: 12,
            internetGb: 2,
            numberMinute: 100
          },
          {
            id: 2,
            name: 'Advanced Data Plan',
            description: 'Provides 5GB of data, 300 minutes of calls, and 100 SMS.',
            price: 20,
            durationContract: 12,
            internetGb: 5,
            numberMinute: 300,
            numberSMS: 100
          },
          {
            id: 9,
            name: 'Advanced Data Plan',
            description: 'Provides 5GB of data, 300 minutes of calls, and 100 SMS.',
            price: 20,
            durationContract: 12,
            internetGb: 5,
            numberMinute: 300,
            numberSMS: 100
          }
        ]
      },
      {
        id: 2,
        name: 'Mobile Premium Plan',
        description: 'Premium mobile plan with unlimited calls and high-speed data.',
        typeUser: TypeUser.Personal,
        typeService: TypeService.Mobile,
        tariffPlans: [
          {
            id: 3,
            name: 'Unlimited Plan',
            description: 'Unlimited calls and 10GB of high-speed data.',
            price: 40,
            durationContract: 24,
            internetGb: 10,
            numberMinute: -1 // -1 za neograničene minute
          },
          {
            id: 4,
            name: 'Super Plan',
            description: 'Unlimited calls, 15GB of data, and 500 SMS.',
            price: 50,
            durationContract: 24,
            internetGb: 15,
            numberMinute: -1,  // Neograničene minute
            numberSMS: 500
          }
        ]
      },

      // Business plans
      {
        id: 3,
        name: 'Business Mobile Plan',
        description: 'Mobile plan designed for businesses with flexible data and call options.',
        typeUser: TypeUser.Business,
        typeService: TypeService.Mobile,
        tariffPlans: [
          {
            id: 5,
            name: 'Business Starter Plan',
            description: '5GB of data, 500 minutes of calls, and 500 SMS for businesses.',
            price: 30,
            durationContract: 12,
            internetGb: 5,
            numberMinute: 500,
            numberSMS: 500
          },
          {
            id: 6,
            name: 'Business Unlimited Plan',
            description: 'Unlimited calls and 20GB of data with priority customer support.',
            price: 75,
            durationContract: 24,
            internetGb: 20,
            numberMinute: -1,  // Neograničene minute
            numberSMS: 1000
          }
        ]
      },
      {
        id: 4,
        name: 'Enterprise Mobile Plan',
        description: 'Comprehensive mobile solution for large enterprises, including shared data and calls.',
        typeUser: TypeUser.Business,
        typeService: TypeService.Mobile,
        tariffPlans: [
          {
            id: 7,
            name: 'Enterprise Plan 50GB',
            description: '50GB of shared data, unlimited calls, and 5000 SMS for employees.',
            price: 150,
            durationContract: 24,
            internetGb: 50,
            numberMinute: -1,  // Neograničene minute
            numberSMS: 5000
          },
          {
            id: 8,
            name: 'Enterprise Premium Plan',
            description: 'Unlimited calls, 100GB of shared data, and 10,000 SMS for large enterprises.',
            price: 250,
            durationContract: 36,
            internetGb: 100,
            numberMinute: -1,  // Neograničene minute
            numberSMS: 10000
          }
        ]
      }
    ];

    return mobileServices;
  }

  getLandlineServices(): Service[] {
    const landlineServices: Service[] = [
      // Personal plans
      {
        id: 5,
        name: 'Landline Basic Plan',
        description: 'Basic landline plan with limited local calls.',
        typeUser: TypeUser.Personal,
        typeService: TypeService.Landline,
        tariffPlans: [
          {
            id: 9,
            name: 'Basic Call Plan',
            description: 'Includes 100 local minutes per month.',
            price: 15,
            durationContract: 12,
            numberMinute: 100
          },
          {
            id: 10,
            name: 'Extended Call Plan',
            description: 'Includes 300 local minutes and 50 national minutes.',
            price: 25,
            durationContract: 12,
            numberMinute: 350
          }
        ]
      },
      {
        id: 6,
        name: 'Landline Premium Plan',
        description: 'Premium landline plan with unlimited local calls and national call discounts.',
        typeUser: TypeUser.Personal,
        typeService: TypeService.Landline,
        tariffPlans: [
          {
            id: 11,
            name: 'Unlimited Local Plan',
            description: 'Unlimited local calls and 100 national minutes.',
            price: 35,
            durationContract: 24,
            numberMinute: -1  // Neograničeni minuti
          },
          {
            id: 12,
            name: 'National Plus Plan',
            description: 'Unlimited local calls, 300 national minutes.',
            price: 50,
            durationContract: 24,
            numberMinute: -1  // Neograničeni minuti
          }
        ]
      },

      // Business plans
      {
        id: 7,
        name: 'Business Landline Plan',
        description: 'Landline plan for small businesses with flexible call packages.',
        typeUser: TypeUser.Business,
        typeService: TypeService.Landline,
        tariffPlans: [
          {
            id: 13,
            name: 'Business Starter Plan',
            description: '200 local minutes and 100 national minutes for small businesses.',
            price: 40,
            durationContract: 12,
            numberMinute: 300
          },
          {
            id: 14,
            name: 'Business Unlimited Plan',
            description: 'Unlimited local and national calls with priority support.',
            price: 80,
            durationContract: 24,
            numberMinute: -1  // Neograničeni minuti
          }
        ]
      },
      {
        id: 8,
        name: 'Enterprise Landline Plan',
        description: 'Comprehensive landline solution for large enterprises with shared minutes.',
        typeUser: TypeUser.Business,
        typeService: TypeService.Landline,
        tariffPlans: [
          {
            id: 15,
            name: 'Enterprise Plan 5000 Minutes',
            description: '5000 shared minutes for national and international calls.',
            price: 200,
            durationContract: 36,
            numberMinute: 5000
          },
          {
            id: 16,
            name: 'Enterprise Premium Plan',
            description: 'Unlimited calls with shared lines and priority support.',
            price: 300,
            durationContract: 36,
            numberMinute: -1  // Neograničeni minuti
          }
        ]
      }
    ];

    return landlineServices;
  }
}
