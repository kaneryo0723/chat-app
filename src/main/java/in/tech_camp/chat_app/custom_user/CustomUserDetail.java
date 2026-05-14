package in.tech_camp.chat_app.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;//デフォルトのログイン情報を変更するため、springSecurityのUserDetailsにアクセス。

import in.tech_camp.chat_app.entity.UserEntity;
import lombok.Data;

@Data
//springSecurityのチュートリアルに、UserDetailsに関する仕様が載っている。
public class CustomUserDetail implements UserDetails {
  private final UserEntity user;

  public CustomUserDetail(UserEntity user){
    this.user=user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities(){
    return Collections.emptyList();//空の権限を返す。
  }

  @Override
  public String getUsername(){
    return user.getEmail();//ユーザーネームでログインするところを、メールでログインできるように変更
  }

  //そもそも元のデータにないので新たに作成
  public Integer getId(){
    return user.getId();
  }

  @Override
  public String getPassword(){
  return user.getPassword();
  }


  //下記内容は特に変更せず。これらの内容はすべて公式ドキュメントに書いてある。
  @Override
  public boolean isAccountNonExpired(){
  return true;
  }

  @Override
  public boolean isAccountNonLocked(){
  return true;
  }

  @Override
  public boolean isCredentialsNonExpired(){
  return true;
  }

  @Override
  public boolean isEnabled(){
  return true;
  }


}
