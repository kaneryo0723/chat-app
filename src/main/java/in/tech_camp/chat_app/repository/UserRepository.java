package in.tech_camp.chat_app.repository;

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




}
