package ru.rgs.csvparser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rgs.csvparser.client.ScoringClient;
import ru.rgs.csvparser.client.ScoringRequest;
import ru.rgs.csvparser.client.ScoringResponse;
import ru.rgs.csvparser.client.exception.ScoringRequestException;
import ru.rgs.csvparser.service.exception.ProcessingException;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@Component
public class ClientInfoScoringTaskCreator {

    public final static String NOT_FOUND_DESCRIPTION = "не найден";

    @Autowired
    private ScoringClient scoringClient;

    public Callable<Object> createScoringUpdateTask(ClientInfo clientInfo) {
        Runnable task = () -> {
            try {
                ScoringResponse response = retrieveScoringResponse(clientInfo);
                updatedClientInfoScoring(response, clientInfo);
            } catch (ScoringRequestException e) {
                clientInfo.setScoringDescription(e.getMessage().toLowerCase());
            }
        };

        return Executors.callable(task);
    }

    private void updatedClientInfoScoring(ScoringResponse response, ClientInfo clientInfo) {
        clientInfo.setScoringDescription(defineScoringDescription(response));
    }

    private String defineScoringDescription(ScoringResponse response) {
        switch (response.getStatus()) {
            case COMPLETED:
                return response.getScoringValue().toString();
            case NOT_FOUND:
                return NOT_FOUND_DESCRIPTION;
            case FAILED:
                return response.getDescription();
            default:
                throw new ProcessingException("Error while setting scoring description from response");
        }
    }

    private ScoringResponse retrieveScoringResponse(ClientInfo clientInfo) {
        ScoringRequest scoringRequest = new ScoringRequest(clientInfo.getClientName().toUpperCase(), clientInfo.getContractDate());

        return scoringClient.retrieveScoring(scoringRequest);
    }
}
