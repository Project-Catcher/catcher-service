package com.catcher.resource;

import com.catcher.common.advice.CatcherControllerAdvice;
import com.catcher.common.response.CommonResponse;
import com.catcher.resource.response.InternalAddressResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class InternalAddressControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private InternalAddressController internalAddressController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(internalAddressController)
                .setControllerAdvice(new CatcherControllerAdvice())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @DisplayName("주소를 평문으로 검색하면 법정동 지역코드를 반환한다")
    @Test
    void internal_address_controller_test() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/internal/address/강원도 강릉시 용강동 58-1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        CommonResponse<InternalAddressResponse> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                });

        //Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getResult().getAreaCode()).isNotBlank();
    }
}
