package com.catcher.core.service;

import com.catcher.core.database.CommentReplyRepository;
import com.catcher.core.database.CommentRepository;
import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.CommentStatus;
import com.catcher.core.domain.entity.enums.LikeType;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.dto.request.SaveCommentRequest;
import com.catcher.core.dto.response.CommentListResponse;
import com.catcher.datasource.repository.LikeJpaRepository;
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

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.catcher.core.domain.entity.enums.UserProvider.CATCHER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CommentServiceTest {
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

    @Autowired
    LikeJpaRepository likeJpaRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentReplyRepository commentReplyRepository;

    @Autowired
    CommentService commentService;

    List<User> userList;
    List<Schedule> scheduleList;
    List<ScheduleParticipant> scheduleParticipantList;
    List<Comment> commentList;
    List<CommentReply> commentReplyList;
    List<Like> likeList;
    Location location = Location.initLocation("1111000000", "서울특별시 종로구");
    @BeforeEach
    void beforeEach() {
        userList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        scheduleParticipantList = new ArrayList<>();
        commentList = new ArrayList<>();
        commentReplyList = new ArrayList<>();
        likeList = new ArrayList<>();
        locationJpaRepository.save(location);

        for (int i = 0; i < 10; i++) {
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
                    LocalDateTime.now().plusDays(5),
                    LocalDateTime.now().plusDays(7)
            );

            scheduleList.add(schedule);
        }

        scheduleRepository.saveAll(scheduleList);

        Schedule schedule = scheduleList.get(0);
        for (User user : userList) {
            Comment comment = createComment(user, schedule);
            commentList.add(comment);
        }
        commentRepository.saveAll(commentList);


        Comment comment = commentList.get(0);
        for (User user : userList) {

            CommentReply commentReply = createCommentReply(user, comment);
            commentReplyList.add(commentReply);
        }
        commentReplyRepository.saveAll(commentReplyList);

        CommentReply commentReply = commentReplyList.get(0);
        for (User user : userList) {
            Like like = createLike(user, comment.getId(), LikeType.COMMENT);
            likeList.add(like);
            Like likeReply = createLike(user, commentReply.getId(), LikeType.COMMENTREPLY);
            likeList.add(likeReply);
        }
        likeJpaRepository.saveAll(likeList);

        flushAndClearPersistence();
    }

    @DisplayName("댓글 등록")
    @Test
    void save_comment() {
        //given
        Schedule schedule = scheduleList.get(0);
        User user = createUser();
        userRepository.save(user);

        SaveCommentRequest saveCommentRequest = SaveCommentRequest.builder()
                .scheduleId(schedule.getId())
                .content(createRandomUUID())
                .isSecret(false)
                .build();

        //when
        CommentListResponse commentListResponse = commentService.saveCommentOrCommentReply(user, saveCommentRequest);
        flushAndClearPersistence();

        //then
        assertThat(commentListResponse.getCommentDtoList()).isNotEmpty();
        assertThat(commentListResponse.getCommentDtoList().stream()
                .filter(commentDto -> commentDto.getUserId().equals(user.getId()))
                .findAny()).isPresent();

    }

    @DisplayName("대댓글 등록")
    @Test
    void save_comment_reply() {
        //given
        Schedule schedule = scheduleList.get(0);
        User user = createUser();
        userRepository.save(user);

        Comment comment = commentList.get(0);

        SaveCommentRequest saveCommentRequest = SaveCommentRequest.builder()
                .scheduleId(schedule.getId())
                .commentId(comment.getId())
                .content(createRandomUUID())
                .isSecret(false)
                .build();

        //when
        CommentListResponse commentListResponse = commentService.saveCommentOrCommentReply(user, saveCommentRequest);
        flushAndClearPersistence();

        //then
        List<CommentListResponse.CommentReplyDto> commentReplyDtoList = commentListResponse.getCommentDtoList().stream()
                .filter(commentDto -> commentDto.getId().equals(comment.getId()))
                .findFirst()
                .orElseThrow()
                .getCommentReplyList();
        assertThat(commentListResponse.getCommentDtoList()).isNotEmpty();
        assertThat(commentReplyDtoList.stream()
                .filter(commentReplyDto -> commentReplyDto.getUserId().equals(user.getId()))
                .findAny()).isPresent();

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

    private Comment createComment(User user, Schedule schedule) {
        return Comment.builder()
                .user(user)
                .schedule(schedule)
                .content(createRandomUUID())
                .status(CommentStatus.NORMAL)
                .build();
    }

    private CommentReply createCommentReply(User user, Comment comment) {
        return CommentReply.builder()
                .user(user)
                .comment(comment)
                .content(createRandomUUID())
                .status(CommentStatus.NORMAL)
                .build();
    }

    private Like createLike(User user, Long targetId, LikeType type) {
        return Like.builder()
                .user(user)
                .targetId(targetId)
                .type(type)
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