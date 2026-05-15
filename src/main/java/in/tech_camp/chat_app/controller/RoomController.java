package in.tech_camp.chat_app.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.RoomForm;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RoomController {
  private final UserRepository userRepository;//@AllArgsConstructorで初期化してくれる
  @GetMapping("/rooms/new")
  //チャット作成画面の表示
  //ルーム関連はこのコントローラーに書く。
  //注意：アカウントが2つ以上ないと相手がいないのでプルダウンに表示されない。
  public String showRoomNew(@AuthenticationPrincipal CustomUserDetail currentUser,Model model) {
     List<UserEntity> users=userRepository.findAllExcept(currentUser.getId());
    model.addAttribute("users",users);//キーはthymeleafで使う。
    model.addAttribute("roomForm",new RoomForm());
      return "rooms/new";
  }
  
}
