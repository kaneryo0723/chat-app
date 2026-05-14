package in.tech_camp.chat_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {
  private final UserRepository userRepository;

  //ユーザー情報を取得する処理をオーバーライド
  
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
    UserEntity userEntity=userRepository.findByEmail(email);
    if(userEntity==null){//メアドが空だったらエラーを出す。
      throw new UsernameNotFoundException("User not found with email:"+email);
    }
    //オーバーライドでメアドを渡す処理のみを作っているので、nameとかpasswordとかを宣言しなくても大丈夫。
    return new CustomUserDetail(userEntity);
    
    


  }
}
