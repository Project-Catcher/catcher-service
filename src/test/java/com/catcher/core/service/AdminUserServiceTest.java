package com.catcher.core.service;

import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserStatusChangeHistory;
import com.catcher.core.domain.entity.enums.UserProvider;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.port.UserStatusChangeHistoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=local")
class AdminUserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private UserStatusChangeHistoryRepository userStatusChangeHistoryRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("startDate 부터 endDate 까지의 날짜를 key로 하고 각 날짜의 count을 value로 갖는 Map을 생성한다.")
    void countNewUsersPerDay() {
        // given
        LocalDate startDate = LocalDate.of(2023, 12, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // when
        final var resultMap = userRepository.countNewUsersPerDay(startDate, endDate);

        // then
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            assertTrue(resultMap.containsKey(date.toString()));
        }
    }

    @Test
    @DisplayName("블랙리스트 설정을 수행한다")
    @Transactional
    void changeUserStatusToBlackList() {
        // given
        User normalUser = getUserFixture(UserRole.USER);
        User adminUser = getUserFixture(UserRole.ADMIN);

        // when
        adminUserService.changeUserStatus(normalUser.getId(), adminUser.getId(), UserStatus.BLACKLISTED, "테스트 블랙리스트 설정");
        List<UserStatusChangeHistory> userStatusChangeHistory = userStatusChangeHistoryRepository.findAllByUserId(normalUser.getId());

        // then
        assertSame(normalUser.getStatus(), UserStatus.BLACKLISTED);
        assertFalse(userStatusChangeHistory.isEmpty());
    }

    /**
     * 다음과 같은 식으로 수행된다.
     * 1. 블랙리스트 설정
     * 2. 블랙리스트 해제
     *
     * userStatusChangeHistory는 다음과 같이 생성되어야 한다.
     * 1. (1, before: NORMAL, after: BLACKLISTED, parent: null, child: 2)
     * 2. (2, before: BLACKLISTED, after: NORMAL, parent: 1, child: null)
     */
    @Test
    @DisplayName("블랙리스트 해제를 수행한다")
    @Transactional
    void rollBackUserStatusToNormal() {
        // given
        User user = getUserFixture(UserRole.USER);
        User adminUser = getUserFixture(UserRole.ADMIN);

        // when
        adminUserService.changeUserStatus(user.getId(), adminUser.getId(), UserStatus.BLACKLISTED, "테스트 블랙리스트 설정");
        adminUserService.changeUserStatus(user.getId(), adminUser.getId(), UserStatus.NORMAL, "테스트 블랙리스트 해제");
        Optional<UserStatusChangeHistory> childUserStatusChangeHistoryOptional = userStatusChangeHistoryRepository.findFirstByUserAndAfterStatusOrderByIdDesc(user, UserStatus.NORMAL);
        Optional<UserStatusChangeHistory> parentUserStatusChangeHistoryOptional = userStatusChangeHistoryRepository.findFirstByUserAndAfterStatusOrderByIdDesc(user, UserStatus.BLACKLISTED);

        // then
        assertSame(user.getStatus(), UserStatus.NORMAL);

        assertEquals(childUserStatusChangeHistoryOptional.get().getParent(), parentUserStatusChangeHistoryOptional.get());
        assertNotEquals(childUserStatusChangeHistoryOptional.get().getParent(), childUserStatusChangeHistoryOptional.get());
        assertEquals(parentUserStatusChangeHistoryOptional.get().getChild(), childUserStatusChangeHistoryOptional.get());
        assertNotEquals(parentUserStatusChangeHistoryOptional.get().getChild(), parentUserStatusChangeHistoryOptional.get());
    }

    /**
     * 블랙리스트 이력은 블랙리스트를 설정한 리스트만 나와야 함
     */
    @Test
    @DisplayName("admin userDetail API 테스트")
    @Transactional
    void getUserDetail() {
        // given
        User user = getUserFixture(UserRole.USER);
        User adminUser = getUserFixture(UserRole.ADMIN);
        adminUserService.changeUserStatus(user.getId(), adminUser.getId(), UserStatus.BLACKLISTED, "테스트 블랙리스트 설정");
        adminUserService.changeUserStatus(user.getId(), adminUser.getId(), UserStatus.NORMAL, "테스트 블랙리스트 해제");

        em.flush();
        em.clear();
        user = userRepository.findById(user.getId()).get();

        // when
        final var userDetail = adminUserService.searchUserDetail(user.getId());

        // then
        assertEquals(userDetail.getEmail(), user.getEmail());
        assertEquals(userDetail.getNickname(), user.getNickname());
        assertEquals(userDetail.getStatus(), user.getStatus().name());
        assertEquals(userDetail.getBlackListHistoryList().size(), 1);
        assertEquals(userDetail.getBlackListHistoryList().get(0).getReason(), "테스트 블랙리스트 설정");
        assertEquals(userDetail.getBlackListHistoryList().get(0).getSetter(), user.getUsername());
    }

    private User getUserFixture(UserRole userRole) {
        return userRepository.save(User.builder()
                .username(UUID.randomUUID().toString())
                .phone(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .nickname(UUID.randomUUID().toString())
                .userAgeTerm(ZonedDateTime.now())
                .userServiceTerm(ZonedDateTime.now())
                .userPrivacyTerm(ZonedDateTime.now())
                .userProvider(UserProvider.CATCHER)
                .userRole(userRole)
                .status(UserStatus.NORMAL)
                .password("test")
                .build());
    }

}