package com.qkdrmawll.hug.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResDto {
    private Long id;
    private String content;
    private String anonymousName;
    private int likes;
}
