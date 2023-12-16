package com.catcher.datasource;

import com.catcher.core.database.LocationRepository;
import com.catcher.core.database.ScheduleParticipantRepository;
import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.database.UploadFileRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ContentType;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.domain.entity.enums.UserRole;
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
    ScheduleParticipantRepository scheduleParticipantRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UploadFileRepository uploadFileRepository;

    @Autowired
    LocationRepository locationRepository;

    List<User> userList;
    List<Schedule> scheduleList;
    List<ScheduleParticipant> scheduleParticipantList;
    Location location = Location.initLocation("1111000000", "서울특별시 종로구");

    @BeforeEach
    void beforeEach() {
        userList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        scheduleParticipantList = new ArrayList<>();
        locationRepository.save(location);

        for (int i = 0; i < 10; i++) {
            User user = createUser();
            userList.add(user);
        }
        userRepository.saveAll(userList);

        for (User user : userList) {
            ScheduleStatus[] statusValues = ScheduleStatus.values();
            Random random = new Random();
            UploadFile uploadFile = generateUploadFile();
            uploadFileRepository.save(uploadFile);

            Schedule schedule = generateSchedule(
                    user,
                    statusValues[random.nextInt(statusValues.length)],
                    LocalDateTime.now().plus(5, ChronoUnit.DAYS),
                    LocalDateTime.now().plus(7, ChronoUnit.DAYS),
                    uploadFile
            );

            scheduleList.add(schedule);
        }

        scheduleRepository.saveAll(scheduleList);

        flushAndClearPersistence();
    }

    @DisplayName("다가오는 일정이 여러개일 때, 조건에 맞는 상위 7개만 반환한다")
    @Test
    void upcomingScheduleList_will_return_satisfied_top_7() {
        //given
        makeParticipant(null);

        for (User user : userList) {
            //when
            List<Schedule> upcomingScheduleList = scheduleRepository.upcomingScheduleList(user.getId());

            //then
            assertThat(upcomingScheduleList.size() <= 7);

            for(Schedule schedule: upcomingScheduleList){
                assertThat(schedule.getScheduleStatus() == ScheduleStatus.NORMAL);
                assertThat(schedule.getEndAt().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN)));
            }
        }
    }

    private void makeParticipant(ParticipantStatus fixedStatus){
        for (User user : userList) {
            ParticipantStatus[] participantStatus = ParticipantStatus.values();
            Random random = new Random();
            ScheduleParticipant scheduleParticipant = generateScheduleParticipant(user, scheduleList.get(0), fixedStatus == null ? participantStatus[random.nextInt(participantStatus.length)] : fixedStatus);

            scheduleParticipantList.add(scheduleParticipant);
        }
        scheduleParticipantRepository.saveAll(scheduleParticipantList);

        flushAndClearPersistence();
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