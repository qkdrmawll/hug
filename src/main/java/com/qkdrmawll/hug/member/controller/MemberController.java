package com.qkdrmawll.hug.member.controller;

import com.qkdrmawll.hug.common.dto.CommonErrorDto;
import com.qkdrmawll.hug.group.domain.Membership;
import com.qkdrmawll.hug.group.repository.MembershipRepository;
import com.qkdrmawll.hug.member.domain.Member;
import com.qkdrmawll.hug.member.dto.*;
import com.qkdrmawll.hug.member.repository.MemberRepository;
import com.qkdrmawll.hug.member.service.MemberService;
import com.qkdrmawll.hug.common.Auth.JwtTokenProvider;
import com.qkdrmawll.hug.common.dto.CommonResDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class MemberController {
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRT;

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    @Qualifier("2")
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, MembershipRepository membershipRepository, @Qualifier("2") RedisTemplate<String, Object> redisTemplate) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.membershipRepository = membershipRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResDto> memberCreate(@RequestBody MemberCreateDto dto) {
        MemberResDto member = memberService.memberCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "회원 등록 완.", member), HttpStatus.CREATED);
    }
    @PatchMapping("/member/update")
    public ResponseEntity<CommonResDto> memberUpdate(MemberUpdateDto dto) {
        MemberResDto memberResDto = memberService.memberUpdate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "회원 수정 완.", memberResDto), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<CommonResDto> login(@RequestBody loginDto dto) {
        Member member = memberService.login(dto);
        List<Membership> Memberships = membershipRepository.findByMember(member);
        List<String> roles = Memberships.stream()
                .map(membership -> membership.getRole().toString())
                .collect(Collectors.toList());
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(),roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(),roles);
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("accessToken", jwtToken);
        loginInfo.put("refreshToken", refreshToken);
        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 240, TimeUnit.HOURS);

        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "회원이 성공적으로 등록되었습니다.", loginInfo), HttpStatus.CREATED);

    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAccessToken(@RequestBody MemberRefreshDto dto) {
        String rt = dto.getRefreshToken();
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secretKeyRT).parseClaimsJws(rt).getBody();
        }catch (Exception e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.UNAUTHORIZED,"refresh token이 유효하지 않습니다"),HttpStatus.UNAUTHORIZED);
        }
        String email = claims.getSubject();

        Object storesRt = redisTemplate.opsForValue().get(email);
        if (storesRt == null || !storesRt.toString().equals(rt)) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.UNAUTHORIZED,"refresh token이 유효하지 않습니다"),HttpStatus.UNAUTHORIZED);
        }
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));
        List<Membership> Memberships = membershipRepository.findByMember(member);
        List<String> roles = Memberships.stream()
                .map(membership -> membership.getRole().toString())
                .collect(Collectors.toList());

        String newAccessToken = jwtTokenProvider.createToken(email,roles);
        Map<String, Object> info = new HashMap<>();
        info.put("accessToken", newAccessToken);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "토큰이 성공적으로 재발급되었습니다.", info), HttpStatus.CREATED);
    }

}
