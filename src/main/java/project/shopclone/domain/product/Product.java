package project.shopclone.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="Products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private String country;
    private Integer price;
    private Integer originPrice;
    private Integer perDiscount;
    private String releaseDate;
    private String category1;
    private String category2;
    private String imageUrl;
    private Boolean isNew;
    private String description;


}
