package com.catcher.core.database;

import com.catcher.core.domain.entity.ScheduleTag;

import java.util.List;

public interface ScheduleTagRepository {
    void save(ScheduleTag scheduleTag);

    void saveAll(List<ScheduleTag> scheduleTagList);
}
