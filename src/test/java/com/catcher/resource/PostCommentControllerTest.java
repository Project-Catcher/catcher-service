package com.catcher.resource;

import com.catcher.app.AppApplication;
import com.catcher.datasource.TestCommentRepository;
import com.catcher.core.domain.entity.Comment;
import com.catcher.core.domain.request.PostCommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    public void testPostCommand() throws Exception {

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
}
