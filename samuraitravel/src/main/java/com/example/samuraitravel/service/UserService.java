package com.example.samuraitravel.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Role;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.form.UserEditForm;
import com.example.samuraitravel.repository.RoleRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	public User create(SignupForm signupForm) {
		User user = new User();
		
		Role role = roleRepository.findByName("Role_GENERAL");
		//signupFormからuserエンティティへ詰め替え
		user.setName(signupForm.getName());
		user.setFurigana(signupForm.getFurigana());
		user.setPostalCode(signupForm.getPostalCode());
		user.setAddress(signupForm.getAddress());
		user.setPhoneNumber(signupForm.getPhoneNumber());
		user.setEmail(signupForm.getEmail());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));//パスワードはハッシュ化してからセットする
		user.setRole(role);
		user.setEnabled(false);//メール認証前のためfalseで登録
		//DBへ登録
		return userRepository.save(user);
	}
	
	//update処理
	@Transactional
	public void update(UserEditForm userEditForm) {
		
		User user = userRepository.getReferenceById(userEditForm.getId());
		
		user.setId(userEditForm.getId());
		user.setFurigana(userEditForm.getFurigana());
		user.setPostalCode(userEditForm.getPostalCode());
		user.setAddress(userEditForm.getAddress());
		user.setPhoneNumber(userEditForm.getPhoneNumber());
		user.setEmail(userEditForm.getEmail());
		
		userRepository.save(user);//DB登録
		
	}
	
	
	 // メールアドレスが登録済みかどうかをチェックする
	public boolean isEmailRegistered(String email) {
		User user = userRepository.findByEmail(email);
		
		return user != null; //登録済みであれば userはNullでないとなりTrueを返す。登録済みでなければuserはNullとなりFalseを返す。
		
//		下記のようにも表せる。
//		if(user != null) {
//			return true;
//		}
//		return false;
	}
	
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation); //パスワードとパスワード確認用の一致を確認
	}
	
	
	
	//ユーザを有効にする
	@Transactional
	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	//メールアドレスが登録されたかどうかをチェックする
	public boolean isEmailChanged(UserEditForm userEditForm) {
		
		User currentUser = userRepository.getReferenceById(userEditForm.getId());
		return !userEditForm.getEmail().equals(currentUser.getEmail());
		
	}
	
}
