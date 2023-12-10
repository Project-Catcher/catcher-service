package com.catcher.core.service;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.*;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ContentType;
import com.catcher.core.domain.entity.enums.ItemType;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.dto.request.SaveScheduleInfoRequest;
import com.catcher.core.dto.request.SaveDraftScheduleRequest;
import com.catcher.core.dto.request.ScheduleDetailRequest;
import com.catcher.core.dto.request.UserItemRequest;
import com.catcher.core.dto.response.*;
import com.catcher.core.port.AddressPort;
import com.catcher.core.port.CatcherItemPort;
import com.catcher.core.port.CategoryPort;
import com.catcher.core.port.LocationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserItemRepository userItemRepository;
    private final CatcherItemPort catcherItemPort;
    private final CategoryPort categoryPort;
    private final LocationPort locationPort;
    private final AddressPort addressPort;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final TagRepository tagRepository;
    private final UploadFileRepository uploadFileRepository;
    private final ScheduleTagRepository scheduleTagRepository;

    @Transactional(readOnly = true)
    public DraftScheduleResponse getDraftSchedule(User user) {
        List<Schedule> scheduleList = scheduleRepository.findByUserAndStatus(user, ScheduleStatus.DRAFT);

        List<DraftScheduleResponse.ScheduleDTO> scheduleDTOList = scheduleList.stream()
                .map(DraftScheduleResponse.ScheduleDTO::new)
                .collect(Collectors.toList());

        return new DraftScheduleResponse(scheduleDTOList);
    }

    @Transactional
    public void saveScheduleDetail(ScheduleDetailRequest request, Long scheduleId){
        Schedule schedule = getSchedule(scheduleId);
        isValidItem(request.getItemType(), request.getItemId());

        ScheduleDetail scheduleDetail = createScheduleDetail(request, schedule);
        scheduleDetailRepository.save(scheduleDetail);
    }

    @Transactional
    public SaveUserItemResponse saveUserItem(User user, UserItemRequest request) {
        Category category = getCategory(request.getCategory());

        var address = addressPort.getAddressByQuery(request.getLocation())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_ADDRESS_RESULT_FOR_QUERY));
        Location location = locationPort.findByAreaCode(address.getAreaCode())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_LOCATION_RESULT));

        UserItem userItem = createUserItem(user, request, category, location);
        userItemRepository.save(userItem);

        return new SaveUserItemResponse(userItem);
    }

    @Transactional(readOnly = true)
    public CatcherItemResponse getCatcherItems(String query) {
        // TODO queryDSL 추후 작업
        List<CatcherItemResponse.CatcherItemDTO> catcherItemDTOList = new ArrayList<>();
        return new CatcherItemResponse(catcherItemDTOList);
    }

    @Transactional
    public SaveScheduleInfoResponse saveScheduleInfo(SaveScheduleInfoRequest request, User user) {
        Schedule schedule = Schedule.builder()
                .title(request.getTitle())
                .user(user)
                .thumbnailUrl(request.getThumbnail())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .scheduleStatus(ScheduleStatus.DRAFT)
                .build();

        scheduleRepository.save(schedule);

        UploadFile uploadFile = UploadFile.builder()
                .contentId(schedule.getId())
                .contentType(ContentType.SCHEDULE)
                .fileUrl(request.getThumbnail())
                .build();

        uploadFileRepository.save(uploadFile);

        return new SaveScheduleInfoResponse(schedule);
    }

    @Transactional
    public void saveDraftSchedule(SaveDraftScheduleRequest request, Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        Map<String, Tag> tagMap = tagRepository.findAll().stream()
                .collect(Collectors.toMap(Tag::getName, Function.identity()));

        List<ScheduleTag> scheduleTagList = request.getTags().stream()
                .map(tagName -> {
                    Tag tag = tagMap.get(tagName);
                    if (tag == null) {
                        tag = createTag(tagName);
                        tagRepository.save(tag);
                    }
                    return createScheduleTag(schedule, tag);
                })
                .collect(Collectors.toList());

        scheduleTagRepository.saveAll(scheduleTagList);

        schedule.draftSchedule(request.getParticipant(), request.getBudget(), request.getIsPublic(),
                request.getParticipateStartAt(), request.getParticipateEndAt());

        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public RecommendedTagResponse getRecommendedTags() {
        List<Tag> tagList = tagRepository.findByIsRecommendedTrue();
        List<RecommendedTagResponse.TagDTO> tagDTOList = tagList.stream()
                .map(RecommendedTagResponse.TagDTO::new)
                .collect(Collectors.toList());

        return new RecommendedTagResponse(tagDTOList);
    }

    private void isValidItem(ItemType itemType, Long itemId) {
        switch (itemType) {
            case USERITEM -> userItemRepository.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
            case CATCHERITEM -> catcherItemPort.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
        }
    }

    private ScheduleDetail createScheduleDetail(ScheduleDetailRequest request, Schedule schedule) {
        return ScheduleDetail.builder()
                .schedule(schedule)
                .itemType(request.getItemType())
                .itemId(request.getItemId())
                .description(request.getDescription())
                .color(request.getColor())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .build();
    }

    private UserItem createUserItem(User user, UserItemRequest request, Category category, Location location) {
        return UserItem.builder()
                .user(user)
                .category(category)
                .location(location)
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }

    private Tag createTag(String tagName) {
        return Tag.builder()
                .name(tagName)
                .build();
    }

    private ScheduleTag createScheduleTag(Schedule schedule, Tag tag) {
        return ScheduleTag.builder()
                .schedule(schedule)
                .tag(tag)
                .build();
    }

    private Category getCategory(String category) {
        return categoryPort.findByName(category)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
    }

    private Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
    }
}
