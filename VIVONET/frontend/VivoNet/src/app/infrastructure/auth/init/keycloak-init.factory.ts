// src/app/infrastructure/auth/init/keycloak-init.factory.ts
import Keycloak from 'keycloak-js';

// Kreiraj Keycloak instancu bez tipova da izbegneš greške
export const keycloak = new (Keycloak as any)({
  url: 'http://localhost:8080',
  realm: 'sep-realm',
  clientId: 'sep-vivonet-frontend',
});

export function initKeycloak(): () => Promise<boolean> {
  return (): Promise<boolean> => {
    return new Promise<boolean>((resolve, reject) => {
      const initResult = keycloak.init({
        onLoad: 'login-required',
        checkLoginIframe: false,
      });

      if (initResult && typeof initResult.then === 'function') {
        initResult
          .then((authenticated: boolean) => {
            console.log('Keycloak authenticated:', authenticated);
            resolve(authenticated);
          })
          .catch((error: any) => {
            console.error('Keycloak initialization error:', error);
            reject(error);
          });
      } else {
        console.error('Keycloak init did not return a Promise');
        reject(new Error('Keycloak initialization failed'));
      }
    });
  };
}

export function getKeycloak(): any {
  return keycloak;
}