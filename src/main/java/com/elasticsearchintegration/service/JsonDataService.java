package com.elasticsearchintegration.service;

import com.elasticsearchintegration.domain.Art;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
            log.error("JSON verilerini okuma sırasında bir hata oluştu : {}", e.getMessage());
            throw new RuntimeException("JSON verilerini okuma sırasında bir hata oluştu.", e);
        }
    }
}
