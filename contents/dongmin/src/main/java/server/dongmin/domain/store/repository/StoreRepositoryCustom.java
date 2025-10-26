package server.dongmin.domain.store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import server.dongmin.domain.store.dto.req.StoreSearchCondition;
import server.dongmin.domain.store.entity.Store;

public interface StoreRepositoryCustom {
    Slice<Store> search(StoreSearchCondition condition, Pageable pageable);
}
