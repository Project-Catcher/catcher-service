package com.catcher.datasource;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.ScheduleParticipantRepository;
import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.Location;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.datasource.repository.LocationJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.catcher.core.domain.entity.enums.UserProvider.CATCHER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ScheduleParticipantRepositoryImplTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleParticipantRepository scheduleParticipantRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationJpaRepository locationJpaRepository;

    List<User> userList;
    List<Schedule> scheduleList;
    List<ScheduleParticipant> scheduleParticipantList;
    Location location = Location.initLocation("1111000000", "서울특별시 종로구");

    @BeforeEach
    void beforeEach() {
        userList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        scheduleParticipantList = new ArrayList<>();
        locationJpaRepository.save(location);

        for (int i = 0; i < 5; i++) {
            User user = createUser();
            userList.add(user);
        }
        userRepository.saveAll(userList);

        for (User user : userList) {
            ScheduleStatus[] statusValues = ScheduleStatus.values();
            Random random = new Random();

            Schedule schedule = generateSchedule(
                    user,
                    statusValues[random.nextInt(statusValues.length)],
                    LocalDateTime.now().plus(5, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(7, ChronoUnit.DAYS)
            );

            scheduleList.add(schedule);
        }

        scheduleRepository.saveAll(scheduleList);

        flushAndClearPersistence();
    }

    @DisplayName("FAIL: 일정 참여 신청이 처리 되지 않은 사용자가 일정 취소 요청을 보내면, Error가 반환되어야 한다")
    @Test
    void fail_if_non_participant_request_cancel_participation_schedule(){
        //given
        Long userId = userList.get(0).getId();
        Long scheduleId = scheduleList.get(0).getId();

        //when
        var result = Assertions.assertThrows(BaseException.class, () -> {
            scheduleParticipantRepository.cancelScheduleParticipant(userId, scheduleId);
        });

        //then
        assertThat(result.getStatus()).isEqualTo(BaseResponseStatus.FAIL_CANCEL_SCHEDULE_PARTICIPANT_STATUS);
    }

    @DisplayName("SUCCESS: 일정 참여 신청한 사용자가 일정 취소 요청을 보내면, 참여자 상태가 CANCEL로 변경된다")
    @Test
    void status_is_cancel_if_participant_request_cancel_participation_schedule(){
        //given
        Long userId = userList.get(1).getId();
        Schedule schedule = scheduleList.get(0);
        scheduleParticipantList.add(generateScheduleParticipant(userList.get(0), schedule, ParticipantStatus.APPROVE));
        scheduleParticipantList.add(generateScheduleParticipant(userList.get(1), schedule, ParticipantStatus.PENDING));

        scheduleParticipantRepository.saveAll(scheduleParticipantList);
        flushAndClearPersistence();

        //when
        scheduleParticipantRepository.cancelScheduleParticipant(userId, schedule.getId());
        ScheduleParticipant scheduleParticipant = scheduleParticipantRepository.findByUserAndScheduleId(userId, schedule.getId()).orElseThrow();
        Long participantNum = scheduleParticipantRepository.findCountScheduleParticipantByStatusAndScheduleId(ParticipantStatus.APPROVE, schedule.getId());

        //then
        assertThat(scheduleParticipant.getStatus()).isEqualTo(ParticipantStatus.CANCEL);
        assertThat(scheduleParticipant.getDeletedAt()).isNotNull();
        assertThat(participantNum).isEqualTo(1);
    }


    private ScheduleParticipant generateScheduleParticipant(User user, Schedule schedule, ParticipantStatus status) {
        return ScheduleParticipant.builder()
                .user(user)
                .schedule(schedule)
                .status(status)
                .build();
    }

    private Schedule generateSchedule(User user, ScheduleStatus scheduleStatus, LocalDateTime startAt, LocalDateTime endAt) {
        return Schedule.builder()
                .user(user)
                .location(location)
                .participantLimit(5L)
                .title("title")
                .description("description")
                .scheduleStatus(scheduleStatus)
                .startAt(startAt)
                .endAt(endAt)
                .viewCount(1L)
                .participateStartAt(startAt)
                .participateEndAt(endAt)
                .thumbnailUrl("https://thumbnail-img-s3.s3.ap-northeast-2.amazonaws.com/dog.jpg")
                .build();
    }

    private User createUser() {
        return User.builder()
                .username(createRandomUUID())
                .password(createRandomUUID())
                .phone(createRandomUUID())
                .email(createRandomUUID())
                .profileImageUrl(null)
                .introduceContent(null)
                .nickname(createRandomUUID())
                .userProvider(CATCHER)
                .userRole(UserRole.USER)
                .userAgeTerm(ZonedDateTime.now())
                .userServiceTerm(ZonedDateTime.now())
                .userPrivacyTerm(ZonedDateTime.now())
                .emailMarketingTerm(ZonedDateTime.now())
                .phoneMarketingTerm(ZonedDateTime.now())
                .build();
    }

    private void flushAndClearPersistence() {
        em.flush();
        em.clear();
    }

    private String createRandomUUID() {
        return UUID.randomUUID().toString();
    }
}