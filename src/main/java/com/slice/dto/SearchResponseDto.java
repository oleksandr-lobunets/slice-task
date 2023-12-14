package com.slice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
public class SearchResponseDto {


    @JsonProperty(value = "total_count")
    private Long totalCount;
    @JsonProperty(value = "incomplete_results")
    private Boolean incompleteResults;
    private List<FileInfoDto> items;


    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(Boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<FileInfoDto> getItems() {
        return items;
    }

    public void setItems(List<FileInfoDto> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "SearchResponseDto{" +
                "totalCount=" + totalCount +
                ", incompleteResults=" + incompleteResults +
                ", items=" + items +
                '}';
    }
}
