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
                + "<img src='https://lh3.googleusercontent.com/fife/ALs6j_En5TDpRfxUKwPpA5yZjPGt7kaTC8WY9IwHJUfQBtlTB04jvs-1NfMugNyw48M53gFdOLqQQj8717XtfSXYFmKPTaOVB1EGAN1U6oD8_kMtsqccNzh8F6MxHgo-jv2bG6Y-zFFANVJq8cOILNTetPDCApQA8pUj6k7ofJRyVC_StRG6lVoNaoirLb4BWM7-08YTtWF9hmQL1oFsPQyviiuTowkVxAvMvtgsVuXuwEIwbo5m8wo30JWnysAwOfiZ1AubyA1B80fGEFLtzW_vJvB7WUrWiSGSF7GNsLCCCkvYwKnzO9mCrjbFDFNk4VO9lEwmaH3JRTaDA1XMYt1S4W4cu6WWKt7vSVmIu7bTjgvKtxMfQi15fX2CbA40MHlti_0asRjl8sNXZUQRpv1DsFs8nZ_fZ1Gxtb5zYQV5HTi_EWw7SDipJQjeB4rjY8xvCkERtEKedCfEhIUDWzInmPRJRM4nuQGt_4HNbIbTrMlIAzE34glxDCLeaygCHqQRu7P38v9HruhzOuYXjP0sXxVrQkwJW4Mxrq97nFf8AVE5UWHm5p1C-v85RaPv45pyITmc9I5s_ZDETo7y-yZSa0HqWl5tKfHxZqAI4JrBHdEPUqwa3A1v0y-4yp-bpE1pnaa9l2OixMlmKBCRcP8GhOWIyn5RQcl6HBrQ1dJ8FdDwE8ZFZ6uH8MznGEM9G27NDckFZjiKnsXixIooMuOfU15OoeqhxBE9j-hbM3E0ehzgOmoHI4bQVVkg3jRo9VpL1E2Wi6y-RhnAcnYyPC5RyOjs30kf3CqXM_VxUJM3uaMosIEk07nBxCiUN3H1UB1BcKPrEwQrOnQdaIfTQDuyp7UwjCJghEhgoof-xHzdUJ14C4oalqDk_rxCK3K6Ax2-FBw9ov6w7syUHkDTrKdGKitFiYYCDlPzfX6dwA5_XKuqpksuZwGN2f7xhrSl5bKV0zdmq6_CiQNFe6sTtg124zYmxV6jwD7WvmmErhg_GUYEGNNro-ZNEjt0rHoSph37yfy3sesnfPRHvhBEMipElgU8q8hRKwCQ-BQRn5hPJ_9xNrbQhGrwgAOLdvvTFD1FtpQ_QTT0GyaDSVsdcTlJBC5IwSqBt7v_lB9JV7qeJ19bJQXP-qVK-D4jnNQ-mWep8QEICjU_BXLGgjmw34rTiCa379iRmWdsicJFGKJ1KrK8kEkHGqCCrNDvCwWKjkaXPIv1leTJ2wWG9mMT_PFYMdPhFVpd8GH24JAqLAJnsPepMuWR3GRhsxq4TqlCxyUNyx91sF-7MRWervIs7tpWKbQE_DniOV32pUiF6Yw-2OIidRrPWmo6nl8w-3HTASczwg3cZms3mp6hz4SFtilAHseBehOPgSCKh5qrLdxwtCQsdc_mMiNBMHH4X9z-oYy4Z4c4qIAC2BavBkfnIFqj3vhPRgLb-BSoy18ODpbaVsPqjmZb3dSQeZmPwpE7In0fcCYxGJ4vemJinbQZB7n9VaOEt05L4SWQ7h7S5mEnbkUffP403Sc6wbLmjMdundTf2w1mbPGd1xti6_JdqqOrkc7KKtBfF0uk_vVZ8HqFzxJoEQTzafpprE53a_chW9zRC9iUQvV9MCU55vMKMEQ=w1920-h953' alt='test image'>";
        
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
