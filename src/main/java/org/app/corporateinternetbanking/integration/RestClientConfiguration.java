package org.app.corporateinternetbanking.integration;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    int connectTimeoutMillis=5000;
    int readTimeoutMillis =10000;
    int maxTotalConnections=100;
    int maxConnectionPerRoute=20;

    @Bean
    public RestClient restClient(){
        return  RestClient.builder()
                .requestFactory(createRequestFactory())
                .build();
    }
    private HttpComponentsClientHttpRequestFactory createRequestFactory(){
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }
    private CloseableHttpClient httpClient(){
        PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionPerRoute);
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom()//.setSocketTimeout(Timeout.ofSeconds(5))
               .setSocketTimeout(Timeout.ofSeconds(5))
               .setTimeToLive(Timeout.ofMinutes(5))
                .setConnectTimeout(Timeout.ofSeconds(5))
                  .build());

        RequestConfig requestConfig=RequestConfig.custom()
                .setResponseTimeout(Timeout.ofMilliseconds(readTimeoutMillis))
                .setRedirectsEnabled(true)
                .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(Timeout.ofSeconds(30))
                .build();
    }
}
