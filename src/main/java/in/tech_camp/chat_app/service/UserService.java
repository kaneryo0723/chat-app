package in.tech_camp.chat_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;

@Service
public class UserService {
   @Autowired // Springが自動的にUserRepositoryの実装を注入します
    private UserRepository userRepository;

    @Autowired // Springが自動的にPasswordEncoderの実装を注入します
    private PasswordEncoder passwordEncoder;

  //パスワードを暗号化し、エンティティファイルのパスワード欄に上書きする
    public void createUserWithEncryptedPassword(UserEntity userEntity) {
      String encodedPassword=encodePassword(userEntity.getPassword());
      userEntity.setPassword(encodedPassword);//パスワードをセットするだけでは、こっちのインスタンスだけが変更されているのでダメ。
      userRepository.insert(userEntity);//大元が保存されているrepositoryにしっかり保存する。このコードがあるからcontrollerでinsertを書かなくていい。
    }
    // パスワードをハッシュ化してから保存
    private String encodePassword(String password){
      return passwordEncoder.encode(password);
    }
        
       
}
