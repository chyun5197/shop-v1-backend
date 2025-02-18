package project.shopclone.global.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageLimitCalculator {
    // (현재 페이지, 페이지당 상품 개수, 한번에 이동가능한 페이지)
    // param, 35, 5
    public static Long calculatePageLimit(Long page, Long pageSize, Long movablePageCount) {
//        return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1;
        return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount;
    }
}
