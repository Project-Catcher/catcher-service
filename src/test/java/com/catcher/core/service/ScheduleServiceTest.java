package com.catcher.core.service;

import com.catcher.AppApplication;
import com.catcher.common.exception.BaseException;
import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.database.ScheduleTagRepository;
import com.catcher.core.database.TagRepository;
import com.catcher.core.database.UserItemRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.PublicStatus;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.dto.request.SaveDraftScheduleRequest;
import com.catcher.core.dto.request.SaveScheduleSkeletonRequest;
import com.catcher.core.dto.request.SaveUserItemRequest;
import com.catcher.core.dto.response.*;
import com.catcher.core.dto.response.DraftScheduleResponse;
import com.catcher.core.dto.response.RecommendedTagResponse;
import com.catcher.core.dto.response.SaveScheduleSkeletonResponse;
import com.catcher.core.dto.response.SaveUserItemResponse;
import com.catcher.datasource.repository.CategoryJpaRepository;
import com.catcher.datasource.repository.LocationJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.catcher.core.domain.entity.enums.UserProvider.CATCHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {AppApplication.class}, properties = "spring.profiles.active=local")
@Transactional
class ScheduleServiceTest {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserItemRepository userItemRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ScheduleTagRepository scheduleTagRepository;

    @Autowired
    CategoryJpaRepository categoryJpaRepository;

    @Autowired
    LocationJpaRepository locationJpaRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ScheduleService scheduleService;

    Location location = Location.initLocation("areacode", "description");

    @BeforeEach
    void beforeEach() {
        locationJpaRepository.save(location);
        flushAndClearPersistence();
    }

