package project.shopclone.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.wish.Wish;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Query("select p " +
//            "from Product p " +
//            "where p.cateNo = :cateNo " +
//            "order by p.releaseDate desc " +
//            "limit :limit offset :offset ")
    // 한 브랜드 모든 상품 최신순
    // 서브쿼리의 인덱스로 찾는 시간 단축
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
    List<Product> findAllByCateNo(
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
    List<Product> findAllByCateNoPriceAsc(
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
    List<Product> findAllByCateNoPriceDesc(
            @Param("cateNo") Integer cateNo,
            @Param("offset") Long offset,
            @Param("limit") Long limit,
            @Param("sorting") String sorting
    );

    // 모든 상품 페이징 일반쿼리
    @Query("select p " +
        "from Product p " +
        "order by p.releaseDate desc " +
        "limit :limit offset :offset ")
    List<Product> findAllProduct(
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

    // 베스트 상품 50개 (위시개수 기준) 일반쿼리
    @Query("select p " +
            "from Product p " +
            "order by p.wishCount desc " +
            "limit 50 offset 0 ")
    List<Product> findBestProductList();

    // 카테고리 like 조회
    @Query("select p " +
        "from Product p " +
        "where p.category = :cates " +
        "order by p.releaseDate desc " +
        "limit :limit offset :offset ")
    // 서브쿼리의 인덱스로 찾는 시간 단축
//    @Query(
//            value = "select product.* " +
//                    "from (" +
//                    "   select id from product" +
//                    "   where category like :cates% " +
//                    "   order by release_date desc " +
//                    "   limit :limit offset :offset " +
//                    ") t left join product on t.id = product.id",
//            nativeQuery = true
//    )
    List<Product> findAllCatesProducts(
            @Param("cates") String cates,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

    //카테고리별 조회
//    @Query("select p " +
//    "from Product p " +
//    "where p.category = :category " +
//    "order by p.releaseDate desc " +
//    "limit :limit offset :offset ")
    // 서브쿼리의 인덱스로 찾는 시간 단축
    @Query(
            value = "select product.* " +
                    "from (" +
                    "   select id from product" +
                    "   where category = :category " +
                    "   order by release_date desc " +
                    "   limit :limit offset :offset " +
                    ") t left join product on t.id = product.id",
            nativeQuery = true
    )
    List<Product> findAllByCategory(
            @Param("category") String category,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );


    // 악기별 조회
    @Query("select p " +
        "from Product p " +
        "where p.inst = :inst " +
        "order by p.releaseDate desc " +
        "limit :limit offset :offset ")
    // 커버링 인덱스로 찾는 시간 단축
//    @Query(
//            value = "select product.* " +
//                    "from (" +
//                    "   select id from product" +
//                    "   where inst = :inst " +
//                    "   order by release_date desc " +
//                    "   limit :limit offset :offset " +
//                    ") t left join product on t.id = product.id",
//            nativeQuery = true
//    )
    List<Product> findAllByInst(
            @Param("inst") String inst,
            @Param("offset") Long offset,
            @Param("limit") Long limit
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

    // 위시에 없는 상품 랜덤 1개 뽑기
    @Query(value = "select * from product where id not in " +
            "(select member_id from wish where member_id = :memberId) " +
            "order by Rand() limit 1",
            nativeQuery = true)
    List<Product> findListOneNotInOriginWish(Long memberId);
}
