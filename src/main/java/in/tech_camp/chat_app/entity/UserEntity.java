package in.tech_camp.chat_app.entity;

import java.util.List;

import lombok.Data;


@Data//getterとsetterを省略できる
public class UserEntity {
    private Integer id;
    private String name;
    private String email;
    private String password;
    //RoomUserEntityにUserEntityやRoomEntityのフィールドを定義したため、UserEntityやRoomEntityからも
    //RoomEntityの情報にアクセスできるようにフィールドを追加
    //1つのUserEntityからは、複数のRoomUserEntityが紐づく。(1人のユーザーは（中間テーブルのデータを）複数持つことができる（＝複数のルームに所属できるから）)
    private List<RoomUserEntity> roomUsers;
      private List<MessageEntity> messages;
}
