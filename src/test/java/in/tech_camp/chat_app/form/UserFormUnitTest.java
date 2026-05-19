package in.tech_camp.chat_app.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import in.tech_camp.chat_app.factories.UserFormFactory;
import in.tech_camp.chat_app.validation.ValidationPriority1;
import in.tech_camp.chat_app.validation.ValidationPriority2;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
@SpringBootTest
public class UserFormUnitTest {
  private UserForm userForm;

  private Validator validator;

  private BindingResult bindingResult;

  @BeforeEach
  public void setUp() {
    userForm = UserFormFactory.createUser();//ダミーデータを使ってユーザーを作成

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();//Validatorオブジェクトを作成
    validator = factory.getValidator();//validatorオブジェクトの取得。これでvalidationの機能が使えるようになる。
    bindingResult=Mockito.mock(BindingResult.class);
  }

  @Nested
  class ユーザーを作成できる場合 {
    @Test
    public void nameとemailとpasswordとpasswordconfirmationが存在すれば登録できる () {
      //入力されたデータが正しいかをチェックする。正しければサイズが0となる。
      Set<ConstraintViolation<UserForm>> violations=validator.validate(userForm, ValidationPriority1.class);
      //期待する値と実際の値が一致しているかを判定
      assertEquals(0, violations.size());
    }
  }

  @Nested
  class ユーザーを作成できない場合 {
    @Test
    public void nameが空では登録できない () {
      userForm.setName("");//名前を空に
      Set<ConstraintViolation<UserForm>> violations=validator.validate(userForm,ValidationPriority1.class);
      assertEquals(1, violations.size());
      //中身を一つずつ取り出すiterator()と、先頭にある最初の１件を取り出すnext()を組み合わせている。
      //そして、エラー文が入っているはずなので、getMessage()で取得。
      assertEquals("Name can't be blank", violations.iterator().next().getMessage());


    }

    @Test
    public void emailが空では登録できない () {
      //nameと同じ処理なので割愛
      userForm.setEmail("");
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("Email can't be blank", violations.iterator().next().getMessage());
    }
     @Test
    public void emailはアットマークを含まないと登録できない() {
      userForm.setEmail("invalidEmail");
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority2.class);
     assertEquals(1, violations.size());
      assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }


    @Test
    public void passwordが空では登録できない() {
     userForm.setPassword("");
     Set<ConstraintViolation<UserForm>> violations=validator.validate(userForm,ValidationPriority1.class);
     assertEquals(1,violations.size());
     assertEquals("Password can't be blank",violations.iterator().next().getMessage());
    }

     @Test
    public void passwordが5文字以下では登録できない() {
      userForm.setPassword("aiueo");
      Set<ConstraintViolation<UserForm>> violations=validator.validate(userForm,ValidationPriority2.class);
      assertEquals(1, violations.size());
      assertEquals("Password should be between 6 and 128 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordが129文字以上では登録できない() {
      userForm.setPassword("a".repeat(129));
      Set<ConstraintViolation<UserForm>> violations=validator.validate(userForm,ValidationPriority2.class);
      assertEquals(1, violations.size());
      assertEquals("Password should be between 6 and 128 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordとpasswordConfirmationが不一致では登録できない() {
      
       userForm.setPasswordConfirmation("differentPassword");
       //validatePasswordConfirmation()の引数がbindingResultだから、bindingResultを宣言する。
       userForm.validatePasswordConfirmation(bindingResult);
       verify(bindingResult).rejectValue("passwordConfirmation","error.user","Password passwordConfirmation doesn't match Password");
    }
  }
}