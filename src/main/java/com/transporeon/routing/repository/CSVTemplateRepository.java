package com.transporeon.routing.repository;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSVTemplateRepository<T> {

    private final List<T> data;

    public CSVTemplateRepository(Path pathToCsvFile, Class<T> clazz) {
        List<T> result;
        try {
            result = load(pathToCsvFile, clazz);
        } catch (IOException e) {
            throw new IllegalStateException("CSV file cannot not be loaded");
        }
        data = result;
    }


    public List<T> findAll() {
        return data;
    }

    protected <T> List<T> load(Path pathToCsvFile, Class<T> type) throws IOException {
        try (Reader reader = Files.newBufferedReader(pathToCsvFile)) {
            return new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .build()
                    .parse();
        }
    }
}
