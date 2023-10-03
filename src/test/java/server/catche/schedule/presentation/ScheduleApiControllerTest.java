package server.catche.schedule.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import server.catche.schedule.application.service.ScheduleService;
import server.catche.schedule.domain.model.Schedule;
import server.catche.schedule.presentation.dto.ScheduleReq;
import server.catche.schedule.presentation.dto.ScheduleResp;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleApiController.class)
class ScheduleApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;

    private Schedule createSchedule(
            String title, String content, String thumbnail, LocalDate startDate, LocalDate endDate
    ) {
        return new Schedule(title, content, thumbnail, startDate, endDate);
    }

    @Nested
    @DisplayName("일정 등록 컨트롤러 단위 테스트")
    class RegisterSchedule {

        @DisplayName("성공")
        @Test
        void success_Test() throws Exception {
            // Given
            Schedule expectedSchedule = createSchedule("제목", "내용", "이미지.jpg"
                    , LocalDate.of(2023, 10, 3)
                    , LocalDate.of(2023, 10, 3));

            ScheduleReq.ScheduleRegisterDTO mockRequest = ScheduleReq.ScheduleRegisterDTO.builder()
                    .title(expectedSchedule.getTitle())
                    .content(expectedSchedule.getContent())
                    .thumbnail(expectedSchedule.getThumbnail())
                    .startDate(expectedSchedule.getStartDate())
                    .endDate(expectedSchedule.getEndDate())
                    .build();

            // When
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/schedule/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockRequest)));
            // Then
            resultActions.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("일정 조회 컨트롤러 단위 테스트")
    class GetSchedule {
        @Test
        void success_Test() throws Exception {
            // Given
            Long scheduleId = 1L;
            String title = "제목";
            String content = "내용";
            String thumbnail = "이미지.jpg";
            LocalDate startDate = LocalDate.of(2023, 10, 3);
            LocalDate endDate = LocalDate.of(2023, 10, 3);

            Schedule schedule = createSchedule(title, content, thumbnail, startDate, endDate);
            when(scheduleService.getSchedule(scheduleId)).thenReturn(ScheduleResp.ScheduleDTO.from(schedule));

            // When
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/schedule/{scheduleId}", scheduleId)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(title))
                    .andExpect(jsonPath("$.content").value(content))
                    .andExpect(jsonPath("$.thumbnail").value(thumbnail))
                    .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                    .andExpect(jsonPath("$.endDate").value(endDate.toString()));
        }
    }
}