package com.catcher.core;

import com.catcher.common.utils.HashCodeGenerator;
import com.catcher.core.domain.entity.CatcherItem;
import com.catcher.core.domain.entity.Category;
import com.catcher.core.port.AddressPort;
import com.catcher.core.port.CatcherItemPort;
import com.catcher.core.port.CategoryPort;
import com.catcher.core.port.LocationPort;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
    등산 카테고리 캐쳐 아이템 적재를 위한 1회성 코드입니다.
    TODO: 카카오 API에서 법정동 코드는 제공되지 않고 행정동 코드만 제공되는 경우가 있음 -> 행정동 - 법정동 코드 매핑 테이블을 만들어야됨
    TODO: description, thumbnailUrl을 구해야됨 -> 이미지는 API가 있는듯? 설명은 찾아봐야됨
 */

@SpringBootTest(properties = "spring.profiles.active=local")
public class MountainInsertTest {

    @Autowired
    CategoryPort categoryPort;

    @Autowired
    CatcherItemPort catcherItemPort;

    @Autowired
    AddressPort addressPort;

    @Autowired
    LocationPort locationPort;

    public static final String CATEGORY_NAME = "mountain";


    @Disabled
    @ParameterizedTest
    @CsvFileSource(resources = "/MNT_CODE.csv", numLinesToSkip = 1, encoding = "EUC-KR")
    public void 산_정보_최초_insert(Long order, String mountainName, String locationDescription, String mountainCode) {
        Category category = categoryPort.findByName(CATEGORY_NAME)
                .orElseGet(() -> categoryPort.save(Category.create(CATEGORY_NAME)));

        String hashKey = HashCodeGenerator.hashString(CATEGORY_NAME, mountainCode);
        final var catcherItemOptional = catcherItemPort.findByItemHashValue(hashKey);
        if (catcherItemOptional.isPresent()) {
            return;
        }
        
        var address = addressPort.getAddressByQuery(locationDescription).get();

        var location = locationPort.findByAreaCode(address.getAreaCode()).get();

        var catcherItem = CatcherItem
                .builder()
                .category(category)
                .location(location)
                .title(mountainName)
                .itemHashValue(hashKey)
                .build();

        catcherItemPort.save(catcherItem);
    }
}