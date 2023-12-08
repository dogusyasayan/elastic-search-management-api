package com.elasticsearchintegration.controller;

import com.elasticsearchintegration.domain.Art;
import com.elasticsearchintegration.model.request.SearchArtRequest;
import com.elasticsearchintegration.service.ArtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/elastic/arts")
@RequiredArgsConstructor
@Slf4j
public class ArtController {
    private final ArtService artService;

    /**
     * Yeni bir sanat eseri eklemek için Elasticsearch'e uygun RESTful API.
     *
     * @param art Eklenecek sanat eseri
     * @return Eklenen sanat eseri
     */
    @PostMapping()
    public Art createIndex(@RequestBody Art art) {
        return artService.createIndex(art);
    }

    /**
     * Elasticsearch'e uygun şekilde JSON dosyasından sanat eserlerini eklemek için kullanılan RESTful API.
     */
    @PostMapping("/init-index")
    public void addItemsFromJson() {
        artService.addItemsFromJson();
    }

    /**
     * Tüm sanat eserlerini Elasticsearch indeksinden getiren RESTful API.
     *
     * @return Tüm sanat eserleri
     */
    @GetMapping("/findAll")
    public Iterable<Art> findAll() {
        return artService.getItems();
    }

    /**
     * Tüm Elasticsearch indekslerinden tüm sanat eserlerini getiren RESTful API.
     *
     * @return Tüm indekslerden tüm sanat eserleri
     */
    @GetMapping("/allIndexes")
    public List<Art> getAllItemsFromAllIndexes() {
        return artService.getAllItemsFromAllIndexes();
    }

    /**
     * Belirli bir Elasticsearch indeksinden tüm sanat eserlerini getiren RESTful API.
     *
     * @param indexName İstenen indeks adı
     * @return Belirli bir indeksten tüm sanat eserleri
     */
    @GetMapping("/getAllDataFromIndex/{indexName}")
    public List<Art> getAllDataFromIndex(@PathVariable String indexName) {
        return artService.getAllDataFromIndex(indexName);
    }

    /**
     * Belirli bir alan ve değere göre Elasticsearch indeksinden sanat eserlerini aramak için kullanılan RESTful API.
     *
     * @param searchRequest Arama isteği
     * @return Belirli bir alan ve değere göre sanat eserleri
     */
    @GetMapping("/search")
    public List<Art> searchItemsByFieldAndValue(@RequestBody SearchArtRequest searchRequest) {
        return artService.searchItemsByFieldAndValue(searchRequest);
    }

    /**
     * Belirli bir isim ve markaya göre Elasticsearch indeksinden sanat eserlerini aramak için kullanılan RESTful API.
     *
     * @param name  Aranan isim
     * @param brand Aranan marka
     * @return Belirli bir isim ve markaya göre sanat eserleri
     */
    @GetMapping("/search/{name}/{brand}")
    public List<Art> searchItemsByNameAndBrandWithQuery(@PathVariable String name, @PathVariable String brand) {
        return artService.searchItemsByNameAndBrand(name, brand);
    }

    /**
     * Elasticsearch'de boolean sorgusu kullanılarak sanat eserlerini aramak için kullanılan RESTful API.
     *
     * @param searchRequest Boolean sorgu isteği
     * @return Boolean sorgusu sonucunda elde edilen sanat eserleri
     */
    @GetMapping("/boolQuery")
    public List<Art> boolQuery(@RequestBody SearchArtRequest searchRequest) {
        return artService.boolQueryFieldAndValue(searchRequest);
    }

    /**
     * Elasticsearch'de otomatik tamamlama (auto-suggest) için isimlere göre önerilerde bulunan RESTful API.
     *
     * @param name Aranan isim
     * @return İsim önerileri kümesi
     */
    @GetMapping("/autoSuggest/{name}")
    public Set<String> autoSuggestItemsByName(@PathVariable String name) {
        return artService.findSuggestedItemNames(name);
    }

    /**
     * Elasticsearch'de sorgu kullanarak isimlere göre önerilerde bulunan RESTful API.
     *
     * @param name Aranan isim
     * @return İsim önerileri listesi
     */
    @GetMapping("/suggestionsQuery/{name}")
    public List<String> autoSuggestItemsByNameWithQuery(@PathVariable String name) {
        return artService.autoSuggestItemsByNameWithQuery(name);
    }

    @GetMapping("/art")
    public List<Art> getArtsAccordingToName(@RequestParam String name) throws Exception {
        return artService.searchItems(name);
    }
}
