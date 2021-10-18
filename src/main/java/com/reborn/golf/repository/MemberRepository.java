package com.reborn.golf.repository;

import com.reborn.golf.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {

    //CustomUserDetailsService에서 사용하는 쿼리이고 권한에 대한 정보도 같이 가져온다.
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.fromSocial = :social and m.email = :email and m.removed = :removed")
    Optional<Member> findByEmail(String email, boolean social, boolean removed);

    //삭제되지 않은 고객정보
    Optional<Member> getMemberByEmailAndRemovedFalse(String email);
    Optional<Member> getMemberByPhoneAndRemovedFalse(String phone);

    //삭제되지 않은 고객정보
    Optional<Member> getMemberByIdxAndRemovedFalse(Integer idx);

    //비밀번호 찾기
    Optional<Member> getMemberByEmailAndPhoneAndRemovedFalse(String email, String phone);

}
