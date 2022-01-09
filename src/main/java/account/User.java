package account;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @apiNote Class User save personal information. This class didn't connectivity with Account in Telegram. It is like
 * module when this telegram bots working
 */

@Data
public class User {
    private boolean isActive = true;                                // user active (online | offline)
    private String name;                                            // user name
    private String surname;                                         // user surname
    private LocalDate birthday;                                     // user birthday
    private String numberPhone;                                     // user number
    private String email;                                           // user email

    private Payment payment;                                        // user payment

    private String studentID;                                       // student ID

    private String fakeUsername;
    private List<Long> connectionAccountList = new ArrayList<>();   // His connect with other students

    public User() {
    }

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public User(boolean isActive, String name, String surname, LocalDate birthday,
                String numberPhone, String email, Payment payment, String studentID,
                String fakeUsername, List<Long> connectionAccountList) {
        this.isActive = isActive;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.numberPhone = numberPhone;
        this.email = email;
        this.payment = payment;
        this.studentID = studentID;
        this.fakeUsername = fakeUsername;
        this.connectionAccountList = connectionAccountList;
    }

    public static boolean studentIDChecking(String studentID) {
        Pattern pattern = Pattern.compile("^[0-9]{9}$");
        Matcher matcher = pattern.matcher(studentID);
        return matcher.find();
    }

    /**
     * @apiNote
     * This method catching accounts in connection list.
     * If list wil empty, method will return string with text.
     * Next time we got all IDs in {@link #connectionAccountList}, and will searching
     * in {@link #getConnectionAccountList()} index.
     * Remember if user remove by self, search will get -1
     *
     * @return Accounts with fake username in STRING.
     */
    public final String getConnectionAccountListInformation() {
        if (connectionAccountList.size() == 0) return "Your connection is empty...";
        StringBuilder connectionList = new StringBuilder("Your connections:\n");
        for (int i = 0; i < connectionAccountList.size(); i++) {
            Long connection = connectionAccountList.get(i);
            int indexAccount = Account.binary_search(Account.getAccountList(), connection);
            if (indexAccount != -1) {
                connectionList.append(i + 1).append(". ").append(
                        Account.getAccountList().get(indexAccount).getUser().getFakeUsername());
            } else {
                //fixme get fake username, in database
                connectionList.append(i + 1).append(". ").append(
//                        Account.getAccountList().get(indexAccount).getUser().getFakeUsername()
                        "Account is stopped (pressed \"/stop\")"
                );
            }
            if (i != connectionAccountList.size() - 1) connectionList.append("\n");
        }
        return connectionList.toString();
    }
}
