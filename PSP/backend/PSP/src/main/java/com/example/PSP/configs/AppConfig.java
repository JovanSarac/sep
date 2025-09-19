package com.example.PSP.configs;


import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
@EnableDiscoveryClient
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() throws Exception{
        // Load default Java truststore
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        sslContextBuilder.loadTrustMaterial((chain, authType) -> true); // trust all initially

        // Load your custom PSP truststore
        KeyStore customTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream in = new FileInputStream(
                "D:/sep/PSP/backend/PSP/src/main/resources/truststore.jks")) {
            customTrustStore.load(in, "truststorepassword".toCharArray());
        }

        // Add it to the builder, combining both
        sslContextBuilder.loadTrustMaterial(customTrustStore, null);

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

//        SSLContext sslContext = SSLContextBuilder.create()
//                .loadTrustMaterial((chain, authType) -> true) // Trust all certs
//                .build();
//
//        // No hostname verification
//        SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(
//                sslContext, NoopHostnameVerifier.INSTANCE);
//
//        // Connection manager using the SSL context
//        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
//                .setSSLSocketFactory(sslConFactory)
//                .build();
//
//        // Create HttpClient with the above connection manager
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(cm)
//                .build();
//
//        // Use the custom HttpClient in RestTemplate
//        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        return new RestTemplate(requestFactory);
    }
}
