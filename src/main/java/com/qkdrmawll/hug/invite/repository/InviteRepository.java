package com.qkdrmawll.hug.invite.repository;

import com.qkdrmawll.hug.invite.domain.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteRepository extends JpaRepository<Invite,Long> {
    Optional<Invite> findByToken(String token);
}
