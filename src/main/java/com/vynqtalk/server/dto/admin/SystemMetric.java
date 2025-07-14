package com.vynqtalk.server.dto.admin;

import lombok.Data;
import lombok.NonNull;

@Data
public class SystemMetric {
    @NonNull
    private String metric;

    @NonNull
    private Double value;
    
    @NonNull
    private String status;

}