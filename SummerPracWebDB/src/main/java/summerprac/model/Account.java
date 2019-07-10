package summerprac.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Account {
    private String email;
    private String password_cpt;
    private String first_name;
    private String last_name;
    private String date_birth;
    private char gender;
    private String avatar_name;
}
