package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileJpaRepository extends JpaRepository<UploadFile, Long> {
}
