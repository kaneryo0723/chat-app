package in.tech_camp.chat_app.form;

import lombok.Data;

@Data
public class UserEditForm {
    private int id;//ルーティングにidを使用するため宣言。
    private String name;
    private String email;
}
