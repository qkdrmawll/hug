package com.qkdrmawll.hug.group.contoller;

import com.qkdrmawll.hug.common.dto.CommonResDto;
import com.qkdrmawll.hug.group.dto.GroupCreateDto;
import com.qkdrmawll.hug.group.dto.GroupInviteDto;
import com.qkdrmawll.hug.group.dto.GroupResDto;
import com.qkdrmawll.hug.group.dto.MembershipResDto;
import com.qkdrmawll.hug.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/group/create")
    public ResponseEntity<CommonResDto> groupCreate(@RequestBody GroupCreateDto dto){
        GroupResDto groupResDto = groupService.groupCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "그룹 생성 완", groupResDto), HttpStatus.OK);
    }
    @DeleteMapping("/group/delete/{id}")
    public ResponseEntity<CommonResDto> groupDelete(@PathVariable Long id){
        groupService.groupDelete(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "그룹 삭제 완", null), HttpStatus.OK);
    }

    @PostMapping("/group/invite")
    public ResponseEntity<CommonResDto> invite(@RequestBody GroupInviteDto dto) {
        groupService.groupMemberInvite(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "멤버 초대 완", null), HttpStatus.OK);
    }
    @GetMapping("/invite/accept")
    public String inviteAccept(String token) {
        MembershipResDto dto = groupService.inviteAccept(token);
        return "WELCOME TO HUUUUUUUUUG";
    }


}
