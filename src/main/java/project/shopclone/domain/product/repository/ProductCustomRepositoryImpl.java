package project.shopclone.domain.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import project.shopclone.domain.product.entity.Product;

import java.util.List;

import static project.shopclone.domain.product.entity.QProduct.product;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;

    public ProductCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Product> searchAll(String keyword, String category, String brand, String sorting, Integer start, Integer end, Long page, Long pageSize) {
        // 검색 조건 동적 쿼리
        BooleanBuilder builder = new BooleanBuilder();

        // 띄어쓰기 기준 여러 단어 포함한 검색 (sql like '%키워드%' 검색)
        String[] keywordList = keyword.split(" ");
        for(String keywordStr : keywordList){
            builder.and(product.name.contains(keywordStr));
        }

        if (!category.isEmpty()) { // 카테고리 선택시 (Guitar, Bass..)
            builder.and(product.category.contains(category));
        }
        if (!brand.isEmpty()) { // 브랜드 선택시
            builder.and(product.brand.eq(brand));
        }
        if (start != null && end != null) { // 사이 금액
            builder.and(product.price.between(start, end));
        }else if(start !=null){ // 최소가격 초과
            builder.and(product.price.gt(start));
        }else if(end !=null){ // 최대가격 미만
            builder.and(product.price.lt(end));
        }

        return queryFactory.selectFrom(product)
                .where(builder)
                .orderBy(orderSpecifier(sorting))
                .limit(pageSize)
                .fetch();
    }

    // 정렬 동적 쿼리
    private OrderSpecifier orderSpecifier(String sorting) {
        if(sorting == null){ // 기본은 신상품순
            return product.releaseDate.desc();
        }else if (sorting.equals("asc")) { // 낮은가격
            return product.price.asc();
        }else if (sorting.equals("desc")) { // 높은가격
            return product.price.desc();
        }
        return product.releaseDate.desc();
    }
}
