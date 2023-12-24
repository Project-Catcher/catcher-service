package com.catcher.datasource;

import com.catcher.core.database.CommentReplyRepository;
import com.catcher.core.domain.entity.CommentReply;
import com.catcher.core.domain.entity.QLike;
import com.catcher.core.domain.entity.enums.LikeType;
import com.catcher.core.dto.CommentReplyWithLikesDto;
import com.catcher.datasource.repository.CommentReplyJpaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.catcher.core.domain.entity.QCommentReply.commentReply;
import static com.catcher.core.domain.entity.QLike.like;
import static com.catcher.core.domain.entity.QUser.user;
import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class CommentReplyRepositoryImpl implements CommentReplyRepository {
    private final JPAQueryFactory queryFactory;
    private final CommentReplyJpaRepository commentReplyJpaRepository;

    @Override
    public Optional<CommentReply> findById(Long commentId) {
        return commentReplyJpaRepository.findById(commentId);
    }

    @Override
    public CommentReply save(CommentReply commentReply) {
        return commentReplyJpaRepository.save(commentReply);
    }

    @Override
    public List<CommentReplyWithLikesDto> findAllWithLikeCountAndLikeStatusByScheduleIdAndUserID(Long scheduleId, Long userId) {
        QLike subLike = new QLike("subLike");

        return queryFactory.select(Projections.constructor(CommentReplyWithLikesDto.class,
                commentReply,
                JPAExpressions.select(count(subLike.id))
                        .from(subLike)
                        .where(subLike.type.eq(LikeType.COMMENTREPLY)
                                .and(subLike.targetId.eq(commentReply.id))),
                like.id.when(Expressions.nullExpression()).then(Expressions.FALSE).otherwise(Expressions.TRUE)
                ))
                .from(commentReply)
                .leftJoin(like)
                .on(like.type.eq(LikeType.COMMENTREPLY)
                        .and(like.targetId.eq(commentReply.id))
                        .and(like.user.id.eq(userId)))
                .leftJoin(commentReply.user, user).fetchJoin()
                .where(commentReply.comment.schedule.id.eq(scheduleId))
                .fetch();
    }

    @Override
    public List<CommentReply> saveAll(List<CommentReply> commentReplyList) {
        return commentReplyJpaRepository.saveAll(commentReplyList);
    }
}
