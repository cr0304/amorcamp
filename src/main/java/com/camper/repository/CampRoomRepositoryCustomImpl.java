package com.camper.repository;

import com.camper.dto.CampSearchDto;
import com.camper.dto.QCampSearchDto;
import com.camper.dto.SearchDto;
import com.camper.entity.QCamp;
import com.camper.entity.QCampImg;
import com.camper.entity.Room;
import com.camper.service.RoomService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class CampRoomRepositoryCustomImpl implements CampRoomRepositoryCustom{

    private JPAQueryFactory queryFactory; // 동적 쿼리 사용하기 위해

    private RoomService roomService;

    public CampRoomRepositoryCustomImpl(EntityManager em, RoomService roomService){
        this.roomService = roomService;
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QCamp.camp.campName.like("%" + searchQuery + "%").or(QCamp.camp.campArea.like("%" + searchQuery + "%"));
    }

    private BooleanExpression searchPermission(String Permission){
        return StringUtils.isEmpty(Permission) ? null : QCamp.camp.permission.like("%" + Permission + "%");
    }


    @Override
    public Page<CampSearchDto> getCampPage(SearchDto searchDto, Pageable pageable) throws Exception {

        QCamp camp = QCamp.camp;
        QCampImg campImg = QCampImg.campImg;
        QueryResults<CampSearchDto> results = queryFactory.select(new QCampSearchDto(camp.id, camp.campName, camp.campArea, camp.surroundings, camp.campInfo, campImg.imgUrl, camp.permission))
                .from(campImg).join(campImg.camp, camp).where( searchByLike(searchDto.getSearchQuery()), searchPermission(searchDto.getPermission()))
                .orderBy(camp.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults(); //데이터 가져오기 시작할 위치 : offset, 페이지당 데이터 개수 : limit
        List<CampSearchDto> content = results.getResults();

        for (int i = 0; i < content.size(); i++){
            List<Room> roomList = roomService.getRoomList(content.get(i).getId()); //해당 캠프id에 관한 룸리스트 받기
            int max = roomList.get(0).getRoomPrice();
            int min = roomList.get(0).getRoomPrice();
            for (int j = 0; j < roomList.size(); j++) {
                if (max < roomList.get(j).getRoomPrice()) {
                    max = roomList.get(j).getRoomPrice();
                }
            }
            for (int j = 0; j < roomList.size(); j++) {
                if (min > roomList.get(j).getRoomPrice()) {
                    min = roomList.get(j).getRoomPrice();
                }
            }
            content.get(i).setMax(max);
            content.get(i).setMin(min);
        }
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}
