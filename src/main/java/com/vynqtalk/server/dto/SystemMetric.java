package com.vynqtalk.server.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class SystemMetric {
    @NonNull
    private String metric;

    @NonNull
    private String value;
    
    @NonNull
    private String status;

}