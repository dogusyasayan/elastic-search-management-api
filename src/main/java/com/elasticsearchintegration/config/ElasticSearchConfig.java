package com.elasticsearchintegration.config;

import org.apache.http.protocol.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.support.HttpHeaders;

/**
 * ElasticSearch konfigürasyon sınıfı.
 * Bu sınıf, ElasticSearch bağlantısını ve temel yapılandırmayı sağlar.
 */
@EnableElasticsearchRepositories(basePackages = "com.elasticsearchintegration.repository")
@Configuration
public abstract class ElasticSearchConfig extends ElasticsearchConfiguration {

    // ElasticSearch sunucu URL'sini içeren özellik.
    private static final String ELASTICSEARCH_URL = "localhost:9200";

    /**
     * ElasticSearch için yapılandırılmış bir ClientConfiguration nesnesi döndürür.
     *
     * @return ClientConfiguration nesnesi
     */
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(ELASTICSEARCH_URL) // Sabit URL'ye bağlantı kurulur.
                .build();
    }
}