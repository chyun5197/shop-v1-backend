//package project.shopclone.domain.cart.entity;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import project.shopclone.domain.member.Member;
//
//import java.util.List;
//
//@Table(name="cart")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//@AllArgsConstructor
//public class Cart {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long cartId;
//
//    @OneToOne
//    @JoinColumn(name="member_id")
//    private Member member;
//
//    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CartItem> cartItems;
//}
