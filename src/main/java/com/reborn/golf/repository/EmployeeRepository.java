package com.reborn.golf.repository;

import com.reborn.golf.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    //CustomUserDetailsService에서 사용하는 쿼리이고 권한에 대한 정보도 같이 가져온다.
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select a from Employee a where a.email = :email")
    Optional<Employee> findByEmail(String email);

    //삭제되지 않은 동료 정보
    Optional<Employee> getAssociatesByEmailAndRemovedFalse(String email);

    //삭제되지 않은 동료 정보
    Optional<Employee> getAssociatesByIdxAndRemovedFalse(Integer idx);
    Optional<Employee> getAssociatesByEmailAndPhoneAndRemovedFalse(String email, String phone);
}
