package com.qkdrmawll.hug.group.repository;

import com.qkdrmawll.hug.group.domain.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Groups, Long> {
}
