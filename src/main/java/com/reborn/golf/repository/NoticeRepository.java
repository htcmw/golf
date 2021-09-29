package com.reborn.golf.repository;

import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.NoticeFractionation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    //getList 메서드에서 사용, Noticc의 리스트를 가져오는 쿼리
    @Query(value =
            "SELECT n.idx, n.title, n.views, n.regDate, n.modDate, w.idx, w.email, w.name " +
                    "FROM Notice n " +
                    "LEFT JOIN n.writer w " +
                    "WHERE n.fractionation = :fractionation AND n.removed = false ",
            countQuery =
                    "SELECT count(n) " +
                            "FROM Notice n")
    Page<Object[]> getNoticesWithWriter(NoticeFractionation fractionation, Pageable pageable);

    @Query(value =
                    "SELECT n.idx, n.title, n.content, n.views, n.regDate, n.modDate, w.idx, w.email, w.name " +
                    "FROM Notice n " +
                            "LEFT JOIN n.writer w " +
                    "WHERE n.fractionation = :fractionation " +
                            "AND n.idx = :idx " +
                            "AND n.removed = false ")
    Optional<Notice> getNoticeByIdx(Long idx, NoticeFractionation fractionation);

    //read 메서드에서 사용, QnA의 답글을 가져오는 쿼리
    @Query(value =
                    "SELECT nc.idx, nc.title, nc.content, nc.regDate, nc.modDate, w.idx, w.email, w.name " +
                    "FROM Notice n " +
                            "JOIN Notice nc on n = nc.parent " +
                            "LEFT JOIN nc.writer w " +
                    "WHERE n.fractionation = :fractionation " +
                            "AND n.idx = :idx " +
                            "AND nc.removed = false " +
                    "ORDER BY nc.regDate ASC")
    List<Notice> getAnswerByIdx(Long idx, NoticeFractionation fractionation);


}
