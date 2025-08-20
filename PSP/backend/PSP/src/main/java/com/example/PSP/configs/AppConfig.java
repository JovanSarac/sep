package com.example.PSP.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() throws Exception{
        // Load default Java truststore
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        sslContextBuilder.loadTrustMaterial((chain, authType) -> true); // trust all initially

        // Load your custom PSP truststore
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream in = new FileInputStream(
                "F:/Nevena/faks/master/SEP/projekat/sep/PSP/backend/PSP/src/main/resources/truststore.jks")) {
            trustStore.load(in, "truststorepassword".toCharArray());
        }

        // Add it to the builder, combining both
        sslContextBuilder.loadTrustMaterial(trustStore, null);

        SSLContext sslContext = sslContextBuilder.build();

        SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConFactory)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
