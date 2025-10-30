package server.dongmin.domain.store.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import server.dongmin.domain.store.dto.req.StoreSearchCondition;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.store.enums.StoreCategory;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static server.dongmin.domain.store.entity.QStore.store;

public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Store> search(StoreSearchCondition condition, Pageable pageable) {
        List<Store> content = queryFactory
                .selectFrom(store)
                .where(
                        locationContains(condition.location()),
                        categoryEq(condition.category())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(store)
                .where(
                        locationContains(condition.location()),
                        categoryEq(condition.category())
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression locationContains(String location) {
        // location 파라미터가 없으면(null이거나 비어있으면) null을 반환
        return hasText(location) ? store.address.contains(location) : null;
    }

    private BooleanExpression categoryEq(StoreCategory category) {
        // category 파라미터가 없으면(null이면) null을 반환
        return category != null ? store.category.eq(category) : null;
    }
}
