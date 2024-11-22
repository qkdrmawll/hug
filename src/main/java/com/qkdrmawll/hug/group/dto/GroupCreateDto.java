package com.qkdrmawll.hug.group.dto;

import com.qkdrmawll.hug.group.domain.Groups;
import com.qkdrmawll.hug.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateDto {
    private String name;
    private String description;

    public Groups toEntity(Member member) {
        return Groups.builder()
                .name(this.name)
                .description(this.description)
                .build();
    }
}
