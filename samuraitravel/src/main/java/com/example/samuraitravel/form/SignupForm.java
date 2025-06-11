package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignupForm {

	@NotBlank(message = "氏名を入力して下さい。")
	private String Name;
	
	@NotBlank(message = "フリガナを入力して下さい。")
	private String furigana;
	
	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;
	
	@NotBlank(message = "")
	private String address;
	private String email;
	private String passwordConfirmation;
	
}
