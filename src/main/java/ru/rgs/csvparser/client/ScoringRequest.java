package ru.rgs.csvparser.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Getter
@AllArgsConstructor
public class ScoringRequest {

    private String clientName;
    private @DateTimeFormat(iso = DATE) LocalDate contractDate;
}
