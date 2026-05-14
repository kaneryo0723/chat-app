package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.LoginForm;
import in.tech_camp.chat_app.form.UserEditForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.service.UserService;
import in.tech_camp.chat_app.validation.ValidationOrder;//全然別のvalidationOrderをインポートしていた
import lombok.AllArgsConstructor;



@AllArgsConstructor
@Controller
public class UserController {
  private final UserRepository userRepository;//@AllArgsConstructorで初期化してくれる

  private final UserService userService;


  // users/sign_upにアクセスしたとき、以下の処理を実行する。
  //メモ：もしリダイレクトのエラーが起きたらurlを書き換える。
  @GetMapping("/users/sign_up")//localhost以降のurlを指定している。ファイルの場所ではない。
  public String showSignUp(Model model){
    model.addAttribute("userForm", new UserForm());//pageUp.htmlにて、userFormの変数を参照しているため、これを使う。
    return "users/signUp";//signUp.htmlを返す
  }

  //サインアップ以降の処理
  @PostMapping("/user")//SecurityConfig.javaに記載されている通りのルーティング。
  public String createUser(@ModelAttribute("userForm") @Validated(ValidationOrder.class) UserForm userForm,BindingResult result,Model model) {
    //バリデーションチェックを追記
    userForm.validatePasswordConfirmation(result);
    //メールアドレスが既に存在したらエラーを出す
    if(userRepository.existsByEmail(userForm.getEmail())){
      result.rejectValue("email", "null","Email already exists");
    }

    if(result.hasErrors()){
      List<String> errorMessages=result.getAllErrors().stream()
                  .map(DefaultMessageSourceResolvable::getDefaultMessage)
                  .collect(Collectors.toList());//エラーを格納する処理

      model.addAttribute("errorMessages",errorMessages);
      return "users/signUp";
    }
    

    //サインアップ画面で入力した諸々をエンティティファイルに格納する処理
      UserEntity userEntity=new UserEntity();
      userEntity.setName(userForm.getName());
      userEntity.setEmail(userForm.getEmail());
      userEntity.setPassword(userForm.getPassword());

      try {
          userService.createUserWithEncryptedPassword(userEntity);//パスワードの暗号化兼リポジトリに保存
      } catch (Exception e) {
        System.out.println("エラー：" + e);
        model.addAttribute("userForm",userForm);//要確認：多分リセット？引数でもらったuserFormを入れてる。
        return "users/signUp";
      }
      
     
      return "redirect:/";
  }
   @GetMapping("users/login")//こっちもファイルを指定しているというよりかは、
                            //SecurityConfig.javaで指定しているルーティングという認識
       public String loginForm(Model model){
      model.addAttribute("loginForm",new LoginForm());//インスタンスを作成
      return "users/login";//login.htmlを表示。
       }

   @GetMapping("/login")//SecurityConfig.javaからログイン情報を取得。springBoot ログイン失敗処理 controllerと検索
   //@RequestParamとは、URLのクエリパラメータ（?key=value形式）をコントローラのメソッド引数として受け取るためのアノテーションを指します。
    //@ModelAttributeは、リクエストパラメータをオブジェクトに変換し、Controllerの引数として受け取れるアノテーションです。
    //リクエストパラメータは、WebサイトやAPIに「こんな情報を送るよ！」と伝えるためのデータのことです。今回はログインフォームのデータを送る。
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,@ModelAttribute("loginForm") LoginForm loginForm, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "メールアドレスかパスワードが間違っています。");
        }
        return "users/login";
    }
    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable("userId")Integer userId, Model model) {
        UserEntity user=userRepository.findById(userId);//userIdを探さないと始まらない。
        UserEditForm userForm=new UserEditForm();//編集用のインスタンス
        //編集画面に遷移したとき、名前とメアドが入力されている状態にする。
        //edit.htmlに変数とか諸々書いてあって、そこに入れるイメージ。
        userForm.setId(user.getId());
        userForm.setName(user.getName());
        userForm.setEmail(user.getEmail());
        
        model.addAttribute("user",userForm);
        return "users/edit";
    }
     @PostMapping("/users/{userId}")
    public String updateUser(@PathVariable("userId") Integer userId, @ModelAttribute("user") @Validated(ValidationOrder.class) UserEditForm userEditForm, BindingResult result, Model model){
      //ユーザー作成メソッドに引き続き、編集メソッドでもバリデーションチェックを実装
      String newEmail = userEditForm.getEmail();
    if (userRepository.existsByEmailExcludingCurrent(newEmail, userId)) {
      result.rejectValue("email", "error.user", "Email already exists");
    }
    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("user", userEditForm);
      return "users/edit";
    }
      
    //上書き処理
      UserEntity user=userRepository.findById(userId);
        user.setName(userEditForm.getName());//編集したものを既存のデータに上書きするからuserが先に来る。
        user.setEmail(userEditForm.getEmail());
        //データベースを書き換える処理なので、try-catchを書く。
        try {
          userRepository.update(user);
      } catch (Exception e) {
        System.out.println("エラー：" + e);
        model.addAttribute("user",userEditForm);//要確認：多分リセット？引数でもらったuserFormを入れてる。
        return "users/edit";
      }
   //画面変更はしないのでaddAttributeは記載しない。
    return "redirect:/";//signUp.htmlを返す
  }

}