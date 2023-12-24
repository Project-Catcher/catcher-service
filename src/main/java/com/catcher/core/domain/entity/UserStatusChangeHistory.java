package com.catcher.core.domain.entity;

import com.catcher.core.domain.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "user_status_change_history")
public class UserStatusChangeHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public UserStatusChangeHistory parent;

    @OneToOne(mappedBy = "parent", fetch = FetchType.LAZY)
    public UserStatusChangeHistory child;

    public UserStatus beforeStatus;

    public UserStatus afterStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_user_id", nullable = false)
    public User changedUser;

    public String reason;

    public static UserStatusChangeHistory create(User user, User changedUser, UserStatusChangeHistory parent, UserStatus beforeStatus, UserStatus afterStatus, String reason) {
        return UserStatusChangeHistory.builder()
                .user(user)
                .changedUser(changedUser)
                .parent(parent)
                .beforeStatus(beforeStatus)
                .afterStatus(afterStatus)
                .reason(reason)
                .build();
    }

    public void setChild(final UserStatusChangeHistory history) {
        this.child = history;
    }

}
