package com.catcher.core.port;

import com.catcher.core.domain.entity.Category;

import java.util.Optional;

public interface CategoryPort {

    Optional<Category> findByName(String name);

    Category save(Category category);
}
