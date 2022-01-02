package user;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String firstName, lastName;
    private Date birthday;
    private String username;
    private String studentId;
    private final Long ID;

    public User(Long ID, String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.ID = ID;
    }
}
