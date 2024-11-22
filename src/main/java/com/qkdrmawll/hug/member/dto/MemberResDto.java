package com.qkdrmawll.hug.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResDto {
    private Long id;
    private String name;
    private String email;
    private String statusMessage;
    private String profileImage;
}
