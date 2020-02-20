package ru.rgs.csvparser.service.impl;

import org.springframework.stereotype.Component;
import ru.rgs.csvparser.service.exception.CsvParseException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.join;

@Component
public class ClientInfoCsvWriter {

    private final static  String SEPARATOR = ",";
    private final static  String END_OF_LINE = "\n";
    private final static  String DEST_FILE_NAME = "build/destination.csv";

    public Path write(List<ClientInfo> clientInfos){
        Path path = Paths.get(DEST_FILE_NAME);
        try (OutputStreamWriter csvWriter = new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)){
            writeHeader(csvWriter);
            writeClientInfo(csvWriter, clientInfos);
        } catch (IOException e) {
           throw new CsvParseException("Error while writing to csv file", e);
        }
        return path;
    }

    private static void writeHeader(OutputStreamWriter csvWriter) throws IOException {
        String header = join(SEPARATOR, "CLIENT_NAME", "CONTRACT_DATE", "SCORING" + END_OF_LINE);
        csvWriter.append(header);
    }

    private void writeClientInfo(OutputStreamWriter csvWriter, List<ClientInfo> clientInfos) throws IOException {
        for (ClientInfo clientInfo : clientInfos) {
            String line = join(SEPARATOR, clientInfo.getClientName().toUpperCase(),
                    clientInfo.getContractDate().toString(), clientInfo.getScoringDescription() + END_OF_LINE);
            csvWriter.append(line);
        }
    }
}
