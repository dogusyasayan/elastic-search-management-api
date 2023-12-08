package com.elasticsearchintegration.mapper;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.elasticsearchintegration.domain.Art;
import com.elasticsearchintegration.model.response.ArtResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchResponseMapper {
    public List<Art> mapper(SearchResponse<Art> searchResponse) {
        return searchResponse.hits().hits().stream()
                .map(artHit -> Art.builder().name(artHit.fields().get("name").toString()).build())
                .collect(Collectors.toList());
    }
}
