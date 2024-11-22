package com.qkdrmawll.hug.member.service;

import com.qkdrmawll.hug.member.domain.Member;
import com.qkdrmawll.hug.member.dto.MemberCreateDto;
import com.qkdrmawll.hug.member.dto.MemberResDto;
import com.qkdrmawll.hug.member.dto.MemberUpdateDto;
import com.qkdrmawll.hug.member.dto.loginDto;
import com.qkdrmawll.hug.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public MemberResDto memberCreate(MemberCreateDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = dto.toEntity(encodedPassword);
        return memberRepository.save(member).fromEntity();
    }

    public Member login(loginDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {

            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return member;
    }
    public MemberResDto memberUpdate(MemberUpdateDto dto) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));
        MultipartFile image= dto.getProfileImage();
        try {
            byte[] bytes = image.getBytes();
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get("/Users/qkdrmawll/Documents/hug/",
                    fileName);
            Files.write(path,bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

//            aws에 pc에 저장된 파일을 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));
            String s3Path = s3Client.utilities().getUrl(a->a.bucket(bucket).key(fileName)).toExternalForm();
            member.update(dto, s3Path);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }
        return member.fromEntity();
    }
    public MemberResDto memberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new EntityNotFoundException("회원을 찾을 수 없습니다"));
        return member.fromEntity();
    }
}
