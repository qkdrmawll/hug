package com.qkdrmawll.hug.invite.domain;

import com.qkdrmawll.hug.group.domain.Groups;
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
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;
    private String token;
    @Enumerated(EnumType.STRING)
    private Status status;

    public void updateStatus() {
        this.status = Status.ACCEPTED;
    }
}
