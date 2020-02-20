package ru.rgs.csvparser.client;

import lombok.Getter;

@Getter
public class ScoringResponse {

    private StatusResponse status;
    private String description;
    private Double scoringValue;


    public enum StatusResponse {
        COMPLETED,
        FAILED,
        NOT_FOUND;
    }
}
