package com.catcher.datasource;


import com.catcher.core.database.UserTagRepository;
import com.catcher.core.domain.entity.UserTag;
import com.catcher.datasource.repository.UserTagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserTagRepositoryImpl implements UserTagRepository {
    private final UserTagJpaRepository userTagJpaRepository;

    @Override
    public void deleteByUserId(Long userId) {
        userTagJpaRepository.deleteByUserId(userId);
    }

    @Override
    public void saveAll(List<UserTag> userTags) {
        userTagJpaRepository.saveAll(userTags);
    }

    @Override
    public List<UserTag> findByUserId(Long userId) {
        return userTagJpaRepository.findByUserIdFJTag(userId);
    }
}
