package com.example.samuraitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.form.SignupForm;

@Controller
public class AuthContoroller {
	
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

}
