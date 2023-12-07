package com.elasticsearchintegration.repository;

import com.elasticsearchintegration.domain.Art;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Elasticsearch ile etkileşimde bulunmak için kullanılan Art belgeleri üzerinde işlemleri gerçekleştiren bir Repository.
 */
public interface ArtRepository extends ElasticsearchRepository<Art, String> {
    /**
     * Belirli bir sanat eserinin adı ve markasıyla eşleşen sanat eserlerini arayan özel bir sorgu.
     *
     * @param name  Aranan sanat eserinin adı
     * @param brand Aranan sanat eserinin markası
     * @return Belirtilen ad ve markaya sahip sanat eserlerinin listesi
     */
    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"brand\": \"?1\"}}]}}")
    List<Art> searchByNameAndBrand(String name, String brand);

    /**
     * Elasticsearch otomatik tamamlama (autocomplete) için özel olarak tasarlanmış bir sorgu.
     *
     * @param input Otomatik tamamlama için kullanılan giriş
     * @return Girişe göre uyan sanat eserlerinin listesi
     */
    @Query("{\"bool\": {\"must\": {\"match_phrase_prefix\": {\"name\": \"?0\"}}}}")
    List<Art> customAutocompleteSearch(String input);
/**
 *searchByNameAndBrand Metodu:
 *bool sorgusu, birden çok koşulu birleştirmek için kullanılır.
 *must ifadesi, içindeki koşulların tamamının sağlanması gerektiğini belirtir.
 *Bu sorgu, belirli bir ad ve markaya sahip sanat eserlerini aramak için kullanılır.
 *match ifadesi, belirli bir alanın belirli bir değeri içermesini sağlar.
 */

/**
 customAutocompleteSearch Metodu:
 *Bu sorgu, Elasticsearch otomatik tamamlama için özel olarak tasarlanmıştır.
 *match_phrase_prefix kullanılarak, belirli bir terimin önekleriyle eşleşen sanat eserlerini bulur.
 *Bu, otomatik tamamlama özelliği sağlamak için kullanışlıdır. Kullanıcının yazmaya başladığı bir kelimenin önekleriyle eşleşen sanat eserlerini getirir.
 */
}