package ru.rgs.csvparser.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "external")
public interface ScoringClient {

    @PostMapping(value = "/score")
    ScoringResponse retrieveScoring(@RequestBody ScoringRequest scoringRequest);
}
