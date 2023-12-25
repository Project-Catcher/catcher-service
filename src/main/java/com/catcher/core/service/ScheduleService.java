package com.catcher.core.service;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.*;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.*;
import com.catcher.core.dto.request.*;
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

import java.time.LocalDateTime;
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
    private final ScheduleParticipantRepository scheduleParticipantRepository;
    private final TemplateRepository templateRepository;

    @Transactional(readOnly = true)
    public DraftScheduleResponse getDraftSchedule(User user) {
        List<Schedule> scheduleList = scheduleRepository.findByUserAndStatus(user, ScheduleStatus.DRAFT);

        List<DraftScheduleResponse.ScheduleDTO> scheduleDTOList = scheduleList.stream()
                .map(DraftScheduleResponse.ScheduleDTO::new)
                .collect(Collectors.toList());

        return new DraftScheduleResponse(scheduleDTOList);
    }

    @Transactional
    public SaveScheduleDetailResponse saveScheduleDetail(SaveScheduleDetailRequest request, Long scheduleId, User user) {

        Schedule schedule = getSchedule(scheduleId, user);
        isValidItem(request.getItemType(), request.getItemId());

        ScheduleDetail scheduleDetail = createScheduleDetail(request, schedule);
        scheduleDetailRepository.save(scheduleDetail);

        return new SaveScheduleDetailResponse(scheduleDetail);
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
                .map(userItem -> {
                    String location = null;
                    if (userItem.getLocation() != null) {
                        location = userItem.getLocation().getAddress().getDescription();
                    }
                    return new GetUserItemResponse.UserItemDTO(userItem, location);
                })
                .toList();

        return new GetUserItemResponse(userItemDTOList);
    }

    @Transactional
    public void updateScheduleDetail(User user, Long scheduleDetailId, UpdateScheduleDetailRequest request) {
        ScheduleDetail scheduleDetail = getScheduleDetail(user, scheduleDetailId);
        scheduleDetail.updateScheduleDetail(request.getColor(), request.getDescription(),
                request.getStartAt(), request.getEndAt());

        scheduleDetailRepository.save(scheduleDetail);
    }

    @Transactional
    public void deleteScheduleDetail(User user, Long scheduleDetailId) {
        getScheduleDetail(user, scheduleDetailId);
        scheduleDetailRepository.deleteScheduleDetail(user, scheduleDetailId);
    }

    @Transactional(readOnly = true)
    private void isValidItem(ItemType itemType, Long itemId) {
        switch (itemType) {
            case USERITEM -> userItemRepository.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
            case CATCHERITEM -> catcherItemPort.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
        }
    }

    @Transactional(readOnly = true)
    private Location getLocation(String query) {
        if (StringUtils.isEmpty(query)) {
            return null;
        }

        var address = addressPort.getAddressByQuery(query)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_ADDRESS_RESULT_FOR_QUERY));
        return locationPort.findByAreaCode(address.getAreaCode())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_LOCATION_RESULT));
    }

    @Transactional(readOnly = true)
    private Category getCategory(String category) {
        return categoryPort.findByName(category)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private Schedule getSchedule(Long scheduleId, User user) {
        return scheduleRepository.findByIdAndUser(scheduleId, user)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
    }

    @Transactional
    public MyListResponse myList(Long userId) {
        return new MyListResponse(
                scheduleRepository.upcomingScheduleList(userId),    //다가오는 일정
                scheduleRepository.draftScheduleList(userId),       //작성중인 일정
                scheduleRepository.myOpenScheduleList(userId),      //모집 중
                scheduleRepository.appliedScheduleList(userId)      //참여 신청
        );
    }

    @Transactional
    public void deleteDraftSchedule(Long userId, Long scheduleId) {
        scheduleRepository.deleteDraftSchedule(userId, scheduleId);
    }

    @Transactional
    public void participateSchedule(User user, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdAndScheduleStatus(scheduleId, ScheduleStatus.NORMAL)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));

        // 현재 시간이 참여 가능 기간보다 이후일 경우 or 현재 시간이 참여 가능 기간보다 이전일 경우 예외 처리
        if (LocalDateTime.now().isAfter(schedule.getParticipateEndAt())
                || LocalDateTime.now().isBefore(schedule.getParticipateStartAt())) {
            throw new BaseException(BaseResponseStatus.INVALID_SCHEDULE_PARTICIPANT_TIME);
        }

        // 인원제한이 있을 경우 참여 인원 체크
        if (schedule.getParticipantLimit() != null) {
            Long countParticipant = scheduleParticipantRepository.findCountScheduleParticipantByStatusAndScheduleId(ParticipantStatus.APPROVE, scheduleId);
            //이미 참여 인원을 초과하였을 경우
            if (countParticipant >= schedule.getParticipantLimit()) {
                throw new BaseException(BaseResponseStatus.FULL_PARTICIPATE_LIMIT);
            }
        }

        ScheduleParticipant scheduleParticipant = scheduleParticipantRepository.findByUserAndScheduleIdFilteredByDeletedAt(user.getId(), scheduleId).orElse(null);
        // 참여하지 않은 상태일 경우 참여 처리
        if (scheduleParticipant == null) {
            scheduleRepository.participateSchedule(user, scheduleId);
        } else {
            switch (scheduleParticipant.getStatus()) {
                case CANCEL -> deleteAndInsertScheduleParticipant(scheduleParticipant, schedule, user);
                case REJECT -> throw new BaseException(BaseResponseStatus.REJECTED_PARTICIPATE);
                case APPROVE -> throw new BaseException(BaseResponseStatus.ALREADY_PARTICIPATED_STATUS);
                case PENDING -> throw new BaseException(BaseResponseStatus.PARTICIPATE_WAITING_FOR_APPROVE);
            }
        }
    }

    @Transactional
    private void deleteAndInsertScheduleParticipant(ScheduleParticipant scheduleParticipant, Schedule schedule, User user) {
        scheduleParticipantRepository.updateScheduleParticipantToDeleted(scheduleParticipant.getId());
        scheduleRepository.participateSchedule(user, schedule.getId());
    }

    @Transactional(readOnly = true)
    public ScheduleListResponse getScheduleListByFilter(ScheduleListRequest scheduleListRequest) {

        List<Schedule> schedules = scheduleRepository.findMainScheduleList(scheduleListRequest);

        List<ScheduleListResponse.ScheduleDTO> scheduleDTOList = schedules.stream()
                .map(ScheduleListResponse.ScheduleDTO::new)
                .toList();

        return new ScheduleListResponse(scheduleDTOList);
    }

    @Transactional(readOnly = true)
    private ScheduleDetail getScheduleDetail(User user, Long scheduleDetailId) {
        ScheduleDetail scheduleDetail = scheduleDetailRepository.findByIdWithUser(scheduleDetailId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));

        User owner = scheduleDetail.getSchedule().getUser();
        if (!owner.equals(user)) {
            throw new BaseException(BaseResponseStatus.NO_ACCESS_AUTHORIZATION);
        }

        return scheduleDetail;
    }

    @Transactional(readOnly = true)
    public GetRecommendedTemplateResponse getRecommendedTemplate() {
        List<Template> templateList = templateRepository.findByRecommendedStatus(RecommendedStatus.ENABLED);

        List<GetRecommendedTemplateResponse.TemplateDTO> templateDTOList = templateList.stream()
                .map(template -> {
                    List<ScheduleDetail> scheduleDetailList = scheduleDetailRepository.findBySchedule(template.getSchedule());
                    return getTemplateDTO(template, scheduleDetailList);
                })
                .collect(Collectors.toList());

        return new GetRecommendedTemplateResponse(templateDTOList);
    }

    @Transactional
    private GetRecommendedTemplateResponse.TemplateDTO getTemplateDTO(Template template, List<ScheduleDetail> scheduleDetailList) {
        List<GetRecommendedTemplateResponse.TemplateScheduleDetailDTO> templateScheduleDetailDTOList = scheduleDetailList.stream()
                .map(GetRecommendedTemplateResponse.TemplateScheduleDetailDTO::new)
                .collect(Collectors.toList());

        return new GetRecommendedTemplateResponse.TemplateDTO(template, templateScheduleDetailDTOList);
    }

    @Transactional
    public void cancelParticipateSchedule(User user, Long scheduleId) {
        scheduleParticipantRepository.cancelScheduleParticipant(user.getId(), scheduleId);
    }

    /**
     * 객체 생성은 파일 하단에 위치한다
     */

    private ScheduleDetail createScheduleDetail(SaveScheduleDetailRequest request, Schedule schedule) {
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
}
