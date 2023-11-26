package com.catcher.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCatcherItem is a Querydsl query type for CatcherItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCatcherItem extends EntityPathBase<CatcherItem> {

    private static final long serialVersionUID = -1062823650L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCatcherItem catcherItem = new QCatcherItem("catcherItem");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final QCategory category;

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.ZonedDateTime> deletedAt = createDateTime("deletedAt", java.time.ZonedDateTime.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.ZonedDateTime> endAt = createDateTime("endAt", java.time.ZonedDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemHashValue = createString("itemHashValue");

    public final QLocation location;

    public final StringPath resourceUrl = createString("resourceUrl");

    public final DateTimePath<java.time.ZonedDateTime> startAt = createDateTime("startAt", java.time.ZonedDateTime.class);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> updatedAt = _super.updatedAt;

    public QCatcherItem(String variable) {
        this(CatcherItem.class, forVariable(variable), INITS);
    }

    public QCatcherItem(Path<? extends CatcherItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCatcherItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCatcherItem(PathMetadata metadata, PathInits inits) {
        this(CatcherItem.class, metadata, inits);
    }

    public QCatcherItem(Class<? extends CatcherItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location"), inits.get("location")) : null;
    }

}

