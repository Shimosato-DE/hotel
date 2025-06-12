package com.example.samuraitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.service.UserService;

@Controller
public class AuthController {
	
	private final UserService userService;
	
	public AuthController(UserService userService) {
		this.userService = userService; 
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model){
		
		if (error != null) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが正しくありません。");
        }
        
        if (logout != null) {
            model.addAttribute("logoutMessage", "ログアウトしました。");
        }
		
		return "auth/login";
	}
	
	
	
	
	@GetMapping("/signup")
	public String signup(Model model){
		
		model.addAttribute("signupForm", new SignupForm());
		
		return "auth/signup";
	}
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {
		
		if(userService.isEmailRegisterd(signupForm.getEmail())) {
			
//			FieldErrorクラスのコンストラクタに渡す引数は以下のとおり
//			第1引数：エラー内容を格納するオブジェクト名
//			第2引数：エラーを発生させるフィールド名
//			第3引数：エラーメッセージ
			FieldError fieldError = new FieldError(result.getObjectName(), "email", "すでに登録済みのアドレスです。");
			
			//BindingResultインターフェースが提供するaddError()メソッド:
			//addError()メソッドにエラー内容を渡すことで、BindingResultオブジェクトに独自のエラー内容を追加することができる
			result.addError(fieldError);
		}
		
		if(result.hasErrors()) {
			return "auth/signup";
		}
		
		userService.create(signupForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました");
		
		return "redirect:/";
	}

}
