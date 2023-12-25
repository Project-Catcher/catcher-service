package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {

}
