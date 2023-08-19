package kr.co.finote.backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.HttpHeaders;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${ELASTICSEARCH_HOST}")
    private String elasticSearchHost;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticSearchHost)
                .withDefaultHeaders(headers())
                .build();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(RestClients.create(clientConfiguration()).rest());
    }

    public HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("ACCEPT", "application/vnd.elasticsearch+json;compatible-with=7");
        headers.add("Content-Type", "application/vnd.elasticsearch+json;compatible-with=7");
        return headers;
    }
}
