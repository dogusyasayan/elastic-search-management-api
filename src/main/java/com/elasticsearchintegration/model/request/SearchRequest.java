package com.elasticsearchintegration.model.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SearchRequest {

    private List<String> fieldName;
    private List<String> searchValue;
}
