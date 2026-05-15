package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.chat_app.entity.UserEntity;

@Mapper
public interface UserRepository {
  //users(カラム名)、VALUES(実際に入れる値)
  @Insert("INSERT INTO users(name,email,password) VALUES (#{name}, #{email}, #{password})")
  //useGeneratedKeys=true:データベース側で自動生成されたキーを取得
  // keyProperty="id":取得した値をJavaオブジェクトのどのフィールドに代入するかを指定。
  @Options(useGeneratedKeys=true,keyProperty="id") 
  void insert(UserEntity user);

  //UserEntity型は、データが一つしか取得されないときに使う。複数取得するときはList<UserEntity>とする。
  //emailをすべて選択する処理
  //UserAuthenticationService.javaを見るとわかりやすいかも
  //UserEntity型のデータを返す。というかselectって選択したデータを返すから返り値のないvoidは使えない。
  @Select("SELECT * FROM users WHERE email=#{email}")
  UserEntity findByEmail(String email);//emailのみをもらうので、userEntityクラスは使えない。
  
  @Select("SELECT * FROM users WHERE id=#{id}")
  UserEntity findById(int id);

  //ユーザー情報の更新
  //予想だが、元々のuserにidがすでに入っているのでuseGeneratedKeysが不要になったと思われる。
  @Update("UPDATE users SET name = #{name}, email = #{email} WHERE id = #{id}")
  void update(UserEntity user);

  //存在するかチェックするのでexists。返り値はtrue or falseなのでboolean
 @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")//1に意味は特にない。
  boolean existsByEmail(String email);

  //ユーザー情報の更新時に、他の誰かとメアドが重複していないかをチェック
  //>0とすることで、1以上あったらtrueを返す。
  //メールが既にあり、かつidが違う(他の誰かが既存のメアドを使った)時に実行される
  @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email} AND id != #{userId}")
  boolean existsByEmailExcludingCurrent(String email, Integer userId);

  //<>←!=と同じ意味。(Integer excludedId)で渡された値が#{excludedId}に埋め込まれる。
  //excluded:除外された。ログイン中のユーザーのidを除外してそれ以外のidをプルダウンに表示させる。
  //データを複数取得するためListを使用している。
  @Select("SELECT * FROM users WHERE id <> #{excludedId}")
  List<UserEntity> findAllExcept(Integer excludedId);

}
