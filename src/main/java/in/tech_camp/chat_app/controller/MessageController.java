package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.RoomUserRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller//このクラスがコントローラーであることを宣言
@AllArgsConstructor
public class MessageController {
  private final UserRepository userRepository;
  private final RoomUserRepository roomUserRepository;
    @GetMapping("/message")//メッセージ詳細画面になったとき、以下の処理を実行する
  //@AuthenticationPrincipalで、現在のログイン中のユーザーを取得。
  public String showMessages(@AuthenticationPrincipal CustomUserDetail currentUser,Model model){
    //ログインユーザーの情報を取得しただけで、編集画面に飛ぶプログラムは書いていない。
    UserEntity user=userRepository.findById(currentUser.getId());
    model.addAttribute("user",user);
    //ログインユーザーが登録されているレコードをすべて取得。
     List<RoomUserEntity> roomUserEntities = roomUserRepository.findByUserId(currentUser.getId());
     //RoomEntityのリストroomListを作成し、ビューに与えている。
    List<RoomEntity> roomList = roomUserEntities.stream()
        .map(RoomUserEntity::getRoom)
        .collect(Collectors.toList());
    model.addAttribute("rooms", roomList);
    return "messages/index";//messagesディレクトリにあるindex.htmlを返す(表示する)
  }
}
