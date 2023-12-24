package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

}
