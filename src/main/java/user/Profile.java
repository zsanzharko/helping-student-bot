package user;

import lombok.Data;

import java.util.Date;

@Data
public class Profile {
    private String firstName, lastName;
    private Date birthday;

    private String surname;

    private String studentId;

    private final Long ID;
}
