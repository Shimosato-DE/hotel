package com.example.samuraitravel.controller;

import jakarta.servlet.http.HttpServletRequest;

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

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.entity.VerificationToken;
import com.example.samuraitravel.event.SignupEventPublisher;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.service.UserService;
import com.example.samuraitravel.service.VerificationTokenService;

//ログイン画面
@Controller
public class AuthController {

	private final UserService userService;
	private final SignupEventPublisher signupEventPublisher;
	private final VerificationTokenService verificationTokenService;
	//private final HttpServletRequest httpServletRequest;

	public AuthController(UserService userService, SignupEventPublisher signupEventPublisher,
			VerificationTokenService verificationTokenService) {
		this.userService = userService;
		this.signupEventPublisher = signupEventPublisher;
		this.verificationTokenService = verificationTokenService;
		//this.httpServletRequest = httpServletRequest;
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			Model model) {

		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが正しくありません。");
		}

		if (logout != null) {
			model.addAttribute("logoutMessage", "ログアウトしました。");
		}

		return "auth/login";
	}

	//会員登録画面の表示
	@GetMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("signupForm", new SignupForm());

		return "auth/signup";
	}

	//会員登録画面からフォーム受信
	@PostMapping("/signup")
	public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult result,
			RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {

		// メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容
		if (userService.isEmailRegisterd(signupForm.getEmail())) {

			//			FieldErrorクラスのコンストラクタに渡す引数は以下のとおり
			//			第1引数：エラー内容を格納するオブジェクト名
			//			第2引数：エラーを発生させるフィールド名
			//			第3引数：エラーメッセージ
			FieldError fieldError = new FieldError(result.getObjectName(), "email", "すでに登録済みのアドレスです。");

			//BindingResultインターフェースが提供するaddError()メソッド:
			//addError()メソッドにエラー内容を渡すことで、BindingResultオブジェクトに独自のエラー内容を追加することができる
			result.addError(fieldError);
		}

		if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
			FieldError fieldError = new FieldError(result.getObjectName(), "password", "パスワードが一致しません。");
			result.addError(fieldError);
		}

		if (result.hasErrors()) {
			return "auth/signup";

		}

		//userService.create(signupForm);
		//redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました");

		User createdUser = userService.create(signupForm); //DBへfalseで登録し結果を格納
		String requestUrl = new String(httpServletRequest.getRequestURL());//認証用URLドメインの動的生成
		signupEventPublisher.publishSignupEvent(createdUser, requestUrl);//ベント発行　User型のオブジェクトとリクエストURLを返す

		redirectAttributes.addFlashAttribute("successMessage",
				"ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

		return "redirect:/";
	}

	@GetMapping("/signup/verify")
	public String verify(@RequestParam(name = "token") String token, Model model) {

		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

		if (verificationToken != null) {
			User user = verificationToken.getUser();
			userService.enableUser(user);
			String successMessage = "会員登録が完了しました。";
			model.addAttribute("successMessage", successMessage);

		} else {
			String errorMessage = "トークンが無効です。";
			model.addAttribute("errorMessage", errorMessage);

		}

		return "auth/verify";

	}

}
