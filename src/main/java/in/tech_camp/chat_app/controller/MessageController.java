package in.tech_camp.chat_app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller//このクラスがコントローラーであることを宣言
@AllArgsConstructor
public class MessageController {
  private final UserRepository userRepository;
  @GetMapping("/")//ホーム画面になったとき、以下の処理を実行する
  //@AuthenticationPrincipalで、現在のログイン中のユーザーを取得。
  public String showMessages(@AuthenticationPrincipal CustomUserDetail currentUser,Model model){
    //ログインユーザーの情報を取得しただけで、編集画面に飛ぶプログラムは書いていない。
    UserEntity user=userRepository.findById(currentUser.getId());
    model.addAttribute("user",user);
    return "messages/index";//messagesディレクトリにあるindex.htmlを返す(表示する)
  }
}
