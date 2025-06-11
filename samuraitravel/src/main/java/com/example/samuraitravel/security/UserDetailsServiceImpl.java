package com.example.samuraitravel.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	
	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		try {
			
			User user = userRepository.findByEmail(email);//emailを引数として受取、findByEmailの呼び出し結果をuserに格納
			String userRoleName = user.getRole().getName();//userのロールを取得し、関連づいているロールオブジェクトのnameフィールドを取得。
			
			Collection<GrantedAuthority> authorities = new ArrayList<>();//権限情報を格納するListを生成
			
			authorities.add(new SimpleGrantedAuthority(userRoleName));//取得したユーザのロール名を元にSimpleGrantedAuthorityインスタンスの生成。これにより、Spring Security はこのユーザーがどの役割を持っているかを認識できる。
			
			return new UserDetailsImpl(user, authorities);//UserDetailsImplにemailとauthorithisの情報を渡してインスタンス生成して返す
			
		}catch(Exception e) {

			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}

	}
	
}
