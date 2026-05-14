package in.tech_camp.chat_app.form;
import lombok.Data;

@Data
public class UserForm {
  //サインアップ処理をするためのデータを用意(idはDB専用なのでいらない)
    private String name;
    private String password;
    private String email;
    private String passwordConfirmation;
}
