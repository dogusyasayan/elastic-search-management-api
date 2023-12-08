package com.elasticsearchintegration.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@Builder
@Document(indexName = "arts_index") // Elasticsearch indeks adı
@Setting(settingPath = "static/es-settings.json") // Elasticsearch ayar dosyasının yolu
@JsonIgnoreProperties(ignoreUnknown = true)
public class Art {
    @Id
    private int id;

    @Field(name = "name", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    private String name;

    @Field(name = "price", type = FieldType.Double)
    private Double price;

    @Field(name = "brand", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    private String brand;

    @Field(name = "category", type = FieldType.Keyword)
    private String category;
}