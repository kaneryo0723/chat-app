package in.tech_camp.chat_app.entity;

import lombok.Data;


@Data//getterとsetterを省略できる
public class UserEntity {
    private Integer id;
    private String name;
    private String email;
    private String password;
}
