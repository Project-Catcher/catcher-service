package com.catcher.core.domain.entity;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.enums.UserGender;
import com.catcher.core.domain.entity.enums.UserProvider;
import com.catcher.core.domain.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    private String profileImageUrl;

    private String introduceContent;

    @Column(unique = true, nullable = false)
    private String nickname;

    private Date birthDate;

    @Enumerated(value = EnumType.STRING)
    private UserGender userGender;

    @Enumerated(value = EnumType.STRING)
    private UserProvider userProvider;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private UserStatus status = UserStatus.NORMAL;

    private ZonedDateTime phoneAuthentication;

    @Column(nullable = false)
    private ZonedDateTime userAgeTerm; // 필수 약관

    @Column(nullable = false)
    private ZonedDateTime userServiceTerm; // 필수 약관

    @Column(nullable = false)
    private ZonedDateTime userPrivacyTerm; // 필수 약관

    private ZonedDateTime emailMarketingTerm; // 이메일 선택약관

    private ZonedDateTime phoneMarketingTerm; // 핸드폰 선택약관

    private ZonedDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStatusChangeHistory> userStatusChangeHistories = new ArrayList<>();

    public boolean changeUserStatus(UserStatus userStatus) {
        if (this.status == userStatus && this.status == UserStatus.BLACKLISTED) {
            throw new BaseException(BaseResponseStatus.ALREAD_BLACKLISTED);
        }

        if (this.status == userStatus) {
            return false; // 같은 상태로 변경하려고 하면 변경되지 않음
        }

        if (this.status == UserStatus.BLACKLISTED && userStatus == UserStatus.REPORTED) {
            return false; // 블랙리스트 상태에서 신고해도 신고 상태로 변경되지 않음
        }

        this.status = userStatus;
        return true;
    }

}