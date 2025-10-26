package server.dongmin.domain.store.dto.req;

import lombok.Data;
import server.dongmin.domain.store.enums.StoreCategory;

@Data
public class StoreSearchCondition {

    private String location;
    private StoreCategory category;
    // 필터링 조건 추가 가능
}
