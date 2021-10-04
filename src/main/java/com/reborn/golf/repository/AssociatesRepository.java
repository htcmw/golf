package com.reborn.golf.repository;

import com.reborn.golf.entity.Associates;
import com.reborn.golf.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AssociatesRepository extends JpaRepository<Associates,Integer> {

    //CustomUserDetailsService에서 사용하는 쿼리이고 권한에 대한 정보도 같이 가져온다.
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select a from Associates a where a.email = :email")
    Optional<Associates> findByEmail(String email);

    //삭제되지 않은 동료 정보
    Optional<Associates> getAssociatesByEmailAndRemovedFalse(String email);

    //삭제되지 않은 동료 정보
    Optional<Associates> getAssociatesByIdxAndRemovedFalse(Integer idx);
    Optional<Associates> getAssociatesByEmailAndPhoneAndRemovedFalse(String email, String phone);
}
