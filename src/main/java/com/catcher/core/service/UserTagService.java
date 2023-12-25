package com.catcher.core.service;

import com.catcher.core.database.TagRepository;
import com.catcher.core.database.UserTagRepository;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.entity.Tag;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserTag;
import com.catcher.core.domain.entity.enums.RecommendedStatus;
import com.catcher.core.dto.response.UserTagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserTagService {
    private final TagRepository tagRepository;
    private final UserTagRepository userTagRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updateTags(User user, List<String> requestTags) {
        user = userRepository.findById(user.getId()).orElseThrow();
        userTagRepository.deleteByUserId(user.getId());

        List<Tag> foundTags = tagRepository.findByNames(requestTags);
        Set<Tag> tagSet = new HashSet<>(foundTags);

        requestTags.forEach(tagName -> {
            if (!tagSet.contains(tagName)) {
                Tag newTag = createTagAndSave(tagName);
                tagSet.add(newTag);
            }
        });

        List<UserTag> userTags = generateUserTags(user, tagSet);
        userTagRepository.saveAll(userTags);
    }

    public UserTagResponse findTagsByUser(User user) {
        List<UserTag> userTags = userTagRepository.findByUserId(user.getId());

        return new UserTagResponse(userTags);
    }

    private List<UserTag> generateUserTags(User user, Set<Tag> tagSet) {
        return tagSet.stream().map(
                tag -> UserTag.createUserTag(user, tag)
        ).toList();
    }

    private Tag createTagAndSave(String tagName) {
        Tag tag = Tag.builder()
                .name(tagName)
                .recommendedStatus(RecommendedStatus.DISABLED)
                .build();

        return tagRepository.save(tag);
    }
}
