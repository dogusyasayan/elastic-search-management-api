package com.elasticsearchintegration.service;

import com.elasticsearchintegration.domain.Art;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonDataService {

    private final ObjectMapper objectMapper;

    public List<Art> readItemsFromJson() {
        try {
            // Verilerin bulunduğu JSON dosyasını okumak için bir kaynak oluşturulur.
            ClassPathResource resource = new ClassPathResource("data/arts.json");
            // Kaynaktan gelen InputStream kullanılarak JSON verileri okunur ve ilgili nesne türüne dönüştürülür.
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, new TypeReference<List<Art>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("JSON verilerini okuma sırasında bir hata oluştu.", e);
        }
    }
}
