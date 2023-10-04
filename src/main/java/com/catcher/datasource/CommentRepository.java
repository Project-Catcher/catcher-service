package com.catcher.datasource;

import com.catcher.core.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // TODO: N+1 join
//    @Query(value = "SELECT DISTINCT c FROM Comment c LEFT OUTER JOIN FETCH c.replies WHERE c.parent IS NULL")
    Page<Comment> findByParentIsNull(Pageable pageable);

}
