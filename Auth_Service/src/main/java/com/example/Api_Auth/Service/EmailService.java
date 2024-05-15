package com.example.Api_Auth.Service;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("mehdibenhmidi51@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sendParrainerMail(String EmailAmi, String EmailSender) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(EmailAmi);
            helper.setSubject("Invitation à rejoindre notre communauté");
            String htmlContent = "<p>Bonjour,</p>"
                    + "<p>Je voulais vous faire savoir que je suis membre de notre communauté et j'en suis très satisfait.</p>"
                    + "<p>J'aimerais vous inviter à nous rejoindre !</p>"
                    + "<p>Inscrivez-vous dès maintenant et profitez de tous nos avantages.</p>"
                    + "<p>Cordialement,<br/>" + EmailSender + "</p>";
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            // Gérer les erreurs d'envoi d'email
            e.printStackTrace();
        }
    }
}
