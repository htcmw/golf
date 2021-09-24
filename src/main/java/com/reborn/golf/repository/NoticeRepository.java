package com.reborn.golf.repository;

import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.NoticeFractionation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> getNoticeByIdxAndFractionationAndRemovedFalse(Long idx, NoticeFractionation fractionation);

    Page<Notice> getNoticesByFractionationAndRemovedFalse(NoticeFractionation fractionation, Pageable pageable);

}
