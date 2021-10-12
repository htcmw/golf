package com.reborn.golf.repository;

import com.reborn.golf.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    //getList 메서드에서 사용, Noticc의 리스트를 가져오는 쿼리
    @Query(value =
            "SELECT n, w " +
                    "FROM Notice n " +
                    "LEFT JOIN n.writer w " +
                    "WHERE n.removed = false ")
    Page<Object[]> getNoticesWithWriter(Pageable pageable);

    //modify, remove 메서드에서 사용

    @Query(value =
                    "SELECT n, w.email, w.name " +
                    "FROM Notice n " +
                            "LEFT JOIN n.writer w " +
                    "WHERE n.idx = :idx " +
                            "AND n.removed = false ")
    Optional<Notice> getNoticeByIdx(Long idx);

}
