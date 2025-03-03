package project.shopclone.domain.product.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.shopclone.domain.product.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Query("select p " +
//            "from Product p " +
//            "where p.cateNo = :cateNo " +
//            "order by p.releaseDate desc " +
//            "limit :limit offset :offset ")
    // 한 브랜드 모든 상품 최신순
    // 서브쿼리의 인덱스로 찾는 시간 단축(limit 보완)
    @Query(
            value = "select product.* " +
                    "from (" +
                    "   select id from product" +
                    "   where cate_no = :cateNo " +
                    "   order by release_date desc " +
                    "   limit :limit offset :offset " +
                    ") t left join product on t.id = product.id",
            nativeQuery = true
    )
    List<Product> findAllBrand(
            @Param("cateNo") Integer cateNo,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

//    @Query("select p " +
//        "from Product p " +
//        "where p.cateNo = :cateNo " +
//        "order by p.price asc " +
//        "limit :limit offset :offset ")
    // 한 브랜드 모든 상품 낮은가격순
    @Query(
            value = "select product.* " +
                    "from (" +
                    "   select id from product" +
                    "   where cate_no = :cateNo " +
                    "   order by price asc " +
                    "   limit :limit offset :offset " +
                    ") t left join product on t.id = product.id",
            nativeQuery = true
    )
    List<Product> findAllBrand(
            @Param("cateNo") Integer cateNo,
            @Param("offset") Long offset,
            @Param("limit") Long limit,
            @Param("sorting") String sorting
    );

    // 한 브랜드 모든 상품 높은가격순
    @Query(
            value = "select product.* " +
                    "from (" +
                    "   select id from product" +
                    "   where cate_no = :cateNo " +
                    "   order by price desc " +
                    "   limit :limit offset :offset " +
                    ") t left join product on t.id = product.id",
            nativeQuery = true
    )
    List<Product> findAllBrandDesc(
            @Param("cateNo") Integer cateNo,
            @Param("offset") Long offset,
            @Param("limit") Long limit,
            @Param("sorting") String sorting
    );

    // 페이지수를 알기 위한 카운트
    @Query(
            value = "select count(*) from (" +
                    "   select id from product where cate_no = :cateNo limit :limit " +
                    ") t",
            nativeQuery = true
    )
    Long count(
            @Param("cateNo") Integer cateNo,
            @Param("limit") Long limit
    );

    // 분류번호 등록된 상품 개수
    Integer countByCateNo(Integer cateNo);

//    @Query(value = "select p from Product p where p.id = :id")
//    Product getProductById(@Param("id") Long id);

    // 임시
    @Query("select p from Product p order by p.releaseDate desc limit 35 offset 0")
    List<Product> getProductTmp();

    // 한 브랜드 상품 개수 (쑤정해야)
    @Query("select count(*) from Product ")
    Integer countAll();

    List<Product> findAllByBrandAndCategory(String brand, String category);
    List<Product> findAllByCateNo(Integer cateNo);
}
