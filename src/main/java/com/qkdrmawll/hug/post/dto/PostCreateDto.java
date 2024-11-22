package com.qkdrmawll.hug.post.dto;

import com.qkdrmawll.hug.group.domain.Groups;
import com.qkdrmawll.hug.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {
    private Long groupId;
    private String anonymousName;
    private String content;

    public Post toEntity(Groups groups) {
        return Post.builder()
                .groups(groups)
                .content(this.content)
                .anonymousName(this.anonymousName)
                .build();
    }
}
