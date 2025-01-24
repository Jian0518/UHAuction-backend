package com.utar.uhauction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendSimpleEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("<html><body>" + text + "<br><img src='cid:image'></body></html>", true);
        mailSender.send(mimeMessage);
    }

    @Async
    public void sendEmailWithImage(String to, String subject, String text, String imagePath) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("<html><body>" + text + "<br><img src='cid:image'></body></html>", true);

        UrlResource image = new UrlResource(imagePath);
        helper.addInline("image", image);

        mailSender.send(mimeMessage);
    }
}