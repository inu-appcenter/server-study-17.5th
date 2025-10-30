package server.dongmin.domain.store.dto.req;

import server.dongmin.domain.store.enums.StoreCategory;

public record StoreSearchCondition(
        String location,
        StoreCategory category
        // 필터링 조건 추가 가능
) {
}
