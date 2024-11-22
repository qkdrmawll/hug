package com.qkdrmawll.hug.post.repository;

import com.qkdrmawll.hug.member.domain.Member;
import com.qkdrmawll.hug.post.domain.Likes;
import com.qkdrmawll.hug.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByMemberAndPost(Member member, Post post);
}
