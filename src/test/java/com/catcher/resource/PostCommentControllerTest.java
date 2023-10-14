package com.catcher.resource;

import com.catcher.app.AppApplication;
import com.catcher.core.domain.entity.Comment;
import com.catcher.core.domain.request.PostCommentReplyRequest;
import com.catcher.core.domain.request.PostCommentRequest;
import com.catcher.core.domain.response.GetCommentsByPageResponse;
import com.catcher.core.service.CommentService;
import com.catcher.datasource.TestCommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AppApplication.class)
@AutoConfigureMockMvc
public class PostCommentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestCommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    public void testPostComment() throws Exception {

        // given
        Long userId = 1L;
        String contents = "댓글 작성 테스트";
        PostCommentRequest postCommentRequest = PostCommentRequest
                .builder()
                .userId(userId)
                .contents(contents)
                .build();

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCommentRequest))
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Comment savedComment = commentRepository.findFirstByUserIdAndContentsOrderByIdDesc(userId, contents);
        assertNotNull(savedComment);
        assertEquals(contents, savedComment.getContents());
    }

    @Test
    @Transactional
    public void testPostCommentReply() throws Exception {

        // given
        Long userId = 2L;
        String contents = "대댓글 작성 테스트";
        Comment parentComment = commentRepository.save(Comment
                .builder()
                .userId(1L)
                .contents("댓글 작성 테스트")
                .build());
        PostCommentReplyRequest postCommentReplyRequest = PostCommentReplyRequest
                .builder()
                .userId(userId)
                .parentId(parentComment.getId())
                .contents(contents)
                .build();

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/comment/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCommentReplyRequest))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Comment reply = commentRepository.findFirstByUserIdAndContentsOrderByIdDesc(userId, contents);
        assertNotNull(reply);
        assertEquals(contents, reply.getContents());
        assertEquals(parentComment.getId(), reply.getParent().getId());
    }

    @Test
    @Transactional
    public void 댓글목록조회_테스트_DB조회() {

        // given
        Comment parentComment1 = commentService.saveSingleComment(1L, "댓글 작성 테스트1");

        Comment parentComment2 = commentService.saveSingleComment(2L, "댓글 작성 테스트2");

        Comment childComment = commentService.saveSingleReply(parentComment1.getId(), 3L, "대댓글 작성 테스트");

        // when
        final var commentList = commentService
                .findByParentIsNull(PageRequest.of(0, 2, Sort.Direction.DESC, "id"))
                .stream().toList();

        Comment result1 = commentList.get(0);
        Comment result2 = commentList.get(1);

        // then
        assertEquals(result1.getId(), parentComment2.getId()); // 내림차순 정렬 확인
        assertEquals(result2.getReplies().get(0).getId(), childComment.getId()); // 대댓글 조회 확인
    }

    @Test
    public void 댓글목록조회_테스트_VO생성() {

        // given - 댓글 2개, 1번 댓글에 대댓글 1개, 해당 대댓글에 대대댓글 1개 추가 후 JPA 영속 상태 mocking
        Comment parentComment1 = new Comment(1L, null, new ArrayList<>(), 1L, "댓글 작성 테스트1");
        Comment parentComment2 = new Comment(2L, null, new ArrayList<>(), 1L, "댓글 작성 테스트2");
        Comment childComment = new Comment(3L, parentComment1, new ArrayList<>(), 2L, "대댓글 작성 테스트");
        Comment nestedChildComment = new Comment(4L, childComment, new ArrayList<>(), 3L, "대대댓글 작성 테스트");
        parentComment1.getReplies().add(childComment);
        childComment.getReplies().add(nestedChildComment);

        Page<Comment> parentCommentsPage = new PageImpl<>(List.of(parentComment1, parentComment2));

        // when
        List<GetCommentsByPageResponse> responseVoList = GetCommentsByPageResponse.createGetCommentsByPageResponseList(parentCommentsPage);
        GetCommentsByPageResponse result1 = responseVoList.get(0);
        GetCommentsByPageResponse result2 = responseVoList.get(1);
        GetCommentsByPageResponse childResult = result1.getChildComments().get(0);
        GetCommentsByPageResponse nestedChildResult = childResult.getChildComments().get(0);

        // then
        assertEquals(result1.getId(), parentComment1.getId());
        assertEquals(result2.getId(), parentComment2.getId());
        assertEquals(result2.getChildComments().size(), 0);
        assertEquals(childResult.getId(), childComment.getId());
        assertEquals(nestedChildResult.getId(), nestedChildComment.getId());
        assertEquals(nestedChildResult.getChildComments().size(), 0);

    }


}
