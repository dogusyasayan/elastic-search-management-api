# Spring Boot ve Elasticsearch Entegrasyonu

Bu proje, Spring Boot ve Elasticsearch'in entegrasyonunu sağlamaktadır. Elasticsearch ile yapılan temel sorgu ve indeksleme operasyonlarına örnek teşkil etmektedir.

## Proje Yapısı

- **ElasticSearchConfig (com.elasticsearchintegration.config):** Elasticsearch yapılandırmasını içeren sınıf. Elasticsearch sunucu URL'sini tanımlar ve yapılandırılmış bir `ClientConfiguration` nesnesi döndürür.

- **ArtController (com.elasticsearchintegration.controller):** Elasticsearch ile etkileşim sağlayan RESTful API'ları içeren controller sınıfı. Sanat eserleriyle ilgili işlemleri gerçekleştirebilir, arama ve öneri sorguları yapabilir.

- **Art (com.elasticsearchintegration.model):** Elasticsearch indeksi için kullanılan sanat eseri model sınıfı. `@Document` ile işaretlenmiş ve Elasticsearch indeks adı belirtilmiştir.

- **SearchArtRequest (com.elasticsearchintegration.model):** Elasticsearch için arama isteğini temsil eden model sınıfı.

- **ArtRepository (com.elasticsearchintegration.repository):** Elasticsearch işlemleri için kullanılan repository sınıfı. Özel sorgular içerebilir.

- **ArtService (com.elasticsearchintegration.service):** Sanat eseri işlemlerini gerçekleştiren servis sınıfı. Elasticsearch ile iletişim kurar ve gerekirse JSON dosyasından veri ekler.

## Elasticsearch Docker Compose Dosyası

Elasticsearch sunucusunu Docker üzerinde çalıştırmak için aşağıdaki Docker Compose dosyasını kullanabilirsiniz:

```yaml
version: '3'
services:
  elasticsearch:
    image: elasticsearch:8.8.0
    environment:
      - discovery.type=single-node
      - validate_after_inactivity=0
      - max_open_files=65536
      - max_content_length_in_bytes=100000000
      - transport.host=elasticsearch
      - xpack.security.enabled=false
    volumes:
      - $HOME/app:/var/app
    networks:
      - my-network
    ports:
      - "9200:9200"
      - "9300:9300"

networks:
  my-network:
```

## Elasticsearch İndeks Ayarları

Elasticsearch indeksi için kullanılan özel ayarlar:

```json
{
  "analysis": {
    "filter": {
      "custom_filter": {
        "type": "edge_ngram",
        "min_gram": 1,
        "max_gram": 20
      }
    },
    "analyzer": {
      "custom_search": {
        "type": "custom",
        "tokenizer": "standard",
        "filter": [
          "lowercase"
        ]
      },
      "custom_index": {
        "type": "custom",
        "tokenizer": "standard",
        "filter": [
          "lowercase",
          "custom_filter"
        ]
      }
    }
  }
}
```

## API Endpointleri

- **POST /api/elastic/arts:** Yeni bir sanat eseri eklemek için Elasticsearch'e uygun RESTful API.
- **POST /api/elastic/arts/init-index:** JSON dosyasından sanat eserlerini Elasticsearch'e eklemek için RESTful API.
- **GET /api/elastic/arts/findAll:** Tüm sanat eserlerini Elasticsearch indeksinden getiren RESTful API.
- **GET /api/elastic/arts/allIndexes:** Tüm Elasticsearch indekslerinden tüm sanat eserlerini getiren RESTful API.
- **GET /api/elastic/arts/getAllDataFromIndex/{indexName}:** Belirli bir Elasticsearch indeksinden tüm sanat eserlerini getiren RESTful API.
- **GET /api/elastic/arts/search:** Belirli bir alan ve değere göre sanat eserlerini aramak için RESTful API.
- **GET /api/elastic/arts/search/{name}/{brand}:** Belirli bir isim ve markaya göre sanat eserlerini aramak için RESTful API.
- **GET /api/elastic/arts/boolQuery:** Elasticsearch'de boolean sorgusu kullanılarak sanat eserlerini aramak için RESTful API.
- **GET /api/elastic/arts/autoSuggest/{name}:** Elasticsearch'de otomatik tamamlama (auto-suggest) için isimlere göre önerilerde bulunan RESTful API.
- **GET /api/elastic/arts/suggestionsQuery/{name}:** Elasticsearch'de sorgu kullanarak isimlere göre önerilerde bulunan RESTful API.


## Kullanım

Elasticsearch Docker Compose dosyasını çalıştırarak Elasticsearch sunucunuzu başlatın:

```sh
docker-compose up -d
```

Spring Boot uygulamasını başlatın:
```sh
./mvnw spring-boot:run
```
Uygulama başladıktan sonra, http://localhost:8080/api/elastic/arts adresine giderek API'ları kullanabilirsiniz.

## Daha Fazla Bilgi

Elasticsearch ile ilgili daha fazla bilgi edinmek istiyorsanız [bu Medium makalesini](https://medium.com/@dogusyasayan/elasticsearch-nedir-ve-nas%C4%B1l-%C3%A7al%C4%B1%C5%9F%C4%B1r-0af4ba37ef7d) okuyabilir ve geri dönüşlerinizi paylaşabilirsiniz.




