package com.catcher.datasource;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.datasource.repository.LocationJpaRepository;
import com.catcher.datasource.repository.ScheduleParticipantJpaRepository;
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
    ScheduleParticipantJpaRepository scheduleParticipantJpaRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationJpaRepository locationJpaRepository;

    List<User> userList;
    List<Schedule> scheduleList;
    List<ScheduleParticipant> scheduleParticipantList;
    Location location = Location.initLocation("1111000000", "서울특별시 종로구");

    private boolean shouldSkipSetup = false;

    @BeforeEach
    void beforeEach() {
        userList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        scheduleParticipantList = new ArrayList<>();
        locationJpaRepository.save(location);

        for (int i = 0; i < 10; i++) {
            User user = createUser();
            userList.add(user);
        }
        userRepository.saveAll(userList);

        if (!shouldSkipSetup) {
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
        }
        flushAndClearPersistence();
    }

    @DisplayName("다가오는 일정이 여러개일 때, 조건에 맞는 상위 7개만 반환한다")
    @Test
    void upcomingScheduleList_will_return_satisfied_top_7() {
        //given
        for (Schedule schedule : scheduleList) {
            ScheduleParticipant scheduleParticipant = generateScheduleParticipant(userList.get(0), schedule, ParticipantStatus.APPROVE);

            scheduleParticipantList.add(scheduleParticipant);
        }
        scheduleParticipantJpaRepository.saveAll(scheduleParticipantList);

        //when
        List<Schedule> upcomingScheduleList = scheduleRepository.upcomingScheduleList(userList.get(0).getId());

        //then
        assertThat(upcomingScheduleList.size()).isLessThanOrEqualTo(7);

        for (Schedule schedule : upcomingScheduleList) {
            assertThat(schedule.getScheduleStatus()).isEqualTo(ScheduleStatus.NORMAL);
            assertThat(schedule.getEndAt()).isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        }
    }

    @DisplayName("다가오는 일정이 없는 경우 빈 list를 반환한다")
    @Test
    void empty_upcoming_schedule_list() {
        //given

        //when
        List<Schedule> upcomingScheduleList = scheduleRepository.upcomingScheduleList(userList.get(0).getId());

        //then
        assertThat(upcomingScheduleList.size()).isEqualTo(0);
    }

    @DisplayName("작성 중인 일정이 여러개가 있을 때, 저장일 기준 최신 7개만 가져온다")
    @Test
    void draftScheduleList_top_7() {
        //given
        List<Schedule> draftScheduleList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Schedule schedule = generateSchedule(
                    userList.get(0),
                    ScheduleStatus.DRAFT,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            draftScheduleList.add(schedule);
        }
        Schedule currentSchedule = generateSchedule(
                userList.get(0),
                ScheduleStatus.DRAFT,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        draftScheduleList.add(currentSchedule);
        scheduleRepository.saveAll(draftScheduleList);

        //when
        List<Schedule> draftScheduleListOrderByCreatedAtDesc = scheduleRepository.draftScheduleList(userList.get(0).getId());

        //then
        assertThat(draftScheduleListOrderByCreatedAtDesc.size()).isLessThanOrEqualTo(7);
        assertThat(draftScheduleListOrderByCreatedAtDesc.get(0).getCreatedAt()).isAfter(draftScheduleListOrderByCreatedAtDesc.get(1).getCreatedAt());

        for (Schedule schedule : draftScheduleList) {
            assertThat(schedule.getScheduleStatus()).isEqualTo(ScheduleStatus.DRAFT);
        }
    }

    @DisplayName("작성 중인 일정이 없는 경우 빈 list를 반환한다")
    @Test
    void empty_draft_schedule_list() {
        //given

        //when
        List<Schedule> draftScheduleList = scheduleRepository.draftScheduleList(userList.get(0).getId());

        //then
        assertThat(draftScheduleList.size()).isEqualTo(0);
    }

    @DisplayName("모집중인 글이 7개 초과인 경우, 7개만 보여준다")
    @Test
    void open_schedule_over_7_returns_7_schedule() {
        //given
        List<Schedule> normalScheduleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Schedule schedule = generateSchedule(
                    userList.get(0),
                    ScheduleStatus.NORMAL,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            normalScheduleList.add(schedule);
        }

        scheduleRepository.saveAll(normalScheduleList);

        //when
        List<Schedule> openScheduleList = scheduleRepository.openScheduleList();

        //then
        assertThat(openScheduleList.size()).isLessThanOrEqualTo(7);

        for (Schedule schedule : openScheduleList) {
            assertThat(schedule.getScheduleStatus()).isEqualTo(ScheduleStatus.NORMAL);
            assertThat(schedule.getEndAt()).isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        }
    }

    @DisplayName("일정 10개중에 5개가 꽉찼다면, 5개만 반환한다")
    @Test
    void open_schedule_10_and_5_is_full_then_returns_not_full_5_schedule() {
        //given
        setShouldSkipSetup(true);

        //정상 일정 만들기
        List<Schedule> normalScheduleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Schedule schedule = generateSchedule(
                    userList.get(0),
                    ScheduleStatus.NORMAL,
                    LocalDateTime.now().minusDays(1L),
                    LocalDateTime.now().plusDays(1L)
            );
            normalScheduleList.add(schedule);
        }

        scheduleRepository.saveAll(normalScheduleList);

        //5개만 사람 꽉 채우기
        for (int i = 0; i < 5; i++) {
            for (int cnt = 0; cnt < 5; cnt++) {
                ScheduleParticipant scheduleParticipant = generateScheduleParticipant(userList.get(cnt), normalScheduleList.get(i), ParticipantStatus.APPROVE);
                scheduleParticipantList.add(scheduleParticipant);
            }
        }
        scheduleParticipantJpaRepository.saveAll(scheduleParticipantList);

        //when
        List<Schedule> openScheduleList = scheduleRepository.openScheduleList();

        //then
        assertThat(openScheduleList.size()).isEqualTo(5);

        for (Schedule schedule : openScheduleList) {
            assertThat(schedule.getScheduleStatus()).isEqualTo(ScheduleStatus.NORMAL);
            assertThat(schedule.getEndAt()).isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        }
    }

    @DisplayName("일정에 빈자리가 1개일 때, 1명이 신청해서 승인 대기중이면 목록에 뜬다")
    @Test
    void schedule_having_1_place_left_and_1_participant_pending_then_open_schedule_contains_the_schedule() {
        //given
        setShouldSkipSetup(true);

        Schedule normalSchedule = generateSchedule(
                userList.get(0),
                ScheduleStatus.NORMAL,
                LocalDateTime.now().minusDays(1L),
                LocalDateTime.now().plusDays(1L)
        );
        scheduleRepository.save(normalSchedule);

        for (int cnt = 0; cnt < 4; cnt++) {
            ScheduleParticipant scheduleParticipant = generateScheduleParticipant(userList.get(cnt), normalSchedule, ParticipantStatus.APPROVE);
            scheduleParticipantList.add(scheduleParticipant);
        }
        ScheduleParticipant scheduleParticipant = generateScheduleParticipant(userList.get(4), normalSchedule, ParticipantStatus.PENDING);
        scheduleParticipantList.add(scheduleParticipant);
        scheduleParticipantJpaRepository.saveAll(scheduleParticipantList);

        //when
        List<Schedule> openScheduleList = scheduleRepository.openScheduleList();

        //then
        assertThat(openScheduleList).contains(normalSchedule);
    }

    @DisplayName("일정에 빈자리가 1개일 때, 1명이 신청해서 승인되면 목록에 안 뜬다")
    @Test
    void schedule_having_1_place_left_and_1_participant_approved_then_open_schedule_does_not_contain_the_schedule() {
        //given
        setShouldSkipSetup(true);

        Schedule normalSchedule = generateSchedule(
                userList.get(0),
                ScheduleStatus.NORMAL,
                LocalDateTime.now().minusDays(1L),
                LocalDateTime.now().plusDays(1L)
        );
        scheduleRepository.save(normalSchedule);

        for (int cnt = 0; cnt < 4; cnt++) {
            ScheduleParticipant scheduleParticipant = generateScheduleParticipant(userList.get(cnt), normalSchedule, ParticipantStatus.APPROVE);
            scheduleParticipantList.add(scheduleParticipant);
        }
        ScheduleParticipant scheduleParticipant = generateScheduleParticipant(userList.get(4), normalSchedule, ParticipantStatus.PENDING);
        scheduleParticipantList.add(scheduleParticipant);
        scheduleParticipantJpaRepository.saveAll(scheduleParticipantList);

        //when
        List<Schedule> firstOpenScheduleList = scheduleRepository.openScheduleList();

        //then
        assertThat(firstOpenScheduleList).contains(normalSchedule);

        //when
        scheduleParticipant = generateScheduleParticipant(userList.get(4), normalSchedule, ParticipantStatus.APPROVE);
        scheduleParticipantJpaRepository.save(scheduleParticipant);

        List<Schedule> secondOpenScheduleList = scheduleRepository.openScheduleList();

        //then
        assertThat(secondOpenScheduleList).doesNotContain(normalSchedule);
        assertThat(secondOpenScheduleList.size()).isEqualTo(0);
        assertThat(firstOpenScheduleList).isNotSameAs(secondOpenScheduleList);
    }

    @DisplayName("모집 중인 일정이 없는 경우 빈 list를 반환한다")
    @Test
    void empty_open_schedule_list() {
        //given
        //일정을 등록하지 않는다
        setShouldSkipSetup(true);

        //when
        List<Schedule> openScheduleList = scheduleRepository.openScheduleList();

        //then
        assertThat(openScheduleList.size()).isEqualTo(0);
    }


    @DisplayName("참여 신청한 스케줄이 7개가 넘는다면 7개만 반환한다")
    @Test
    void applied_schedule_over_7_returns_only_7_schedule_list() {
        //given
        List<Schedule> normalScheduleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Schedule schedule = generateSchedule(
                    userList.get(0),
                    ScheduleStatus.NORMAL,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            normalScheduleList.add(schedule);
        }

        scheduleRepository.saveAll(normalScheduleList);

        for (Schedule schedule : normalScheduleList) {
            ScheduleParticipant scheduleParticipant = generateScheduleParticipant(userList.get(0), schedule, ParticipantStatus.APPROVE);
            scheduleParticipantList.add(scheduleParticipant);
        }
        scheduleParticipantJpaRepository.saveAll(scheduleParticipantList);

        //when
        List<Schedule> appliedList = scheduleRepository.appliedScheduleList(userList.get(0).getId());

        //then
        assertThat(appliedList.size()).isLessThanOrEqualTo(7);

        for (Schedule schedule : appliedList) {
            assertThat(schedule.getScheduleStatus()).isEqualTo(ScheduleStatus.NORMAL);
            assertThat(schedule.getEndAt()).isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        }
    }

    @DisplayName("모집 중인 일정이 없는 경우 빈 list를 반환한다")
    @Test
    void empty_applied_schedule_list() {
        //given

        //when
        List<Schedule> openScheduleList = scheduleRepository.appliedScheduleList(userList.get(0).getId());

        //then
        assertThat(openScheduleList.size()).isEqualTo(0);
    }

    private void setShouldSkipSetup(boolean shouldSkipSetup) {
        this.shouldSkipSetup = shouldSkipSetup;
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