package com.catcher.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1283914728L;

    public static final QUser user = new QUser("user");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.ZonedDateTime> deletedAt = createDateTime("deletedAt", java.time.ZonedDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduceContent = createString("introduceContent");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> updatedAt = _super.updatedAt;

    public final DateTimePath<java.time.ZonedDateTime> userAgeTerm = createDateTime("userAgeTerm", java.time.ZonedDateTime.class);

    public final DateTimePath<java.time.ZonedDateTime> userLocationTerm = createDateTime("userLocationTerm", java.time.ZonedDateTime.class);

    public final DateTimePath<java.time.ZonedDateTime> userMarketingTerm = createDateTime("userMarketingTerm", java.time.ZonedDateTime.class);

    public final StringPath username = createString("username");

    public final DateTimePath<java.time.ZonedDateTime> userPrivacyTerm = createDateTime("userPrivacyTerm", java.time.ZonedDateTime.class);

    public final EnumPath<com.catcher.core.domain.entity.enums.UserProvider> userProvider = createEnum("userProvider", com.catcher.core.domain.entity.enums.UserProvider.class);

    public final EnumPath<com.catcher.core.domain.entity.enums.UserRole> userRole = createEnum("userRole", com.catcher.core.domain.entity.enums.UserRole.class);

    public final DateTimePath<java.time.ZonedDateTime> userServiceTerm = createDateTime("userServiceTerm", java.time.ZonedDateTime.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

