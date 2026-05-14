package in.tech_camp.chat_app.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.tech_camp.chat_app.validation.ValidationPriority1;
import in.tech_camp.chat_app.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserForm {
  //サインアップ処理をするためのデータを用意(idはDB専用なのでいらない)
  //バリデーション一覧→さらに絞るの順で検索する
  //バリデーションは、springBoot エラー 優先順位と検索すると出てきやすい
    @NotBlank(message="Name can't be blank",groups=ValidationPriority1.class)
    private String name;

    @NotBlank(message="Email can't be blank",groups=ValidationPriority1.class)
    @Email(message = "Email should be valid", groups = ValidationPriority2.class)
    private String email;

    @NotBlank(message="Password can't be blank",groups=ValidationPriority1.class)
    @Length(min = 6, max = 128,message="Password should be between 6 and 128 characters", groups = ValidationPriority2.class)
    private String password;
    private String passwordConfirmation;

    //BindingResult bindingResult は、入力されたデータと検証結果（エラーがあるかどうか）を保持するためのアノテーション
    public void validatePasswordConfirmation(BindingResult result){
      //rejectValue:指定されたエラーの説明を使用して、現在のオブジェクトの指定されたフィールドのフィールドエラーを登録します
      if(!password.equals(passwordConfirmation)){
        result.rejectValue("passwordConfirmation","error.user","Password passwordConfirmation doesn't match Password");
      }
    }
    
}
