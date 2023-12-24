package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplyJpaRepository extends JpaRepository<CommentReply, Long> {

}
