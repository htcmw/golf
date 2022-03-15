package com.reborn.golf.product.repository;

import com.reborn.golf.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslProductRepository {
    // 상세페이지 조회 때 사용
    @Query( "SELECT " +
                "p, " +
                "pi," +
                "AVG(COALESCE(r.grade,0)),  " +
                "COUNT(r)" +
            "FROM Product p " +
                    "LEFT OUTER JOIN ProductImage pi ON pi.product = p " +
                    "LEFT OUTER JOIN ProductReply  r ON r.product = p " +
            "WHERE p.idx = :productIdx " +
                    "AND pi.removed = false " +
            "GROUP BY pi")
    List<Object[]> getProductWithAll(Long productIdx);

    // 수정, 삭제 때 사용
    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.idx = :productIdx " +
            "AND p.removed = false ")
    Optional<Product> getProductByIdx(Long productIdx);
}
