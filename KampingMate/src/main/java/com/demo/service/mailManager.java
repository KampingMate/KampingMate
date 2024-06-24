package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class mailManager {
    
    @Value("${spring.mail.username}")
    private String sender;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    public void send(String sendTo, String sub, String con) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(sender);
        mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(sendTo));
        mimeMessage.setSubject(sub);
        mimeMessage.setText(con);
        javaMailSender.send(mimeMessage);
    }
    
    public void sendCompleteEmail(String email) throws Exception{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        
        helper.setFrom(sender);
        helper.setTo(email);
        helper.setSubject("예약이 완료되었습니다");
        
     // HTML 본문 생성
        String htmlMsg = "<h3>KampingMate의 예약이 확정되었습니다.</h3>"
                + "<p>예약이 성공적으로 완료되었습니다.</p>"
                + "<img src='https://lh3.googleusercontent.com/fife/ALs6j_EMyq5kJk8oY9y_iZimqKGj-nCVmjTOl3qrHmXzXesoRinGx1916jSbJZApfTy-FeE_ByaVa6KtoX9Ctlzcz_lV1AWqdOxCTWCjawBIdJ_ru3dBqFvtMkXJxds3eU_sW8WOTKiuSGGhtz-cuDFKUtePlB2VH6HhQjebiey-nLDn01xvjeOAaDO3sXphwtsLUj1lgpoH26pk1VUdlDTTOWPM7d0lb2tsmenTFh572jeVePCojS6SJQp-SgH9TWYCVGlHGMkFC0AVeOAq-Qk3N2Ew_ARCVjSh9hOv1jTgYEx9W_DRbAPu6p1EYxgH64_wDaB3J68UDM0jkBHtmQHtehb1FZxTABbKau9-m8S5EBdWk36beinoxjPQuhGNAi4GOw1aB_vS3hKrWJKdPhOem1xXGzzzCC-G0it5gTYH2IwuZZnjzK5o7Iqz4LDeSP51L2oWlthLwcrVHx4zq4ZPrCW-bvOikfGOYgQfP3nZz98mN27n5OjZ5Mh7IIsoJfhDgCDRuK8CEk1gmUUhDAsuZ5-Rgvx3bsrI63XYNA3t_iEakQ_GkF0pqAPrIRmHlHYZn9LFyhA9s36u_CRiJFp7rXu8kCMiYmDKPDSxq8M_Nvp6anY7mX6an17UUY5C89OYA_Zz7eJZiXIsdux0d0dGR-FojNG3Cw-gAjOjPqpVNr931F7lAoeDbm6zsWby9RcNfs0OMlvFBqYiazutMERKH8seaZdXON_yUIzl295ScxfKXi5dveyvO2eKIZQwg-IzNlwro5dZ1WOx_lnonSkTay_8vURh_zitdKDYaen8MuYatQ5pacCrIdErw0Osmpd8sgtHGEkibMhjowXhUhbaKL29kYEu-u4nKOT4WHshb0LEy9H9gHRrJayOGCX8cGqB1WITgjOGPQDcmRhdEBgwldYeDmAZiToMCaGlCUgkP2ZX7_azB80D1rVkJYXWF8o5sRU2AOdbBYG7Xe8alsj-1S79juZvScCBctRMeFOhB6CoooW_srtoUP_IyH5gbeqeZ0H3HPlLFkNa3q0MHUp_JrGowFjXXzLVgzRo2xXcK06hJ1W5XolxElQ58hfQCoqUweZH3tlLlPh_JrWpu_9lfPd5lfTJ74zSiR8xPoTOYgyrQ4-ETYWldvH2hIvUZ5TsdoeZ45soov18smZqBovQgHrKhgbAOsyn9RN2y8FTzZ44AOdWF-bsilDW9q7_eKj7h_bESXw1tTf-mXcZyZJ7pc51_zJVh9PzPVCjm5f0RAAh1GXrv44yP2AQqBS4QP-GNyloR_QPYsSflspL8lpOVetAmoFEkN4MOLtvMHMEIdDw7pr7XIZSlv1AsbicabqE2S7mKRwyZrZlHqWceN0lTK1IoJgMy_YcSueP2UXX9U5ZOJA-N_7XUMDHwxzZhbrzMLUAqS88utbadr7Y7sEthf3SwWr1dNrQWSMCU94QQBeCq390995l7t9bbzL_nO3K3OZ_TlJnbqe1IhiTQINiJSjycpQXL1dqyFOnlejs01_JxI2TtYakYJ1rxAwui1WJbO9gw3tcbSCGIfUBCobIJ-nuj57BiobVLJw-4tyIPuObS1oUFRDqYdKrcKMq89KypFpmYIFy-1HPyfsW-a5KlQ=w1116-h801' alt='test image'>";
        
        helper.setText(htmlMsg, true);  // HTML 형식으로 설정
         javaMailSender.send(mimeMessage);
    }

    public void sendFailEmail(String email) throws Exception{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
         mimeMessage.setFrom(sender);
         mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(email));
         mimeMessage.setSubject("예약실패");	//제목
         mimeMessage.setText("KampingMate 예약 실패 ㅠㅠ");	// 내용
         javaMailSender.send(mimeMessage);
    }
}
