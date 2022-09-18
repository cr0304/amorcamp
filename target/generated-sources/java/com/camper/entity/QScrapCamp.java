package com.camper.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScrapCamp is a Querydsl query type for ScrapCamp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScrapCamp extends EntityPathBase<ScrapCamp> {

    private static final long serialVersionUID = 325085978L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScrapCamp scrapCamp = new QScrapCamp("scrapCamp");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final QCamp camp;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final QScrap scrap;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QScrapCamp(String variable) {
        this(ScrapCamp.class, forVariable(variable), INITS);
    }

    public QScrapCamp(Path<? extends ScrapCamp> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScrapCamp(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScrapCamp(PathMetadata metadata, PathInits inits) {
        this(ScrapCamp.class, metadata, inits);
    }

    public QScrapCamp(Class<? extends ScrapCamp> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.camp = inits.isInitialized("camp") ? new QCamp(forProperty("camp")) : null;
        this.scrap = inits.isInitialized("scrap") ? new QScrap(forProperty("scrap"), inits.get("scrap")) : null;
    }

}

