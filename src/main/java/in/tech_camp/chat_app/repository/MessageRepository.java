package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.MessageEntity;

@Mapper
public interface MessageRepository {
  @Insert("INSERT INTO messages(content, image,user_id, room_id) VALUES(#{content},#{image},#{user.id},#{room.id})")
  @Options(useGeneratedKeys=true,keyProperty="id")
  void insert(MessageEntity messageEntity);

  //指定したルームIDのメッセージ一覧を取得し、それぞれのメッセージに『投稿したユーザーの情報』と『作成日時』を正しくセットしてJavaのリストとして返す
  @Select("SELECT * FROM messages WHERE room_id = #{roomId}")//引数で受け取った部屋を探し、そこで記載されたメッセージをすべて取得する。
  @Results(value={
    @Result(property="createdAt",column="created_at"),//左にEntityの変数、右にテーブルの変数(sqlのやつ)を書く。
    @Result(property="user",column="user_id",
            one=@One(select="in.tech_camp.chat_app.repository.UserRepository.findById"))//user_idを引数として、findById()を実行
  })
  List<MessageEntity> findByRoomId(Integer roomId);

  @Select("SELECT COUNT(*) FROM messages")
  int count();
}
