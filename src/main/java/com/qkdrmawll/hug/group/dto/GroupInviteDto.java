package com.qkdrmawll.hug.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupInviteDto {
    private Long groupId;
    private String memberEmail;
}
