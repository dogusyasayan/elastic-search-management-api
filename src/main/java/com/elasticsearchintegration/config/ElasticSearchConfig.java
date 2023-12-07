package com.elasticsearchintegration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * ElasticSearch konfigürasyon sınıfı.
 * Bu sınıf, ElasticSearch bağlantısını ve temel yapılandırmayı sağlar.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.elasticsearchintegration.repository")
@ComponentScan(basePackages = "com.elasticsearchintegration")
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    // ElasticSearch sunucu URL'sini içeren özellik.
    @Value("${elasticsearch.url}")
    private String url;

    /**
     * ElasticSearch için yapılandırılmış bir ClientConfiguration nesnesi döndürür.
     *
     * @return ClientConfiguration nesnesi
     */
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(url) // Belirtilen URL'ye bağlantı kurulur.
                .build();
    }
}
