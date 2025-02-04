package project.shopclone.domain.product.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.shopclone.domain.product.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(
//            value = "select product.id, product.no, product.category, product.brand, " +
//                    "product.name, product.price, product.origin_price, product.image " +
//                    "from (" +
//                    "   select id from product" +
//                    "   where brand = :brand " +
//                    "   order by release_date desc " +
//                    "   limit :limit offset :offset " +
//                    ") t left join product on t.id = product.id",
//            nativeQuery = true
            value = "select p from Product p " +
                    "where p.brand = :brand " +
                    "order by p.releaseDate desc " +
                    "limit :limit offset :offset "
    )
    List<Product> findAllBrand(
            @Param("brand") String brand,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

    @Query( "select p from Product p " +
            "where p.brand = :brand " +
            "order by p.price asc " +
            "limit :limit offset :offset "
            )
    List<Product> findAllBrand(
            @Param("brand") String brand,
            @Param("offset") Long offset,
            @Param("limit") Long limit,
            @Param("sorting") String sorting
    );

    @Query( "select p from Product p " +
            "where p.brand = :brand " +
            "order by p.price desc " +
            "limit :limit offset :offset "
    )
    List<Product> findAllBrandDesc(
            @Param("brand") String brand,
            @Param("offset") Long offset,
            @Param("limit") Long limit,
            @Param("sorting") String sorting
    );

    @Query(
            value = "select count(*) from (" +
                    "   select id from product where brand = :brand limit :limit " +
                    ") t",
            nativeQuery = true
    )
    Long count(
            @Param("brand") String brand,
            @Param("limit") Long limit
    );

//    @Query(value = "select p from Product p where p.id = :id")
//    Product getProductById(@Param("id") Long id);

    @Query(value = "select p from Product p order by p.releaseDate desc limit 35 offset 0")
    List<Product> getProductTmp();

    @Query("select count(*) from Product ")
    Integer countAll();
}
