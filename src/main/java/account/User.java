package account;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private Long currentConnection;

    public User() {
    }

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public User(boolean isActive, String name, String surname, LocalDate birthday,
                String numberPhone, String email, Payment payment, String studentID,
                String fakeUsername, List<Long> connectionAccountList, Long currentConnection) {
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
        this.currentConnection = currentConnection;
    }

    public static boolean studentIDChecking(String studentID) {
        Pattern pattern = Pattern.compile("^[0-9]{9}$");
        Matcher matcher = pattern.matcher(studentID);
        return matcher.find();
    }

    /**
     * @return Accounts with fake username in STRING.
     * @apiNote This method catching accounts in connection list.
     * If list wil empty, method will return string with text.
     * Next time we got all IDs in {@link #connectionAccountList}, and will searching
     * in {@link #getConnectionAccountList()} index.
     * Remember if user remove by self, search will get -1
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

    public final List<String> getConnectionAccountList() {
        List<String> connections = new ArrayList<>();

        for (Long aLong : connectionAccountList) {
            Account account = Account.getAccountList().get(
                    Account.binary_search(Account.getAccountList(), aLong));
            connections.add(account.getID() + "|" + account.getUser().getFakeUsername());
        }
        return connections;
    }

    public boolean setInfoEdit(String command, String item) {
        switch (command) {
            case "/edit_account_name" -> name = item;
            case "/edit_account_surname" -> surname = item;
            case "/edit_account_birthday" -> {
                if (item.length() != 10) return false;
                // dd/mm/yyyy
                int day = Integer.parseInt(item.substring(0, item.indexOf('/')));
                int month = Integer.parseInt(item.substring(item.indexOf('/') + 1, item.lastIndexOf('/')));
                int year = Integer.parseInt(item.substring(item.lastIndexOf('/') + 1));
                birthday = LocalDate.of(year, month, day);
            }
            case "/edit_account_phone_number" -> {
                // 87470148206
                // +77470148206
                if (item.length() < 11 || item.length() > 12) return false;
                //fixme do checking number
                numberPhone = item;
            }
            case "/edit_account_email" -> {
                //fixme do checking email
                email = item;
            }
            case "/edit_account_payment_card" -> {
                //fixme do checking card number
                payment.setCard(item);
            }
            case "/edit_account_student_id" -> {
                if (User.studentIDChecking(item))
                    setStudentID(item);
                else return false;
            }
            case "/edit_account_fake_username" -> {
                for (int i = 0; i < Account.getAccountList().size(); i++) {
                    Account account = Account.getAccountList().get(i);
                    if (account.getUser().getFakeUsername() != null || !Objects.equals(account.getUser().getFakeUsername(), "")) {
                        if (account.getUser().getFakeUsername().equals(item)) {
                            return false;
                        }
                    }
                }
                fakeUsername = item;
            }
        }
        return true;
    }

    /**
     * Connected when id will stay in database
     *
     * @param id user wanted to connect to this telegram id
     * @return if connection stay current
     */
    public final boolean connectTo(Long id) {
        int index = Account.binary_search(Account.getAccountList(), id);
        if (index == -1) return false;
        currentConnection = Account.getAccountList().get(index).getID();
        return true;
    }

    /**
     * Remove connection with other user
     *
     * @return when current connection will stay null
     */
    public final boolean removeConnection(Long id) {
        // todo check this
        int index = Account.binary_search(Account.getAccountList(), id);
        if (index == -1) return false;
        currentConnection = null;
        return true;
    }
}
