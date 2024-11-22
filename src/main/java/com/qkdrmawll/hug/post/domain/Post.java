package com.qkdrmawll.hug.post.domain;

import com.qkdrmawll.hug.group.domain.Groups;
import com.qkdrmawll.hug.member.domain.Member;
import com.qkdrmawll.hug.post.dto.PostResDto;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;
    private String anonymousName;
    private String content;
    private int likes;

    public PostResDto fromEntity() {
        return PostResDto.builder()
                .id(this.id)
                .content(this.content)
                .anonymousName(this.anonymousName)
                .likes(this.likes).build();
    }

    public int like() {
        this.likes += 1;
        return likes;
    }

    public int unlike() {
        this.likes -= 1;
        return likes;
    }
//    @ManyToOne
//    @JoinColumn(name = "author_id")
//    private Member author;
//    private boolean isAnonymous;
}
