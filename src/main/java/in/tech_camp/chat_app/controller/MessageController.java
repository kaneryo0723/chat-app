package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import in.tech_camp.chat_app.entity.MessageEntity;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.MessageForm;
import in.tech_camp.chat_app.repository.MessageRepository;
import in.tech_camp.chat_app.repository.RoomRepository;
import in.tech_camp.chat_app.repository.RoomUserRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.validation.ValidationOrder;
import lombok.AllArgsConstructor;


@Controller//このクラスがコントローラーであることを宣言
@AllArgsConstructor
public class MessageController {
  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final RoomUserRepository roomUserRepository;
  private final MessageRepository messageRepository;
    @GetMapping("/rooms/{roomId}/messages")//フォームから送信され、コントローラーが受け取るためGet
  public String showMessages(@PathVariable("roomId") Integer roomId,@AuthenticationPrincipal CustomUserDetail currentUser,Model model){
    //ログインユーザーの情報を取得しただけで、編集画面に飛ぶプログラムは書いていない。
    UserEntity user=userRepository.findById(currentUser.getId());
    model.addAttribute("user",user);
    //ログインユーザーが登録されているレコードをすべて取得。
     List<RoomUserEntity> roomUserEntities = roomUserRepository.findByUserId(currentUser.getId());
     //RoomEntityのリストroomListを作成し、ビューに与えている。
    List<RoomEntity> roomList = roomUserEntities.stream()
        .map(RoomUserEntity::getRoom)
        .collect(Collectors.toList());
        //ホーム画面のサイドバーにルーム一覧を出力する。
    model.addAttribute("rooms", roomList);

    //メッセージを投稿するためのインスタンスを作成し、ビューに渡す。
    model.addAttribute("messageForm",new MessageForm());

    //roomIdをPathVariableで受け取って、ビューファイルに渡す。
    RoomEntity room=roomRepository.findById(roomId);
     model.addAttribute("room", room);

    //ルーム内のチャットをすべて取得
     List<MessageEntity> messages=messageRepository.findByRoomId(roomId);

     //ビューファイルに渡す
     model.addAttribute("messages",messages);
    return "messages/index";//messagesディレクトリにあるindex.htmlを返す(表示する)
  }
  //フォームからのリクエストをうけとりmessageテーブルに保存するメソッド
  @PostMapping("/rooms/{roomId}/messages")//フォームから送信され、messageテーブルに保存するためpost
  public String saveMassage(@PathVariable("roomId") Integer roomId,@ModelAttribute("messageForm")@Validated(ValidationOrder.class) MessageForm messageForm,BindingResult bindingResult,@AuthenticationPrincipal CustomUserDetail currentUser){
    if(bindingResult.hasErrors())
{
return "redirect:/rooms/"+roomId+"/messages";
}    MessageEntity message=new MessageEntity();//自身のメソッドを使うため、一度newしてあげないといけない。
    message.setContent(messageForm.getContent());//フォームに入力された文章をmessageエンティティに保存

    UserEntity user=userRepository.findById(currentUser.getId());//ログインしているユーザーのid
    RoomEntity room=roomRepository.findById(roomId);//チャットルームのid

    message.setUser(user);
    message.setRoom(room);

    try {
        messageRepository.insert(message);
    } catch (Exception e) {
      System.out.println("エラー："+e);
    }
    


    return "redirect:/rooms/"+roomId+"/messages";
  }

}
