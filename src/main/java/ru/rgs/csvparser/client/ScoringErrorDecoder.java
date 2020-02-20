package ru.rgs.csvparser.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.rgs.csvparser.client.exception.ScoringRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static java.util.stream.Collectors.joining;

@Component
public class ScoringErrorDecoder implements ErrorDecoder {

    @Autowired
    private ObjectMapper mapper;
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            try (BufferedReader bufferedReader = new BufferedReader(getResponseReader(response))) {
                return new ScoringRequestException(getDescription(bufferedReader));
            } catch (IOException e) {
                return defaultDecoder.decode(methodKey, response);
            }
        }

        return defaultDecoder.decode(methodKey, response);
    }

    private String getDescription(BufferedReader bufferedReader) throws IOException {
        String responseBody = bufferedReader.lines().collect(joining());
        ScoringResponse response = mapper.readValue(responseBody, ScoringResponse.class);

        return response.getDescription();
    }

    private Reader getResponseReader(Response response) throws IOException {
        return response.body().asReader();
    }
}
