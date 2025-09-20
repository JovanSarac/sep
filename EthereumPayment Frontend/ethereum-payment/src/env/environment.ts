export const environment = {
    production: false,
    pspHost: 'https://localhost:8090/api/',
    apiGateway: 'https://localhost:9001/',
    keycloak: {
        config: {
        url: 'http://localhost:8080',
        realm: 'sep-realm',
        clientId: 'sep-eth-frontend'
        },
        initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false
        }
    }
};


  