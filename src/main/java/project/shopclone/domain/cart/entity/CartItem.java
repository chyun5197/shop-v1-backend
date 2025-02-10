//package project.shopclone.domain.cart.entity;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import project.shopclone.domain.product.entity.Product;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Table(name="cart_item")
//public class CartItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long cartItemId;
//
//    @ManyToOne
//    @JoinColumn(name="cart_id")
//    private Cart cart;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="product_id")
//    private Product product;
//
//    private int count;
//}
