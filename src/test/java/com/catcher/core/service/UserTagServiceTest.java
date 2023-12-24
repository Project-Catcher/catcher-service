package com.catcher.core.service;

import com.catcher.AppApplication;
import com.catcher.core.database.UserTagRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.Tag;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserTag;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.dto.response.UserTagResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.catcher.core.domain.entity.enums.UserProvider.CATCHER;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = AppApplication.class)
@Transactional
@ActiveProfiles("test")
class UserTagServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTagService userTagService;
    @PersistenceContext
    EntityManager em;
    @Autowired
    UserTagRepository userTagRepository;

    User stubUser;

    @BeforeEach
    void beforeEach() {
        stubUser = createUser();
        flushAndClearPersistence();
    }

    @Test
    @DisplayName("유저 태그 삽입 요청시 정상적으로 삽입되어야 한다.")
    void insert_user_tag_should_exists_in_db() {
        // given
        int saveTagCount = 10;
        List<String> userTagNames = new ArrayList<>(saveTagCount);
        for (int i = 0; i < saveTagCount; i++) {
            userTagNames.add("new-tag-name" + i);
        }

        // when
        userTagService.updateTags(stubUser, userTagNames);
        flushAndClearPersistence();

        // then
        List<UserTag> savedTags = userTagRepository.findByUserId(stubUser.getId());
        List<String> savedTagNames = savedTags.stream()
                .map(UserTag::getTag)
                .map(Tag::getName)
                .toList();

        assertThat(savedTags.size()).isEqualTo(saveTagCount);
        assertThat(savedTagNames).contains(userTagNames.toArray(new String[0]));
    }

    @Test
    @DisplayName("유저 태그 삽입 요청시 기존 유저 태그들은 삭제되어야한다.")
    void inser_user_tag_then_return_only_new_tags() {
        // given
        int before = 10;
        List<String> oldTags = new ArrayList<>();
        for (int i = 0; i < before; i++) {
            oldTags.add("past-tag-" + i);
        }
        userTagService.updateTags(stubUser, oldTags);

        // when
        List<String> newTags = new ArrayList<>();
        int after = 10;
        for (int i = 0; i < after; i++) {
            newTags.add("new-tag-" + i);
        }
        userTagService.updateTags(stubUser, newTags);
        flushAndClearPersistence();

        // then
        UserTagResponse foundTags = userTagService.findTagsByUser(stubUser);

        assertContains(foundTags.getTags(), newTags);
        assertNotContains(foundTags.getTags(), oldTags);
    }

    private void assertContains(List<String> target, List<String> source) {
        assertThat(target).contains(source.toArray(new String[0]));
    }

    private void assertNotContains(List<String> target, List<String> source) {
        assertThat(target).doesNotContain(source.toArray(new String[0]));
    }

    private void flushAndClearPersistence() {
        em.flush();
        em.clear();
    }

    private User createUser() {
        User user = User.builder()
                .username(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .phone(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .profileImageUrl(null)
                .introduceContent(null)
                .nickname(UUID.randomUUID().toString())
                .userProvider(CATCHER)
                .userRole(UserRole.USER)
                .userAgeTerm(ZonedDateTime.now())
                .userServiceTerm(ZonedDateTime.now())
                .userPrivacyTerm(ZonedDateTime.now())
                .build();
        return userRepository.save(user);
    }

}