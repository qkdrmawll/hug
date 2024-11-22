package com.qkdrmawll.hug.post.repository;

import com.qkdrmawll.hug.group.domain.Groups;
import com.qkdrmawll.hug.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByGroups(Groups groups);
}
