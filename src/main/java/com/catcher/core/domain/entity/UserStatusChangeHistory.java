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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private UserStatusChangeHistory child;

    private UserStatus action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_user_id", nullable = false)
    private User changedUser;

    private String reason;

    private boolean affected;

    public static UserStatusChangeHistory create(User user,
                                                 User changedUser,
                                                 UserStatus action,
                                                 String reason,
                                                 boolean affected) {
        return UserStatusChangeHistory.builder()
                .user(user)
                .changedUser(changedUser)
                .action(action)
                .reason(reason)
                .affected(affected)
                .build();
    }

    public void setChild(final UserStatusChangeHistory history) {
        this.child = history;
    }

}
