package com.camper.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCamp is a Querydsl query type for Camp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCamp extends EntityPathBase<Camp> {

    private static final long serialVersionUID = -19768231L;

    public static final QCamp camp = new QCamp("camp");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    public final StringPath bNumber = createString("bNumber");

    public final StringPath campArea = createString("campArea");

    public final StringPath campInfo = createString("campInfo");

    public final StringPath campName = createString("campName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inTime = createString("inTime");

    public final StringPath outTime = createString("outTime");

    public final StringPath permission = createString("permission");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final StringPath surroundings = createString("surroundings");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QCamp(String variable) {
        super(Camp.class, forVariable(variable));
    }

    public QCamp(Path<? extends Camp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCamp(PathMetadata metadata) {
        super(Camp.class, metadata);
    }

}

