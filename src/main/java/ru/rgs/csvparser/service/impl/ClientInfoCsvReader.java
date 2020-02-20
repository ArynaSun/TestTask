package ru.rgs.csvparser.service.impl;

import org.springframework.stereotype.Component;
import ru.rgs.csvparser.service.exception.CsvParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;

@Component
public class ClientInfoCsvReader {

    public static final String FILE_HEADER = "FIRST_NAME,LAST_NAME,MIDDLE_NAME,CONTRACT_DATE";
    private static final String SPACE = " ";
    private static final String SEPARATOR = ",";
    private static final int NAME_INDEX = 0;
    public static final int HEADER_LINE_INDEX = 0;
    private static final int DATE_INDEX = 3;
    private static final int LAST_NAME_INDEX = 1;
    private static final int MIDDLE_NAME_INDEX = 2;
    private static final int CLIENT_INFO_ITEMS = 4;
    private static final int CLIENT_INFORMATION_START_INDEX = 1;

    public List<ClientInfo> getClientInformation(Path source) {
        List<String> lines = readFile(source);
        validateLines(lines);
        List<ClientInfo> clientInformation = new ArrayList<>();

        for (int i = CLIENT_INFORMATION_START_INDEX; i < lines.size(); i++) {
            ClientInfo clientInfo = getClientInfo(lines.get(i));
            clientInformation.add(clientInfo);
        }

        return clientInformation;
    }

    private ClientInfo getClientInfo(String line) {
        String[] clientInfoItems = line.split(SEPARATOR);

        if (clientInfoItems.length != CLIENT_INFO_ITEMS) {
            throw new CsvParseException("Client info line is invalid and can't be parsed");
        }

        String clientName =
                clientInfoItems[NAME_INDEX] +
                        SPACE + clientInfoItems[MIDDLE_NAME_INDEX] +
                        SPACE + clientInfoItems[LAST_NAME_INDEX];

        return ClientInfo
                .builder()
                .clientName(clientName)
                .contractDate(LocalDate.parse(clientInfoItems[DATE_INDEX])).build();

    }

    private void validateLines(List<String> lines) {
        if (lines.size() >= 1) {
            validateHeader(lines);
        } else {
            throw new CsvParseException("List of file lines if empty");
        }
    }

    private void validateHeader(List<String> lines) {
        if (!(FILE_HEADER.equals(lines.get(HEADER_LINE_INDEX)))) {
            throw new CsvParseException("Header of file is incorrect");
        }
    }

    public List<String> readFile(Path source) {
        try {
            Stream<String> lineStream = lines(source);
            return lineStream.collect(toList());
        } catch (IOException e) {
            throw new CsvParseException("Error while reading csv file", e);
        }
    }
}
