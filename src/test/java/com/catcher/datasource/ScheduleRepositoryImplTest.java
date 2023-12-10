package com.catcher.datasource;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ContentType;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.datasource.repository.LocationJpaRepository;
import com.catcher.datasource.repository.ScheduleJpaRepository;
import com.catcher.datasource.repository.ScheduleParticipantJpaRepository;
import com.catcher.datasource.repository.UploadJpaRepository;
import com.catcher.datasource.user.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.catcher.core.domain.entity.enums.UserProvider.CATCHER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class ScheduleRepositoryImplTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    ScheduleParticipantJpaRepository scheduleParticipantJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UploadJpaRepository uploadJpaRepository;

    @Autowired
    LocationJpaRepository locationJpaRepository;

    List<User> userList;
    List<Schedule> scheduleList;
    List<ScheduleParticipant> scheduleParticipantList;
    Location location = Location.initLocation("areacode", "description");

    @BeforeEach
    void beforeEach() {
        userList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        scheduleParticipantList = new ArrayList<>();
        locationJpaRepository.save(location);

        for (int i = 0; i < 10; i++) {
            User user = createUser();
            userList.add(user);
            userJpaRepository.save(user);
        }

        for (User user : userList) {
            ScheduleStatus[] statusValues = ScheduleStatus.values();
            Random random = new Random();
            UploadFile uploadFile = generateUploadFile();
            uploadJpaRepository.save(uploadFile);

            Schedule schedule = generateSchedule(
                    user,
                    // TODO: 랜덤값으로 변경 필요
                    ScheduleStatus.NORMAL,
                    LocalDateTime.now().plus(5, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(7, ChronoUnit.DAYS),
                    uploadFile
            );

            scheduleList.add(schedule);
            scheduleJpaRepository.save(schedule);
        }

        for (User user : userList) {
            ParticipantStatus[] participantStatus = ParticipantStatus.values();
            Random random = new Random();
            ScheduleParticipant scheduleParticipant = generateScheduleParticipant(user, scheduleList.get(0), participantStatus[random.nextInt(participantStatus.length)]);

            scheduleParticipantList.add(scheduleParticipant);
            scheduleParticipantJpaRepository.save(scheduleParticipant);
        }

        flushAndClearPersistence();
    }

    @DisplayName("다가오는 일정이 여러개일 때, 조건에 맞는 상위 7개만 반환한다")
    @Test
    void upcomingScheduleList_will_return_satisfied_top_7() {
        //given

        for (User user : userList) {
            //when
            List<Schedule> upcomingScheduleList = scheduleRepository.upcomingScheduleList(user.getId());

            //then
            assertThat(upcomingScheduleList.size() < 7);

            for(Schedule schedule: upcomingScheduleList){
                assertThat(schedule.getScheduleStatus() == ScheduleStatus.NORMAL);
                assertThat(schedule.getEndAt().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN)));
            }
        }
    }

    private ScheduleParticipant generateScheduleParticipant(User user, Schedule schedule, ParticipantStatus status) {
        return ScheduleParticipant.builder()
                .user(user)
                .schedule(schedule)
                .status(status)
                .build();
    }

    private Schedule generateSchedule(User user, ScheduleStatus scheduleStatus, LocalDateTime startAt, LocalDateTime endAt, UploadFile uploadFile) {
        return Schedule.builder()
                .user(user)
                .location(location)
                .participantLimit(10L)
                .locationDetail("locationDetail")
                .title("title")
                .description("description")
                .scheduleStatus(scheduleStatus)
                .startAt(startAt)
                .endAt(endAt)
                .uploadFile(uploadFile)
                .viewCount(1L)
                .participateStartAt(startAt)
                .participateEndAt(endAt)
                .build();
    }

    private UploadFile generateUploadFile(){
        return UploadFile.builder()
                .contentId(1L)
                .contentType(ContentType.SCHEDULE)
                .fileUrl(createRandomUUID())
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