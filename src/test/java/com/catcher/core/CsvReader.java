package com.catcher.core;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

class CsvReader {

    public <T> List<T> readCSVFile(String filePath, char separator, String charset, Class<T> clazz) throws IOException {
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(filePath), charset))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(separator)
                        .build())
                .withSkipLines(1)
                .build()) {
            List<T> csvDataList = new CsvToBeanBuilder<T>(csvReader)
                    .withType(clazz)
                    .build()
                    .parse();

            return csvDataList;
        }
    }

    public static class CsvDataWithNumber {

        @CsvBindByPosition(position = 0)
        private String areaCode;

        @CsvBindByPosition(position = 5)
        private String latitude;

        @CsvBindByPosition(position = 6)
        private String longitude;

        @CsvBindByPosition(position = 7)
        private String type;

        public String getAreaCode() {
            return areaCode;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getType() {
            return type;
        }

    }

    public static class MyCSVData {

        @CsvBindByPosition(position = 0)
        private String areaCode;

        @CsvBindByPosition(position = 1)
        private String description;

        @CsvBindByPosition(position = 2)
        private String existenceText;

        public String getAreaCode() {
            return areaCode;
        }

        public String getDescription() {
            return description;
        }

        public String getExistenceText() {
            return existenceText;
        }

    }

    public static class CsvData3 {

        @CsvBindByPosition(position = 0)
        private String area1;

        @CsvBindByPosition(position = 1)
        private String area2;

        @CsvBindByPosition(position = 2)
        private String area3;

        @CsvBindByPosition(position = 3)
        private String area4;

        @CsvBindByPosition(position = 4)
        private String area5;

        @CsvBindByPosition(position = 5)
        private String latitude;

        @CsvBindByPosition(position = 6)
        private String longitude;

        public String getArea1() {
            return area1;
        }

        public String getArea2() {
            return area2;
        }

        public String getArea3() {
            return area3;
        }

        public String getArea4() {
            return area4;
        }

        public String getArea5() {
            return area5;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

    }

}
