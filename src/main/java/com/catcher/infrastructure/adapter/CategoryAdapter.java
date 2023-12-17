package com.catcher.infrastructure.adapter;

import com.catcher.core.domain.entity.Category;
import com.catcher.core.port.CategoryPort;
import com.catcher.datasource.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Optional<Category> findByName(final String name) {
        return categoryJpaRepository.findByName(name);
    }

    @Override
    public Category save(final Category category) {
        return categoryJpaRepository.save(category);
    }

}
