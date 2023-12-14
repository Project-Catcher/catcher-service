package com.catcher.datasource;

import com.catcher.core.database.UserItemRepository;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserItem;
import com.catcher.datasource.repository.UserItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserItemRepositoryImpl implements UserItemRepository {
    private final UserItemJpaRepository userItemJpaRepository;

    @Override
    public void save(UserItem userItem) {
        userItemJpaRepository.save(userItem);
    }

    @Override
    public Optional<UserItem> findById(Long itemId) {
        return userItemJpaRepository.findById(itemId);
    }

    @Override
    public List<UserItem> findByUser(User user) {
        return userItemJpaRepository.findByUser(user);
    }
}
