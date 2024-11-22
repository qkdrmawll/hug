package com.qkdrmawll.hug.group.domain;

import com.qkdrmawll.hug.group.dto.GroupResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    public GroupResDto fromEntity() {
        return GroupResDto.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .build();
    }
}
