package com.catcher.infrastructure.adapter;

import com.catcher.core.domain.entity.CatcherItem;
import com.catcher.core.domain.entity.Category;
import com.catcher.core.port.CatcherItemPort;
import com.catcher.datasource.repository.CatcherItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CatcherItemAdapter implements CatcherItemPort {

    private final CatcherItemJpaRepository catcherItemJpaRepository;
    @Override
    public void saveAll(final List<CatcherItem> catcherItems) {
        catcherItemJpaRepository.saveAll(catcherItems);
    }

    @Override
    public void save(final CatcherItem catcherItem) {
        catcherItemJpaRepository.save(catcherItem);
    }

    @Override
    public Optional<CatcherItem> findByItemHashValue(final String hashKey) {
        return catcherItemJpaRepository.findByItemHashValue(hashKey);
    }

    @Override
    public List<CatcherItem> findByCategory(final Category category) {
        return catcherItemJpaRepository.findByCategory(category);
    }

}
