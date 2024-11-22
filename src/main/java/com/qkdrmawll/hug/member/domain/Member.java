package com.qkdrmawll.hug.member.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qkdrmawll.hug.member.dto.MemberResDto;
import com.qkdrmawll.hug.member.dto.MemberUpdateDto;
import com.qkdrmawll.hug.group.domain.Membership;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String statusMessage;
    private String profileImage;


    public void update(MemberUpdateDto dto, String profileImage) {
        this.name = dto.getName();
        this.statusMessage = dto.getStatusMessage();
        this.profileImage = profileImage;
    }

    public MemberResDto fromEntity() {
        return MemberResDto.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .statusMessage(this.statusMessage)
                .profileImage(this.profileImage)
                .build();
    }
}
