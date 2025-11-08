package com.cdcrane.transakt.transactions.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExceptionErrorResponse {

    private String message;
    private int responseCode;
    private long timestamp;

}
