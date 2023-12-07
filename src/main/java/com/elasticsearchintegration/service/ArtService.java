package com.elasticsearchintegration.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearchintegration.domain.Art;
import com.elasticsearchintegration.model.request.SearchRequest;
import com.elasticsearchintegration.repository.ArtRepository;
import com.elasticsearchintegration.utils.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArtService {
    private final ArtRepository artRepository;
    private final JsonDataService jsonDataService;
    private final ElasticsearchClient elasticsearchClient;

    /**
     * Yeni bir sanat eseri ekler ve Elasticsearch üzerinde index oluşturur.
     *
     * @param art Eklenen sanat eseri
     * @return Eklenen sanat eseri
     */
    public Art createIndex(Art art) {
        return artRepository.save(art);
    }

    /**
     * JSON dosyasından sanat eserlerini ekler.
     */
    public void addItemsFromJson() {
        log.info("Adding Items from Json");
        List<Art> arts = jsonDataService.readItemsFromJson();
        artRepository.saveAll(arts);
    }

    /**
     * Tüm sanat eserlerini getirir.
     *
     * @return Tüm sanat eserleri
     */
    public Iterable<Art> getItems() {
        log.info("Getting Items");
        return artRepository.findAll();
    }

    /**
     * Tüm indekslerden tüm sanat eserlerini getirir.
     *
     * @return Tüm indekslerden tüm sanat eserleri
     */
    public List<Art> getAllItemsFromAllIndexes() {
        Query query = ElasticSearchUtil.createMatchAllQuery();
        log.info("Elasticsearch query: {}", query.toString());
        SearchResponse<Art> response = null;
        try {
            response = elasticsearchClient.search(q -> q.query(query), Art.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Elasticsearch response: {}", response.toString());

        return extractItemsFromResponse(response);
    }

    /**
     * Belirli bir indeksten tüm sanat eserlerini getirir.
     *
     * @param indexName İstenen indeks adı
     * @return Belirli bir indeksten tüm sanat eserleri
     */
    public List<Art> getAllDataFromIndex(String indexName) {
        try {
            var supplier = ElasticSearchUtil.createMatchAllQuery();
            log.info("Elasticsearch query {}", supplier.toString());

            SearchResponse<Art> response = elasticsearchClient.search(
                    q -> q.index(indexName).query(supplier), Art.class);

            log.info("Elasticsearch response {}", response.toString());

            return extractItemsFromResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Belirli bir alan ve değere göre sanat eserlerini arar.
     *
     * @param searchRequest Arama isteği
     * @return Belirli bir alan ve değere göre sanat eserleri
     */
    public List<Art> searchItemsByFieldAndValue(SearchRequest searchRequest) {
        SearchResponse<Art> response = null;
        try {
            Supplier<Query> querySupplier = ElasticSearchUtil.buildQueryForFieldAndValue(searchRequest.getFieldName().get(0),
                    searchRequest.getSearchValue().get(0));//sorgu olustur

            log.info("Elasticsearch query {}", querySupplier.toString());

            response = elasticsearchClient.search(q -> q.index("items_index")
                    .query(querySupplier.get()), Art.class);//sorguyu calistir ve cevabi alir

            log.info("Elasticsearch response: {}", response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extractItemsFromResponse(response);
    }

    /**
     * Belirli bir isim ve markaya göre sanat eserlerini arar.
     *
     * @param name  Aranan isim
     * @param brand Aranan marka
     * @return Belirli bir isim ve markaya göre sanat eserleri
     */
    public List<Art> searchItemsByNameAndBrand(String name, String brand) {
        return artRepository.searchByNameAndBrand(name, brand);
    }

    /**
     * Boolean sorgusu kullanılarak sanat eserlerini arar.
     *
     * @param searchRequest Boolean sorgu isteği
     * @return Boolean sorgusu sonucunda elde edilen sanat eserleri
     */
    public List<Art> boolQueryFieldAndValue(SearchRequest searchRequest) {
        try {
            var supplier = ElasticSearchUtil.createBoolQuery(searchRequest);
            log.info("Elasticsearch query: " + supplier.get().toString());

            SearchResponse<Art> response = elasticsearchClient.search(q ->
                    q.index("items_index").query(supplier.get()), Art.class);
            log.info("Elasticsearch response: {}", response.toString());

            return extractItemsFromResponse(response);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Otomatik tamamlama (auto-suggest) için isimlere göre önerilerde bulunur.
     *
     * @param itemName Aranan isim
     * @return İsim önerileri kümesi
     */
    public Set<String> findSuggestedItemNames(String itemName) {
        Query autoSuggestQuery = ElasticSearchUtil.buildAutoSuggestQuery(itemName);
        log.info("Elasticsearch query: {}", autoSuggestQuery.toString());

        try {
            return elasticsearchClient.search(q -> q.index("items_index").query(autoSuggestQuery), Art.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .map(Art::getName)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sorgu kullanarak isimlere göre önerilerde bulunur.
     *
     * @param name Aranan isim
     * @return İsim önerileri listesi
     */
    public List<String> autoSuggestItemsByNameWithQuery(String name) {
        List<Art> arts = artRepository.customAutocompleteSearch(name);
        log.info("Elasticsearch response: {}", arts.toString());
        return arts
                .stream()
                .map(Art::getName)
                .collect(Collectors.toList());
    }

    /**
     * Elasticsearch sorgu cevabından sanat eserlerini çıkarır.
     *
     * @param response Elasticsearch sorgu cevabı
     * @return Elasticsearch sorgu cevabındaki sanat eserleri
     */
    public List<Art> extractItemsFromResponse(SearchResponse<Art> response) {
        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}