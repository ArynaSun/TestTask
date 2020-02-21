package ru.rgs.csvparser.client.exception;

import lombok.Getter;

@Getter
public class ScoringRequestException extends RuntimeException {

    public ScoringRequestException(String message) {
        super(message);
    }
}
