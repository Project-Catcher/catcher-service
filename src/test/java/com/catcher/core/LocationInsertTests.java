package com.catcher.core;

import com.catcher.core.domain.entity.Location;
import com.catcher.infrastructure.jpa.repository.LocationJpaRepository;
import com.catcher.core.request.KakaoMapsRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/*
    테스트 코드가 아닌 1회성 location 테이블 데이터 적재용 코드입니다.
    원본 데이터 파일이 필요할 경우 요청주세요
 */

@SpringBootTest
public class LocationInsertTests {

    @Autowired
    LocationJpaRepository locationJpaRepository;
    
    @Autowired
    KakaoApiClient kakaoApiClient;

    @Test
    @Transactional
    @Disabled
    public void 지역코드_삽입() throws IOException {
        CsvReader csvReader = new CsvReader();
        var list = csvReader.readCSVFile("${your_file_source}", '\t', "EUC-KR", CsvReader.MyCSVData.class);

        list.forEach(row -> {
            if (row.getExistenceText().equals("존재")) {
                locationJpaRepository.save(Location.initLocation(row.getAreaCode(), row.getDescription()));
            }
        });
    }

    @Test
    @Transactional
    @Disabled
    public void 지역코드_좌표_매핑() throws IOException {
        CsvReader csvReader = new CsvReader();
        var list = csvReader.readCSVFile("${your_file_source}", ',', "EUC-KR", CsvReader.CsvDataWithNumber.class);

        final AtomicInteger i = new AtomicInteger(0);
        list.forEach(row -> {

            if (row.getType().equals("B")) {
                var locationOptional = locationJpaRepository.findByAreaCode(row.getAreaCode());
                if (locationOptional.isPresent()) {
                    var location = locationOptional.get();
                    location.editCoordinates(row.getLatitude(), row.getLongitude());
                    locationJpaRepository.save(location);
                }
            }

        });
    }

    @Test
    @Transactional
    @Disabled
    public void 지역코드_좌표_매핑2() throws IOException {
        CsvReader csvReader = new CsvReader();
        var list = csvReader.readCSVFile("{your_file_source}", ',', "UTF-8", CsvReader.CsvData3.class);

        list.forEach(row -> {

            if (!row.getLatitude().isBlank() && !row.getLongitude().isBlank()) {
                var locationString = String.join(" ", row.getArea1(), row.getArea2(), row.getArea3(), row.getArea4(), row.getArea5());
                var locationOptional = locationJpaRepository.findByDescription(locationString);
                if (locationOptional.isPresent()) {
                    var location = locationOptional.get();
                    if (location.getLatitude() != null) {
                        location.editCoordinates(row.getLatitude(), row.getLongitude());
                        locationJpaRepository.save(location);
                    }
                }
            }

        });
    }

    @Test
    @Transactional
    @Disabled
    public void 지역코드_좌표_매핑_with_kakao() throws IOException {

        var locationList = locationJpaRepository.findAll();

        for (var location : locationList) {
            var response = kakaoApiClient.searchAddress(KakaoMapsRequest.createDefaultRequest(location.getDescription()), "${your_kakao_key}");

            if (response.getDocuments().isEmpty()) {
                System.out.println("-----------------------------------");
                continue;
            }
            if (location.getAreaCode().equals(response.getDocuments().get(0).getAddress().getAreaCode())) {
                location.editCoordinates(response.getDocuments().get(0).getAddress().getLatitude(), response.getDocuments().get(0).getAddress().getLongitude());
            } else {
                System.out.println("-----------------------------------");
            }
        }

        locationJpaRepository.saveAll(locationList);


    }

    @Test
    @Transactional
    @Disabled
    public void 지역코드_null_제거() {
        var locationList = locationJpaRepository.findAllByLatitudeIsNull();

        for (var location : locationList) {
            if (!location.getAreaCode().endsWith("00")) {
                var newAreaCode = location.getAreaCode().substring(0, location.getAreaCode().length() - 2) + "00"; // 리 단위일 경우 면 단위 좌표로 대체
                var newLocation = locationJpaRepository.findByAreaCode(newAreaCode).get();
                location.editCoordinates(newLocation.getLatitude(), newLocation.getLongitude());
            }
        }

        locationJpaRepository.saveAll(locationList);
    }


}


