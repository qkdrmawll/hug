package com.qkdrmawll.hug.post.service;

import com.qkdrmawll.hug.group.domain.Groups;
import com.qkdrmawll.hug.group.repository.GroupRepository;
import com.qkdrmawll.hug.member.domain.Member;
import com.qkdrmawll.hug.member.repository.MemberRepository;
import com.qkdrmawll.hug.post.domain.Likes;
import com.qkdrmawll.hug.post.domain.Post;
import com.qkdrmawll.hug.post.dto.PostCreateDto;
import com.qkdrmawll.hug.post.dto.PostResDto;
import com.qkdrmawll.hug.post.repository.LikeRepository;
import com.qkdrmawll.hug.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    @Qualifier("3")
    private final RedisTemplate<String,Object> redisTemplate;
    private static final String POPULAR_POST_KEY = "popularPostKey:";

    @Autowired
    public PostService(PostRepository postRepository, GroupRepository groupRepository, MemberRepository memberRepository, LikeRepository likeRepository, @Qualifier("3") RedisTemplate<String, Object> redisPostTemplate) {
        this.postRepository = postRepository;
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.likeRepository = likeRepository;
        this.redisTemplate = redisPostTemplate;
    }

    public PostResDto postCreate(PostCreateDto dto) {
        Groups groups = groupRepository.findById(dto.getGroupId()).orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));
        Post post = dto.toEntity(groups);
        return postRepository.save(post).fromEntity();
    }

    public PostResDto postDetail(Long id) {
        String redisKey = POPULAR_POST_KEY + id;
        Post post = (Post) redisTemplate.opsForValue().get(redisKey);
        if (post == null) {
            post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("post를 조회할 수 없습니다."));
        }
        return post.fromEntity();
    }

    public void postLike(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("post를 조회할 수 없습니다."));
        Optional<Likes> optionalLike = likeRepository.findByMemberAndPost(member, post);
        int postLike = 0;
        if (optionalLike.isPresent()) {
            likeRepository.delete(optionalLike.get());
            postLike = post.unlike();
        }else {
            postLike = post.like();
            Likes like = Likes.builder()
                    .post(post)
                    .member(member)
                    .build();
            likeRepository.save(like);
        }
        if (postLike >= 20) {
            redisTemplate.opsForValue().set(POPULAR_POST_KEY+id,post);
        }else {
            redisTemplate.opsForValue().getAndDelete(POPULAR_POST_KEY+id);
        }
    }

    public List<PostResDto> postList(Long id) {
        Groups group = groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("그룹이 존재하지 않습니다."));
        List<Post> posts = postRepository.findByGroups(group);
        List<PostResDto> list = new ArrayList<>();
        for (Post post : posts) {
            list.add(post.fromEntity());
        }
        return list;
    }
}
