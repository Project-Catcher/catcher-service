package com.catcher.datasource;

import com.catcher.core.database.UploadFileRepository;
import com.catcher.core.domain.entity.UploadFile;
import com.catcher.datasource.repository.UploadFileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UploadFileRepositoryImpl implements UploadFileRepository {
    private final UploadFileJpaRepository uploadFileJpaRepository;

    @Override
    public void save(UploadFile uploadFile) {
        uploadFileJpaRepository.save(uploadFile);
    }
}
