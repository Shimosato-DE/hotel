package com.example.samuraitravel.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.service.VerificationTokenService;

@Component
public class SignupEventListener {

	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender javaMailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = javaMailSender;
	}
	
	
	//SignupEventから通知を受けて、認証用のメールを送付する
	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {
		User user = signupEvent.getUser(); //SignupEventオブジェクトのuserを取得
		String token = UUID.randomUUID().toString(); //UUIDを用いてランダムなトークンを生成
		verificationTokenService.create(user, token);
		String recipientAddress = user.getEmail(); //Userからメールアドレスを取得
		String subject = "メール認証"; //メールの件名設定
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;//生成したトークンを認証用のURLとして埋め込む
		String message = "以下のリンクをクリックして会員登録を完了してください。"; //メールの本文設定
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(recipientAddress);//宛先
		mailMessage.setSubject(subject);//件名
		mailMessage.setText(message + "\n" + confirmationUrl);//本文+ 改行 + 認証用URL
		javaMailSender.send(mailMessage);//上記情報で送信
	}
}
