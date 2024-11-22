package com.qkdrmawll.hug.group.repository;

import com.qkdrmawll.hug.group.domain.Groups;
import com.qkdrmawll.hug.group.domain.Membership;
import com.qkdrmawll.hug.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {
    Optional<Membership> findByMemberAndGroups(Member member, Groups groups);
    List<Membership> findByMember(Member member);
}
