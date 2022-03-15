package com.reborn.golf.member.repository;

import com.reborn.golf.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    //spring security, CustomUserDetailsService에서 사용
    @EntityGraph(attributePaths = {"roleSet"})
    @Query("select m " +
            "from Member m " +
            "where m.fromSocial = :social and m.email = :email and m.removed = :removed")
    Optional<Member> findByEmail(@Param("email") String email, @Param("social") boolean social, @Param("removed") boolean removed);

    //order-system에서 사용
    @EntityGraph(attributePaths = {"wallet"})
    @Query("SELECT m " +
            "FROM Member m " +
            "WHERE m.idx = :memberIdx and m.removed = false")
    Optional<Member> getMemberWithWallet(@Param("memberIdx") Integer memberIdx);

    //삭제되지 않은 고객정보
    //PurchasedProductService, MemberService에서 사용
    Optional<Member> getByIdxAndRemovedFalse(Integer idx);

    Optional<Member> getMemberByPhoneAndRemovedFalse(String phone);

    //비밀번호 찾기
    Optional<Member> getMemberByEmailAndPhoneAndRemovedFalse(String email, String phone);


    @Query("SELECT m " +
            "FROM Member m LEFT JOIN FETCH Wallet w on m.wallet = w " +
            "WHERE m.idx = :idx AND m.email = :email AND m.name = :name")
    Optional<Member> findByIdxWithWallet(@Param("idx") Integer idx, @Param("email") String email, @Param("name") String name);

}