package in.tech_camp.chat_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserEntity {
    private Integer id;
    private String name;
    private String password;
    private String email;
}
