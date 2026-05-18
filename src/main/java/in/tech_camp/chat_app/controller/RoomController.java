package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.RoomForm;
import in.tech_camp.chat_app.repository.RoomRepository;
import in.tech_camp.chat_app.repository.RoomUserRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.validation.ValidationOrder;
import lombok.AllArgsConstructor;



@Controller
@AllArgsConstructor
public class RoomController {
  private final UserRepository userRepository;//@AllArgsConstructorで初期化してくれる
   private final RoomRepository roomRepository;

  private final RoomUserRepository roomUserRepository;

  @GetMapping("/")
  public String index(@AuthenticationPrincipal CustomUserDetail currentUser,Model model) {
   // 現在ログインしているユーザーのIDを使って、データベースからユーザーの詳細情報を取得します
    UserEntity user = userRepository.findById(currentUser.getId());
    // 取得したユーザー情報を、画面（View）に渡すために「user」という名前でmodelに登録します
    model.addAttribute("user", user);
    // ログインユーザーのIDを元に、「どのルームに所属しているか」の中間データをリストで取得します（ここで@Oneの仕組みが動き、Room情報も各Entityにセットされます）
    List<RoomUserEntity> roomUserEntities = roomUserRepository.findByUserId(currentUser.getId());
    // 中間データのリスト（roomUserEntities）から、中に入っている「Room情報」だけを取り出して、新しいリストに変換します
    List<RoomEntity> roomList = roomUserEntities.stream() // リストを流れ作業（Stream）の状態にする
    .map(RoomUserEntity::getRoom)                    // 各データからRoomEntity（ルーム本体の情報）だけを抽出する
    .collect(Collectors.toList());                  // 抽出した結果を再びリスト形式にまとめる
    // 抽出した「ルーム情報のリスト」を、画面に渡すために「rooms」という名前でmodelに登録します
    model.addAttribute("rooms", roomList);
    // "rooms/index.html"（一覧画面のテンプレートファイル）を呼び出して表示します
return "rooms/index";
  }
  

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

 @PostMapping("/rooms")
  public String createRoom(@ModelAttribute("RoomForm") @Validated(ValidationOrder.class) RoomForm roomForm, BindingResult bindingResult,@AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    //バリデーションチェック
      if (bindingResult.hasErrors()) {
      List<String> errorMessages = bindingResult.getAllErrors().stream()
                              .map(DefaultMessageSourceResolvable::getDefaultMessage)
                              .collect(Collectors.toList());
      List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
      model.addAttribute("users", users);
      model.addAttribute("roomForm", roomForm);
      model.addAttribute("errorMessages", errorMessages);
      return "rooms/new";
    }
    RoomEntity roomEntity = new RoomEntity();
    roomEntity.setName(roomForm.getName());
    try {
      roomRepository.insert(roomEntity);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
      model.addAttribute("users", users);
      model.addAttribute("roomForm", new RoomForm());
      model.addAttribute("currentUserId", currentUser.getId()); // 追加
      return "rooms/new";
    }

    List<Integer> memberIds = roomForm.getMemberIds();
    for (Integer userId : memberIds) {
      UserEntity userEntity = userRepository.findById(userId);
      RoomUserEntity roomUserEntity = new RoomUserEntity();
      roomUserEntity.setRoom(roomEntity);
      roomUserEntity.setUser(userEntity);
      try {
        roomUserRepository.insert(roomUserEntity);
      } catch (Exception e) {
        System.out.println("エラー：" + e);
        List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("roomForm", new RoomForm());
        return "rooms/new";
      }
    }    
    return "redirect:/";
  }
  @PostMapping("/rooms/{roomId}/delete")
  public String deleteRoom(@PathVariable Integer roomId) {
    roomRepository.deleteById(roomId);
    return "redirect:/";
  }
}