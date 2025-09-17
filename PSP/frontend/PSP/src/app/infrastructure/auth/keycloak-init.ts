// import Keycloak from 'keycloak-js'

// const keycloak = new (Keycloak as any)({
//     url: 'http://localhost:8080',
//     realm: 'sep-realm',
//     clientId: 'psp-front-test'
// });

// export function initKeycloak(): () => Promise<boolean> {
//     return () => 
//         // keycloak.init({
//         //     onLoad:'check-sso',
//         //     silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html'
//         // }).then(authenticated => {
//         //     return authenticated;
//         // })
//         keycloak.init({
//             onLoad: 'check-sso',
//             // pkceMethod: 'S256',
//             // checkLoginIframe: false,
//             // redirectUri: window.location.href,
//             silentCheckSsoRedirectUri: window.location.origin + '../../assets/silent-check-sso.html'
//         }).then((authenticated: any) => {
//             if (authenticated) {
//                 console.log("Token:", keycloak.token);
//                 localStorage.setItem("access_token", keycloak.token!);
//                 localStorage.setItem("refresh_token", keycloak.refreshToken!);
//                 setInterval(() => {
//                 keycloak.updateToken(30).then((refreshed: any) => {
//                     if (refreshed) {
//                     console.log("Token refreshed:", keycloak.token);
//                     }
//                 }).catch(() => {
//                     console.error("Failed to refresh token");
//                 });
//                 }, 10000);
//             } else {
//                 console.warn("Not authenticated");
//             }
//             return authenticated;
//         });
// }

// export function getKeyCloak() {return keycloak;}

// import Keycloak from 'keycloak-js';

// const keycloak = new (Keycloak as any)({
//     url: 'http://localhost:8080',
//     realm: 'sep-realm',
//     clientId: 'psp-front-test'
// });

// let isKeycloakInitialized = false;
// let isKeycloakAuthenticated = false;

// export function initKeycloak(): () => Promise<boolean> {
//     return () => 
//         keycloak.init({
//             // onLoad: 'check-sso',
//             // checkLoginIframe: false, // Disable iframe to avoid CORS issues
//             // pkceMethod: 'S256', // Enable PKCE for better security
//             // silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html'
//             onLoad: 'login-required',
//             pkceMethod: 'S256',
//             checkLoginIframe: false,
//             redirectUri: window.location.href
//         }).then((authenticated: boolean) => {
//             isKeycloakInitialized = true;
//             isKeycloakAuthenticated = authenticated;

//             console.log("🔐 Keycloak initialized:", {
//                 authenticated,
//                 token: keycloak.token ? "EXISTS" : "NO TOKEN",
//                 refreshToken: keycloak.refreshToken ? "EXISTS" : "NO REFRESH TOKEN"
//             });
            
//             if (authenticated) {
//                 console.log("Token:", keycloak.token);
//                 localStorage.setItem("access_token", keycloak.token!);
//                 localStorage.setItem("refresh_token", keycloak.refreshToken!);
                
//                 // Set up token refresh - check every 5 minutes instead of 10 seconds
//                 setInterval(() => {
//                     keycloak.updateToken(30).then((refreshed: boolean) => {
//                         if (refreshed) {
//                             console.log("Token refreshed:", keycloak.token);
//                             // Update localStorage with new tokens
//                             localStorage.setItem("access_token", keycloak.token!);
//                             if (keycloak.refreshToken) {
//                                 localStorage.setItem("refresh_token", keycloak.refreshToken);
//                             }
//                         }
//                     }).catch(() => {
//                         console.error("Failed to refresh token");
//                         // Optionally redirect to login
//                         keycloak.login();
//                     });
//                 }, 300000); // 5 minutes
//             } else {
//                 console.warn("Not authenticated");
//             }
//             return authenticated;
//         }).catch((error: any) => {
//             console.error("Keycloak initialization failed:", error);
//             return false;
//         });
// }

// export function getKeyCloak() {
//     return keycloak;
// }