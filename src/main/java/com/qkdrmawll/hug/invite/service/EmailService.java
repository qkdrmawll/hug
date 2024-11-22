package com.qkdrmawll.hug.invite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Value("${app.baseUrl}")
    private String baseUrl;

    public void sendInviteEmail(String to, String groupName, String token) {
        String inviteUrl = "http://" + baseUrl + "/invite/accept?token=" + token;
        String htmlContent = "<p>그룹 <strong>" + groupName + "</strong>에 초대되었습니다.</p>" +
                "<p>초대를 수락하려면 여기를 클릭하세요: <a href=\"" + inviteUrl + "\">초대 수락하기</a></p>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject("그룹 초대");
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
