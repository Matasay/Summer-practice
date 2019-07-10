package summerprac.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationClass {
    private String class_name;
    private String parent_class_name;
}
