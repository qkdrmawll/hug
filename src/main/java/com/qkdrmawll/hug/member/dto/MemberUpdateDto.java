package com.qkdrmawll.hug.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    private String name;
    private String statusMessage;
    private MultipartFile profileImage;
}
