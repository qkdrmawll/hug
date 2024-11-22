package com.qkdrmawll.hug.post.controller;

import com.qkdrmawll.hug.common.dto.CommonResDto;
import com.qkdrmawll.hug.post.dto.PostCreateDto;
import com.qkdrmawll.hug.post.dto.PostResDto;
import com.qkdrmawll.hug.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post/create")
    public ResponseEntity<CommonResDto> postCreate(@RequestBody PostCreateDto dto) {
        PostResDto postResDto = postService.postCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "글 등록 완.", postResDto), HttpStatus.CREATED);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<CommonResDto> postDetail(@PathVariable Long id) {
        PostResDto postResDto = postService.postDetail(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "글 조회 완.", postResDto), HttpStatus.CREATED);
    }
    @PostMapping("/post/{id}/like")
    public ResponseEntity<CommonResDto> postLike(@PathVariable Long id) {
        postService.postLike(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "좋아요 완.", null), HttpStatus.CREATED);
    }
    @GetMapping("/group/{id}/posts")
    public ResponseEntity<CommonResDto> postList(@PathVariable Long id) {
        List<PostResDto> postResDtoList = postService.postList(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "좋아요 완.", postResDtoList), HttpStatus.CREATED);
    }
}
