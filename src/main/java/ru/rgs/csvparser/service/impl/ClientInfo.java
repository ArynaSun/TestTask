package ru.rgs.csvparser.service.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ClientInfo {

    private String clientName;
    private LocalDate contractDate;
    private String scoringDescription;
}
