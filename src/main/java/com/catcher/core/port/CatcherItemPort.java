package com.catcher.core.port;

import com.catcher.core.domain.entity.CatcherItem;
import com.catcher.core.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CatcherItemPort {

    void saveAll(List<CatcherItem> catcherItems);

    void save(CatcherItem catcherItem);

    Optional<CatcherItem> findByItemHashValue(String hashKey);

    List<CatcherItem> findByCategory(Category category);

}
