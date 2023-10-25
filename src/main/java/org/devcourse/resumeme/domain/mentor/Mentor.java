package org.devcourse.resumeme.domain.mentor;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.mentee.MenteePosition;
import org.devcourse.resumeme.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.domain.user.Provider;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Mentor {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "mentee_id")
    private Long id;

    @Getter
    @Column(unique = true)
    private String oauthUsername;

    @Getter
    private String password;

    @Getter
    private String email;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String imageUrl;

    @Getter
    @Embedded
    private RequiredInfo requiredInfo;

    private String refreshToken;

    @OneToMany
    private Set<MenteePosition> interestedPositions = new HashSet<>();

    private String careerContent;

    private int careerYear;

    private String introduce;

    @Builder
    public Mentor(Long id, String oauthUsername, String password, String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken, Set<MenteePosition> interestedPositions, String careerContent, int careerYear, String introduce) {
        this.id = id;
        this.oauthUsername = oauthUsername;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.refreshToken = refreshToken;
        this.interestedPositions = interestedPositions;
        this.careerContent = careerContent;
        this.careerYear = careerYear;
        this.introduce = introduce;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
