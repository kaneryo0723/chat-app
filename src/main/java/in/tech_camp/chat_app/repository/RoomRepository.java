package in.tech_camp.chat_app.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.RoomEntity;

//チャットルームの保存
@Mapper
public interface RoomRepository {
  @Insert("INSERT INTO rooms(name) VALUES(#{name})")
  @Options(useGeneratedKeys=true,keyProperty="id")
  void insert(RoomEntity roomEntity);

  // RoomUserRepositoryで使う用のメソッド
  @Select("SELECT * FROM rooms WHERE id = #{id}")
  RoomEntity findById(Integer id);

  @Delete("DELETE FROM rooms WHERE id = #{id}")
  void deleteById(Integer id);
  
}
