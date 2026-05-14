package in.tech_camp.chat_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.LoginForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.service.UserService;
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
  public String createUser(@ModelAttribute("userForm") UserForm userForm,Model model) {
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
    
}