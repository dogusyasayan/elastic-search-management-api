package com.elasticsearchintegration.utils;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.elasticsearchintegration.model.request.SearchArtRequest;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * Elasticsearch sorgularını oluşturmak için yardımcı metotları içeren yardımcı sınıf.
 */
@UtilityClass
public class ElasticSearchUtil {

    /**
     * Tüm öğeleri getiren basit bir sorgu oluşturur.
     *
     * @return MatchAll sorgusu
     */
    public static Query createMatchAllQuery() {
        return Query.of(q -> q.matchAll(new MatchAllQuery.Builder().build()));
    }

    /**
     * Belirli bir alan ve değere göre sorgu oluşturur.
     *
     * @param fieldName   Arama yapılacak alanın adı
     * @param searchValue Arama yapılacak değer
     * @return Belirli bir alan ve değere göre Match sorgusu
     */
    public static Supplier<Query> buildQueryForFieldAndValue(String fieldName, String searchValue) {
        return () -> Query.of(q -> q.match(buildMatchQueryForFieldAndValue(fieldName, searchValue)));
    }

    /**
     * Belirli bir alan ve değere göre Match sorgusu oluşturur.
     *
     * @param fieldName   Arama yapılacak alanın adı
     * @param searchValue Arama yapılacak değer
     * @return Match sorgusu
     */
    public static MatchQuery buildMatchQueryForFieldAndValue(String fieldName, String searchValue) {
        return new MatchQuery.Builder()
                .field(fieldName)
                .query(searchValue)
                .build();
    }

    /**
     * Boolean sorgusu oluşturur.
     *
     * @param dto Arama talebini içeren veri transfer nesnesi
     * @return Boolean sorgusu
     */
    public static Supplier<Query> createBoolQuery(SearchArtRequest dto) {
        return () -> Query.of(q -> q.bool(boolQuery(dto.getFieldName().get(0).toString(), dto.getSearchValue().get(0),
                dto.getFieldName().get(1).toString(), dto.getSearchValue().get(1))));
    }

    /**
     * Boolean sorgusu oluşturur.
     *
     * @param key1   İlk alan adı
     * @param value1 İlk değer
     * @param key2   İkinci alan adı
     * @param value2 İkinci değer
     * @return Boolean sorgusu
     */
    public static BoolQuery boolQuery(String key1, String value1, String key2, String value2) {
        return new BoolQuery.Builder()
                .filter(termQuery(key1.toString(), value1))
                .must(termQuery(key2.toString(), value2))
                .build();
    }

    /**
     * Term sorgusu oluşturur.
     *
     * @param field Alan adı
     * @param value Değer
     * @return Term sorgusu
     */
    public static Query termQuery(String field, String value) {
        return Query.of(q -> q.term(new TermQuery.Builder()
                .field(field)
                .value(value)
                .build()));
    }

    /**
     * Match sorgusu oluşturur.
     *
     * @param field Alan adı
     * @param value Değer
     * @return Match sorgusu
     */
    public static Query matchQuery(String field, String value) {
        return Query.of(q -> q.match(new MatchQuery.Builder()
                .field(field)
                .query(value)
                .build()));
    }

    /**
     * Otomatik tamamlama (auto-suggest) için sorgu oluşturur.
     *
     * @param name Aranan isim
     * @return Otomatik tamamlama için Match sorgusu
     */
    public static Query buildAutoSuggestQuery(String name) {
        return Query.of(q -> q.match(createAutoSuggestMatchQuery(name)));
    }

    /**
     * Otomatik tamamlama (auto-suggest) için Match sorgusu oluşturur.
     *
     * @param name Aranan isim
     * @return Match sorgusu
     */
    public static MatchQuery createAutoSuggestMatchQuery(String name) {
        return new MatchQuery.Builder()
                .field("name")
                .query(name)
                .analyzer("custom_index")
                .build();
    }
}
