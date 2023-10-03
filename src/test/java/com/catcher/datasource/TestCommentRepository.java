package com.catcher.datasource;

import com.catcher.core.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

// TODO: 프로덕션 코드에는 없는 기능이 필요한 경우?
public interface TestCommentRepository extends JpaRepository<Comment, Long> {

    Comment findFirstByContentsOrderByIdDesc(String contents);

}
