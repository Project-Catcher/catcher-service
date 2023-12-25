package com.catcher.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class UserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", unique = false)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = false)
    private User user;

    public static UserTag createUserTag(User user, Tag tag) {
        return UserTag.builder()
                .user(user)
                .tag(tag)
                .build();
    }

}
