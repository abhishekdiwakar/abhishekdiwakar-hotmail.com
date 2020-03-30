package com.abkan.consulting.coronavirustrackingapp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmedData {
    private String state;
    private String country;
    private Integer totalCount;
}
