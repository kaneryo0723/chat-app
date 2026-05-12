package in.tech_camp.chat_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller//このクラスがコントローラーであることを宣言
public class MessageController {
  @GetMapping("/")//ホーム画面になったとき、以下の処理を実行する
  public String showMessages(){
    return "messages/index";//messagesディレクトリにあるindex.htmlを返す(表示する)
  }
}
