package in.tech_camp.chat_app.entity;

import java.util.List;

import lombok.Data;

@Data
public class RoomEntity {
  private Integer id;
  private String name;
  //RoomUserEntityにUserEntityやRoomEntityのフィールドを定義したため、UserEntityやRoomEntityからも
  //RoomEntityの情報にアクセスできるようにフィールドを追加
  //1つのRoomEntityからは、複数のRoomUserEntityが紐づく。(1つのルームは（中間テーブルのデータを）複数持つことができる（＝そのルームには複数のユーザーが紐づくから）)
  private List<RoomUserEntity> roomUsers;
  private List<MessageEntity> messages;
}