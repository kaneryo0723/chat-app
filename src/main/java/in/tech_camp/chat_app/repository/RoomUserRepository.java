package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.RoomUserEntity;

@Mapper
public interface RoomUserRepository {
  @Insert("INSERT INTO room_users(user_id,room_id) VALUES(#{user.id},#{room.id})")
  @Options(useGeneratedKeys=true,keyProperty="id")
  void insert(RoomUserEntity userRoomEntity);

  // room_users を探したついでに、書いてある room_id を使って rooms テーブルから詳細データも自動で取ってきて、一つの塊にしておいて
  //roomsテーブルを参照していないのにroomsテーブルを参照しに行けるのは、findByIdメソッドにつけているselectアノテーションのおかげ
  @Select("SELECT * FROM room_users WHERE user_id=#{userId}")
  @Result(property="room",column="room_id",
          one=@One(select="in.tech_camp.chat_app.repository.RoomRepository.findById"))
          List<RoomUserEntity> findByUserId(Integer userId);
// Javaで受け取る中身（イメージ）：

// 1件目：userId: 10, roomId: 101, room: { name: "プログラミング部" }

// 2件目：userId: 10, roomId: 102, room: { name: "週末ランチ会" }

}
