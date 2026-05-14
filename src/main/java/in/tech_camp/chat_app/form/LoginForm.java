package in.tech_camp.chat_app.form;

import lombok.Data;

@Data
public class LoginForm {
  //ログイン処理をするためのデータを用意(ログインなのでメールとパスワードだけでok)
   private String email; 
   private String password;
    
}
