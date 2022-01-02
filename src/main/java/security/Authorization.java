package security;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Authorization {
    private static final List<User> users = new ArrayList<>();

    private static void registerUser() {

    }

    public static void checkUser(String first_name, String last_name, Long user_id, String username) {
        for (User user : users) {
            if (Objects.equals(user.getID(), user_id)) return;
        }
        users.add(new User(user_id, first_name, last_name, username));
    }
}