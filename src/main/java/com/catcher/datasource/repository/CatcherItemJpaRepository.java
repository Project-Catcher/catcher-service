package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.CatcherItem;
import com.catcher.core.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatcherItemJpaRepository extends JpaRepository<CatcherItem, Long> {

    Optional<CatcherItem> findByItemHashValue(String hashKey);

    List<CatcherItem> findByCategory(Category category);

}
