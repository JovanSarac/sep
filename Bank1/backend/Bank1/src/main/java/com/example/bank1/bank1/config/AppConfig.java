package com.example.bank1.bank1.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() throws Exception{
        // Load default Java truststore
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        sslContextBuilder.loadTrustMaterial((chain, authType) -> true); // trust all initially

        // Load your custom PSP truststore
        /*KeyStore customTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream in = new FileInputStream(
                "F:/Nevena/faks/master/SEP/projekat/sep/PSP/backend/PSP/src/main/resources/truststore.jks")) {
            customTrustStore.load(in, "truststorepassword".toCharArray());
        }*/
       
        KeyStore customTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream in = new ClassPathResource("truststore.jks").getInputStream()) {
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
    }
}

