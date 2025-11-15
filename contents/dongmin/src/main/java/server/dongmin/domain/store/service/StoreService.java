package server.dongmin.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dongmin.domain.store.dto.req.StoreRequest;
import server.dongmin.domain.store.dto.req.StoreSearchCondition;
import server.dongmin.domain.store.dto.res.StoreResponse;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.store.repository.StoreRepository;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.domain.user.enums.Role;
import server.dongmin.global.exception.error.CustomErrorCode;
import server.dongmin.global.exception.error.RestApiException;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponse createStore(UserDetailsImpl userDetails, StoreRequest storeRequest) {

        if (userDetails.getUser().getRole() == Role.OWNER || userDetails.getUser().getRole() == Role.ADMIN) {
            Store store = Store.create(userDetails.getUser().getUserId(),  storeRequest);
            storeRepository.save(store);
            return StoreResponse.from(store);
        } else
            throw new RestApiException(CustomErrorCode.USER_NOT_CREATE_STORE);

    }

    @Transactional(readOnly = true)
    public Slice<StoreResponse> getStore(StoreSearchCondition condition, Pageable pageable) {
        return storeRepository.search(condition, pageable).map(StoreResponse::from);
    }

}