    @DisplayName("SUCCESS: 임시 저장된 일정 조회")
    @Test
    void get_draft_schedule() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);

        Schedule draftSchedule = createSchedule(user, location, ScheduleStatus.DRAFT);
        Schedule savedSchedule = createSchedule(user, location, ScheduleStatus.NORMAL);

        scheduleRepository.save(draftSchedule);
        scheduleRepository.save(savedSchedule);
        flushAndClearPersistence();

        // When
        DraftScheduleResponse result = scheduleService.getDraftSchedule(user);

        // Then
        assertEquals(1, result.getSchedules().size());
        assertEquals(draftSchedule.getTitle(), result.getSchedules().get(0).getTitle());
    }

    @DisplayName("SUCCESS: 임시 저장된 일정 조회 시, 임시 저장된 일정이 없을 경우")
    @Test
    void get_none_draft_schedule() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);
        flushAndClearPersistence();

        // When
        DraftScheduleResponse result = scheduleService.getDraftSchedule(user);

        // Then
        assertEquals(0, result.getSchedules().size());
    }

    @DisplayName("SUCCESS: 일정 기본 정보 저장")
    @Test
    void save_schedule_skeleton() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);

        SaveScheduleSkeletonRequest request = SaveScheduleSkeletonRequest.builder()
                .title("제목")
                .thumbnail("image.png")
                .location("서울특별시")
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .build();

        // When
        SaveScheduleSkeletonResponse result = scheduleService.saveScheduleSkeleton(request, user);
        flushAndClearPersistence();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();

        List<Schedule> savedSchedules = scheduleRepository.findByUserAndStatus(user, ScheduleStatus.DRAFT);
        assertEquals(1, savedSchedules.size());

        Schedule savedSchedule = savedSchedules.get(0);
        assertThat(savedSchedule.getTitle()).isEqualTo(request.getTitle());
        assertThat(savedSchedule.getThumbnailUrl()).isEqualTo(request.getThumbnail());
    }

    @DisplayName("SUCCESS: 일정 임시 저장")
    @Test
    void save_draft_schedule() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);

        Schedule schedule = createSchedule(user, location, ScheduleStatus.DRAFT);
        scheduleRepository.save(schedule);

        List<String> tagList = List.of("친구와", "나홀로");
        SaveDraftScheduleRequest request = createDraftScheduleRequest(tagList);

        // When
        scheduleService.saveDraftSchedule(request, schedule.getId(), user);
        flushAndClearPersistence();

        // Then
        assertThat(schedule.getTitle()).isEqualTo(request.getTitle());
        assertEquals(scheduleTagRepository.findBySchedule(schedule).size(), tagList.size());
        assertEquals(scheduleTagRepository.findBySchedule(schedule).get(0).getTag().getName(), tagList.get(0));
        assertEquals(scheduleTagRepository.findBySchedule(schedule).get(1).getTag().getName(), tagList.get(1));
    }

    @DisplayName("FAIL: 임시 저장 시, 유효한 일정이 아닐 경우")
    @Test
    void invalid_save_draft_schedule() {
        // Given
        User owner = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        User reader = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(owner);
        userRepository.save(reader);

        Schedule schedule = createSchedule(owner, location, ScheduleStatus.DRAFT);
        scheduleRepository.save(schedule);

        List<String> tagList = List.of("친구와", "나홀로");
        SaveDraftScheduleRequest request = createDraftScheduleRequest(tagList);

        // When, Then
        assertThatThrownBy(() -> scheduleService.saveDraftSchedule(request, schedule.getId(), reader))
                .isInstanceOf(BaseException.class);
    }

    @DisplayName("SUCCESS: 나만의 아이템 저장")
    @Test
    void save_user_item() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);

        SaveUserItemRequest request = createUserItemRequest("나만의 맛집", "restaurant");

        // When
        SaveUserItemResponse result = scheduleService.saveUserItem(user, request);
        flushAndClearPersistence();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(userItemRepository.findById(result.getId())).isPresent();
    }

    @DisplayName("FAIL: 나만의 아이템 저장 시, 유효한 카테고리가 아닐 경우")
    @Test
    void invalid_category_save_user_item() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);

        SaveUserItemRequest request = createUserItemRequest("기타 아이템","etc");

        // When, Then
        assertThatThrownBy(() -> scheduleService.saveUserItem(user, request))
                .isInstanceOf(BaseException.class);
    }

    @DisplayName("SUCCESS: 추천 태그 조회")
    @Test
    void get_tags() {
        // Given
        Tag tag = createTag("나홀로", RecommendedStatus.DISABLED);
        Tag recommendedTag = createTag("친구와", RecommendedStatus.ENABLED);

        tagRepository.save(tag);
        tagRepository.save(recommendedTag);
        flushAndClearPersistence();

        // When
        RecommendedTagResponse result = scheduleService.getRecommendedTags();

        // Then
        assertEquals(1, result.getTags().size());
        assertEquals(recommendedTag.getName(), result.getTags().get(0).getName());
    }

    @DisplayName("SUCCESS: 추천 태그로 설정된 태그가 없을 경우")
    @Test
    void get_none_tags() {
        // Given
        Tag tag = createTag("나홀로", RecommendedStatus.DISABLED);

        tagRepository.save(tag);
        flushAndClearPersistence();

        // When
        RecommendedTagResponse result = scheduleService.getRecommendedTags();

        // Then
        assertEquals(0, result.getTags().size());
    }

    @DisplayName("SUCCESS: 나만의 아이템 목록 조회")
    @Test
    void get_user_items() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);

        Category category = Category.create("restaurant");
        categoryJpaRepository.save(category);

        UserItem userItem1 = createUserItem(user, category, createRandomUUID());
        UserItem userItem2 = createUserItem(user, category, createRandomUUID());

        userItemRepository.save(userItem1);
        userItemRepository.save(userItem2);
        flushAndClearPersistence();

        // When
        GetUserItemResponse result = scheduleService.getUserItems(user);

        // Then
        assertEquals(2, result.getItems().size());
        assertEquals(userItem1.getCategory().getName(), result.getItems().get(0).getCategory());
        assertEquals(userItem2.getCategory().getName(), result.getItems().get(1).getCategory());
    }

    @DisplayName("SUCCESS: 아이템이 없을 경우, 나만의 아이템 목록 조회")
    @Test
    void get_none_user_items() {
        // Given
        User user = createUser(createRandomUUID(), createRandomUUID(), createRandomUUID(), createRandomUUID());
        userRepository.save(user);

        // When
        GetUserItemResponse result = scheduleService.getUserItems(user);

        // Then
        assertEquals(0, result.getItems().size());
    }

    private User createUser(String name, String phone, String email, String nickname) {
        return User.builder()
                .username(name)
                .password("1234")
                .phone(phone)
                .email(email)
                .profileImageUrl(null)
                .introduceContent(null)
                .nickname(nickname)
                .userProvider(CATCHER)
                .userRole(UserRole.USER)
                .userAgeTerm(ZonedDateTime.now())
                .userServiceTerm(ZonedDateTime.now())
                .userPrivacyTerm(ZonedDateTime.now())
                .build();
    }

    private static Schedule createSchedule(User user, Location location, ScheduleStatus scheduleStatus) {
        return Schedule.builder()
                .user(user)
                .title("title")
                .viewCount(0L)
                .thumbnailUrl("image.jpg")
                .location(location)
                .scheduleStatus(scheduleStatus)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .build();
    }

    private static SaveDraftScheduleRequest createDraftScheduleRequest(List<String> tagList) {
        return SaveDraftScheduleRequest.builder()
                .title("제목")
                .thumbnail("image.png")
                .location("서울특별시")
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .tags(tagList)
                .isPublic(PublicStatus.PUBLIC)
                .participant(0L)
                .budget(0L)
                .participateEndAt(LocalDateTime.now())
                .participateStartAt(LocalDateTime.now())
                .build();
    }

    private static SaveUserItemRequest createUserItemRequest(String title, String category) {
        return SaveUserItemRequest.builder()
                .title(title)
                .category(category)
                .location("서울특별시 종로구")
                .build();
    }

    private static Tag createTag(String name, RecommendedStatus disabled) {
        return Tag.builder()
                .name(name)
                .recommendedStatus(disabled)
                .build();
    }

    private static UserItem createUserItem(User user, Category category, String title) {
        return UserItem.builder()
                .user(user)
                .category(category)
                .title(title)
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