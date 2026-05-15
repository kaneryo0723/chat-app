package in.tech_camp.chat_app.entity;

import lombok.Data;

@Data
public class RoomUserEntity {
  private Long id;//テーブルのid
  private UserEntity user;//部屋にいるユーザー
  private RoomEntity room;//部屋情報
}
