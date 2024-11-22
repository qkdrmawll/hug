package com.qkdrmawll.hug.group.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.qkdrmawll.hug.group.dto.MembershipResDto;
import com.qkdrmawll.hug.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;
    @Enumerated(EnumType.STRING)
    private Role role;

    public MembershipResDto fromEntity() {
        return MembershipResDto.builder()
                .groupId(groups.getId())
                .memberId(member.getId())
                .build();
    }
}
