package com.qkdrmawll.hug.group.service;

import com.qkdrmawll.hug.group.domain.Groups;
import com.qkdrmawll.hug.invite.domain.Invite;
import com.qkdrmawll.hug.group.domain.Membership;
import com.qkdrmawll.hug.group.domain.Role;
import com.qkdrmawll.hug.group.dto.GroupCreateDto;
import com.qkdrmawll.hug.group.dto.GroupInviteDto;
import com.qkdrmawll.hug.group.dto.GroupResDto;
import com.qkdrmawll.hug.group.dto.MembershipResDto;
import com.qkdrmawll.hug.group.repository.GroupRepository;
import com.qkdrmawll.hug.invite.domain.Status;
import com.qkdrmawll.hug.invite.repository.InviteRepository;
import com.qkdrmawll.hug.group.repository.MembershipRepository;
import com.qkdrmawll.hug.invite.service.EmailService;
import com.qkdrmawll.hug.member.domain.Member;
import com.qkdrmawll.hug.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final InviteRepository inviteRepository;
    private final EmailService emailService;
    public GroupResDto groupCreate(GroupCreateDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));
        Groups groups = dto.toEntity(member);
        Membership membership = Membership.builder()
                .groups(groups)
                .member(member)
                .role(Role.LEADER)
                .build();
        membershipRepository.save(membership);
        return groupRepository.save(groups).fromEntity();
    }

    public void groupDelete(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));
        Groups groups = groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("group을 찾을 수 없습니다."));
        Membership membership = membershipRepository.findByMemberAndGroups(member, groups).orElseThrow(() -> new EntityNotFoundException("membership을 찾을 수 없습니다."));
        if (membership.getRole()!=Role.LEADER) {
            throw new IllegalArgumentException("그룹을 삭제할 권한이 없습니다.");
        }
        groupRepository.delete(groups);
    }

    public void groupMemberInvite(GroupInviteDto dto) {
        Groups group = groupRepository.findById(dto.getGroupId()).orElseThrow(() -> new EntityNotFoundException("그룹이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail(dto.getMemberEmail()).orElseThrow(() -> new EntityNotFoundException("멤버가 존재하지 않습니다."));
        String token = UUID.randomUUID().toString();
        Invite invite = Invite.builder()
                .token(token)
                .groups(group)
                .member(member)
                .status(Status.PENDING)
                .build();
        inviteRepository.save(invite);

        // 이메일 알림 전송
        emailService.sendInviteEmail(dto.getMemberEmail(), group.getName(),token);
    }

    public MembershipResDto inviteAccept(String token) {
        Invite invite = inviteRepository.findByToken(token).orElseThrow(()->new EntityNotFoundException("초대를 찾을 수 없습니다."));
        Member member = invite.getMember();
        Groups groups = invite.getGroups();
        invite.updateStatus();
        Membership membership = Membership.builder()
                .member(member)
                .groups(groups)
                .role(Role.USER)
                .build();
        return membershipRepository.save(membership).fromEntity();


    }
}
