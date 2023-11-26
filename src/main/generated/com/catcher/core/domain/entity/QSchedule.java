package com.catcher.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = 1573342260L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Long> budget = createNumber("budget", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.ZonedDateTime> endAt = createDateTime("endAt", java.time.ZonedDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QLocation location;

    public final StringPath locationDetail = createString("locationDetail");

    public final NumberPath<Long> participantLimit = createNumber("participantLimit", Long.class);

    public final DateTimePath<java.time.ZonedDateTime> participateEndAt = createDateTime("participateEndAt", java.time.ZonedDateTime.class);

    public final DateTimePath<java.time.ZonedDateTime> participateStartAt = createDateTime("participateStartAt", java.time.ZonedDateTime.class);

    public final DateTimePath<java.time.ZonedDateTime> startAt = createDateTime("startAt", java.time.ZonedDateTime.class);

    public final EnumPath<com.catcher.core.domain.entity.enums.ScheduleStatus> status = createEnum("status", com.catcher.core.domain.entity.enums.ScheduleStatus.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> updatedAt = _super.updatedAt;

    public final QUploadFile uploadFile;

    public final QUser user;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location"), inits.get("location")) : null;
        this.uploadFile = inits.isInitialized("uploadFile") ? new QUploadFile(forProperty("uploadFile")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

