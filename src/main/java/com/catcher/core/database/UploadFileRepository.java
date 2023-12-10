package com.catcher.core.database;

import com.catcher.core.domain.entity.UploadFile;

public interface UploadFileRepository {
    void save(UploadFile uploadFile);
}
