package com.catcher.core.service;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.*;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ContentType;
import com.catcher.core.domain.entity.enums.ItemType;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.dto.request.SaveScheduleSkeletonRequest;
import com.catcher.core.dto.request.SaveDraftScheduleRequest;
import com.catcher.core.dto.request.ScheduleDetailRequest;
import com.catcher.core.dto.request.SaveUserItemRequest;
import com.catcher.core.dto.response.*;
import com.catcher.core.port.AddressPort;
import com.catcher.core.dto.response.MyListResponse;
import com.catcher.core.port.CatcherItemPort;
import com.catcher.core.port.CategoryPort;
import com.catcher.core.port.LocationPort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
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
    public void saveScheduleDetail(ScheduleDetailRequest request, Long scheduleId, User user) {
        Schedule schedule = getSchedule(scheduleId, user);
        isValidItem(request.getItemType(), request.getItemId());

        ScheduleDetail scheduleDetail = createScheduleDetail(request, schedule);
        scheduleDetailRepository.save(scheduleDetail);
    }

    @Transactional
    public SaveUserItemResponse saveUserItem(User user, SaveUserItemRequest request) {
        Category category = getCategory(request.getCategory());

        Location location = getLocation(request.getLocation());
        UserItem userItem = createUserItem(user, request, category, location);
        userItemRepository.save(userItem);

        return new SaveUserItemResponse(userItem);
    }

    @Transactional
    public SaveScheduleSkeletonResponse saveScheduleSkeleton(SaveScheduleSkeletonRequest request, User user) {
        Location location = getLocation(request.getLocation());

        Schedule schedule = Schedule.builder()
                .title(request.getTitle())
                .user(user)
                .thumbnailUrl(request.getThumbnail())
                .location(location)
                .viewCount(0L)
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

        return new SaveScheduleSkeletonResponse(schedule);
    }

    @Transactional
    public void saveDraftSchedule(SaveDraftScheduleRequest request, Long scheduleId, User user) {
        Schedule schedule = getSchedule(scheduleId, user);
        Map<String, Tag> tagMap = tagRepository.findAll().stream()
                .collect(Collectors.toMap(Tag::getName, Function.identity()));

        List<ScheduleTag> scheduleTagList = request.getTags().stream()
                .map(tagName -> {
                    Tag tag = tagMap.get(tagName);
                    if (tag == null) {
                        tag = createTag(tagName);
                        tagRepository.save(tag);
                        tagMap.put(tagName, tag);
                    }
                    return createScheduleTag(schedule, tag);
                })
                .collect(Collectors.toList());

        scheduleTagRepository.saveAll(scheduleTagList);

        Location location = getLocation(request.getLocation());

        schedule.draftSchedule(
                request.getTitle(), request.getThumbnail(), request.getStartAt(), request.getEndAt(),
                location, request.getParticipant(), request.getBudget(), request.getIsPublic(),
                request.getParticipateStartAt(), request.getParticipateEndAt()
        );

        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public RecommendedTagResponse getRecommendedTags() {
        List<Tag> tagList = tagRepository.findByRecommendedStatus(RecommendedStatus.ENABLED);
        List<RecommendedTagResponse.TagDTO> tagDTOList = tagList.stream()
                .map(RecommendedTagResponse.TagDTO::new)
                .collect(Collectors.toList());

        return new RecommendedTagResponse(tagDTOList);
    }

    @Transactional(readOnly = true)
    public GetUserItemResponse getUserItems(User user) {
        List<UserItem> userItemList = userItemRepository.findByUser(user);
        List<GetUserItemResponse.UserItemDTO> userItemDTOList = userItemList.stream()
                .map(GetUserItemResponse.UserItemDTO::new)
                .toList();

        return new GetUserItemResponse(userItemDTOList);
    }

    private void isValidItem(ItemType itemType, Long itemId) {
        switch (itemType) {
            case USERITEM -> userItemRepository.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
            case CATCHERITEM -> catcherItemPort.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
        }
    }

    private Location getLocation(String query) {
        if (StringUtils.isEmpty(query)) {
            return null;
        }

        var address = addressPort.getAddressByQuery(query)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_ADDRESS_RESULT_FOR_QUERY));
        return locationPort.findByAreaCode(address.getAreaCode())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_LOCATION_RESULT));
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

    private UserItem createUserItem(User user, SaveUserItemRequest request, Category category, Location location) {
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
                .recommendedStatus(RecommendedStatus.DISABLED)
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

    private Schedule getSchedule(Long scheduleId, User user) {
        return scheduleRepository.findByIdAndUser(scheduleId, user)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
    }

    @Transactional
    public MyListResponse myList(Long userId) {
        return new MyListResponse(
                scheduleRepository.upcomingScheduleList(userId),    //다가오는 일정
                scheduleRepository.draftScheduleList(userId),       //작성중인 일정
                scheduleRepository.openScheduleList(),              //모집 중
                scheduleRepository.appliedScheduleList(userId)      //참여 신청
        );
    }

    @Transactional
    public void deleteDraftSchedule(Long userId, Long scheduleId) {
        scheduleRepository.deleteDraftSchedule(userId, scheduleId);
    }
}
