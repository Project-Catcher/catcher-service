package com.catcher.datasource;

import com.catcher.core.database.CommentRepository;
import com.catcher.core.domain.entity.Comment;
import com.catcher.core.domain.entity.QLike;
import com.catcher.core.domain.entity.enums.LikeType;
import com.catcher.core.dto.CommentWithLikesDto;
import com.catcher.datasource.repository.CommentJpaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.catcher.core.domain.entity.QComment.comment;
import static com.catcher.core.domain.entity.QLike.like;
import static com.catcher.core.domain.entity.QUser.user;
import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final JPAQueryFactory queryFactory;
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentJpaRepository.findById(commentId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public List<Comment> saveAll(List<Comment> commentList) {
        return commentJpaRepository.saveAll(commentList);
    }

    @Override
    public List<CommentWithLikesDto> findAllWithLikeCountAndLikeStatusByScheduleIdAndUserID(Long scheduleId, Long userId) {
        QLike subLike = new QLike("subLike");

        return queryFactory.select(Projections.constructor(CommentWithLikesDto.class,
                        comment,
                        JPAExpressions.select(count(subLike.id))
                                .from(subLike)
                                .where(subLike.type.eq(LikeType.COMMENT)
                                        .and(subLike.targetId.eq(comment.id))),
                        like.id.when(Expressions.nullExpression()).then(Expressions.FALSE).otherwise(Expressions.TRUE)
                ))
                .from(comment)
                .leftJoin(like)
                .on(like.type.eq(LikeType.COMMENT)
                        .and(like.targetId.eq(comment.id))
                        .and(like.user.id.eq(userId)))
                .leftJoin(comment.user, user).fetchJoin()
                .where(comment.schedule.id.eq(scheduleId))
                .fetch();
    }
}
