package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;

//イベントを発行するクラス(Publisherクラス)
@Component
public class SignupEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void publishSignupEvent(User user, String requestUrl) {//signupが完了したUserオブジェクトと認証リンクを受け取る
		
		applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
		//SignupEventにはサインアップしたUserオブジェクトとリクエストのURLが含まれ生成される。
		//引数には発行したいEventクラス（今回はSignupEventクラス）のインスタンスを渡す
	}

}
