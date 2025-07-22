package org.example.restClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryDto {
    private String name;
    private OwnerDto owner;
    private boolean fork;
}
