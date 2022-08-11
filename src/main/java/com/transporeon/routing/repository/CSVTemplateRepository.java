package com.transporeon.routing.repository;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSVTemplateRepository<T> {

    private final List<T> data;

    public CSVTemplateRepository(InputStream inputStream, Class<T> clazz) {
        List<T> result;
        try {
            result = load(inputStream, clazz);
        } catch (Exception e) {
            throw new IllegalStateException("CSV file cannot be loaded");
        }
        data = result;
    }

    public List<T> findAll() {
        return data;
    }

    protected <T> List<T> load(InputStream inputStream, Class<T> type) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            return new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .build()
                    .parse();
        }
    }
}
