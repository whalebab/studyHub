package com.mkc.studyHub.domain.user.vo;

import com.mkc.studyHub.domain.user.vo.constant.Ability;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Profile {

    private Long profileId;
    private Long userKey;
    private Long interest;
    private Ability ability;
    private String introduction;
    private String portfolioLink;

    @Builder
    public Profile(Long interest, Ability ability, String introduction, String portfolioLink) {
        this.interest = interest;
        this.ability = ability;
        this.introduction = introduction;
        this.portfolioLink = portfolioLink;
    }

}
