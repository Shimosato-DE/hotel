package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEvent;

import com.example.samuraitravel.entity.User;

import lombok.Getter;


//Listenerクラスにイベントが発生したことを知らせるクラス
@Getter
public class SignupEvent extends ApplicationEvent { //イベントのソース（発生源）などを保持するクラス

	private User user;
	
	private String requestUrl;
	
	public SignupEvent(Object source, User user, String requestUrl) {
		super(source);
		
		this.user = user;
		
		this.requestUrl = requestUrl; 
	}
	
	
}
