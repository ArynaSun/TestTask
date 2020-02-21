package ru.rgs.csvparser.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rgs.csvparser.service.CsvParserService;
import ru.rgs.csvparser.service.impl.CsvParserServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class MainConfiguration {

    private static final int NUMBER_OF_THREADS = 3;

    @Bean
    public CsvParserService csvParserService() {
        return new CsvParserServiceImpl();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }
}
