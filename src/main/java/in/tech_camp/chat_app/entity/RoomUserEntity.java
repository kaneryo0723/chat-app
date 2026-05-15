package in.tech_camp.chat_app.entity;

import lombok.Data;

@Data
public class RoomUserEntity {
  private Long id;//テーブルのid
  private UserEntity user;//部屋にいるユーザー
  private RoomEntity room;//部屋情報
  //MessageEntityは、「部屋ひとつあたりの発言履歴」なので、「一人のユーザーにすべてのメッセージが紐づく」
  //という構造になってしまう。なのでインポートしない。
}
