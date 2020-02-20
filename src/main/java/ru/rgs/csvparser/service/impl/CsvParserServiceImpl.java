package ru.rgs.csvparser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.rgs.csvparser.service.exception.ProcessingException;
import ru.rgs.csvparser.service.CsvParserService;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class CsvParserServiceImpl implements CsvParserService {

    @Autowired
    ExecutorService executorService;
    @Autowired
    private ClientInfoScoringTaskCreator scoringTaskCreator;
    @Autowired
    private ClientInfoCsvReader clientInfoCsvReader;
    @Autowired
    private ClientInfoCsvWriter clientInfoCsvWriter;

    @Override
    public Path processCsv(Path source) {
        List<ClientInfo> clientInformation = clientInfoCsvReader.getClientInformation(source);
        addScoringDescription(clientInformation);

        return clientInfoCsvWriter.write(clientInformation);
    }

    private void addScoringDescription(List<ClientInfo> clientInformation) {
        try {
            executorService.invokeAll(
                    clientInformation
                            .stream()
                            .map(clientInfo -> scoringTaskCreator.createScoringUpdateTask(clientInfo))
                            .collect(Collectors.toList())
            );
        } catch (InterruptedException e) {
            throw new ProcessingException("Error while executing threads");
        }
    }
}
