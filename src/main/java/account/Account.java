package account;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.ChatLocation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @apiNote
 * Class Account creates. It will connect with database and other bots to realizing(ecosystem)
 * Account is main class, with all information about user and personal information;
 * Class give information to work in bots.
 * {@link #chatId} chat id in Telegram
 * {@link #activity} gives latest activity in telegram Bot
 *
 *
 * @version 1.0
 * @author Sanzhar Zhanibekov
 */

@Data
public final class Account {
    private static List<Account> accountList = new ArrayList<>();

    private final User user;        // user with all communication
    private final String chatId;    // chat ID bot to account

    @NotNull
    private final Long ID;          // unify telegram id account
    private String username;        // telegram username

    private ChatLocation chatLocation;                          // when account was the latest
    private AccountActivity activity = new AccountActivity();   // latest account activity with messages


    public Account(Long ID, String chatId, String username, String name, String surname) {
        this.ID = ID;
        this.username = username;
        this.chatId = chatId;
        this.user = new User(name, surname);
    }

    public Account(Long ID, String chatId) {
        this.ID = ID;
        this.username = "null";
        this.chatId = chatId;
        this.user = new User();
    }

    public Account(Long ID, String chatId, String username, ChatLocation chatLocation, String name, String surname) {
        this.ID = ID;
        this.username = username;
        this.chatLocation = chatLocation;
        this.chatId = chatId;
        this.user = new User(name, surname);
    }

    // This constructor will use when we wil add database
    public Account(Long ID, String username, ChatLocation chatLocation, String chatId,
                   AccountActivity activity, String name, String surname) {
        this.ID = ID;
        this.username = username;
        this.user = new User(name, surname);
        this.chatLocation = chatLocation;
        this.chatId = chatId;
        this.activity = activity;
    }

    public String getInformationAccount() {
        return "Name: " + getUser().getName() + "\n" +
                "Surname: " + getUser().getSurname() + "\n" +
                "student ID: " + getUser().getStudentID() + "\n";
    }

    public static List<Account> getAccountList() {
        return accountList;
    }

    public static void setAccountList(List<Account> accountList) {
        Account.accountList = accountList;
    }

    @Override
    public String toString() {
        return "Account{" +
                "user=" + user +
                ", chatId='" + chatId + '\'' +
                ", ID=" + ID +
                ", username='" + username + '\'' +
                ", chatLocation=" + chatLocation +
                ", activity=" + activity +
                '}';
    }

    public static String getAccountInformation(Account account) {
        if (binary_search(account.getID()) == -1) {
            //todo notify admin to problem
            return """
                    Sorry, we didn't find your account :(
                    I will try to notify the admin in your problem
                    Please wait...""";
        }

        return "Account:\n\n" +
                "Status: " + account.getUser().isActive() + "\n" +
                "Fake username:\t" + account.getUser().getFakeUsername() + "\n" +
                "Name:\t" + "fake username: " + account.getUser().getName() + "\n" +
                "Surname:\t" + account.getUser().getSurname() + "\n" +
                "Birthday:\t" + account.getUser().getBirthday() + "\n" +
                "Phone number:\t" + account.getUser().getNumberPhone() + "\n" +
                "Email:\t" + account.getUser().getEmail() + "\n" +
                "Payment card:\t" + account.getUser().getPayment();
    }

    public static int binary_search(Long ID) {
        int low = 0;
        int high = Account.accountList.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Long guess = Account.accountList.get(mid).getID();
            if (Objects.equals(guess, ID)) {
                return mid;
            } if (guess > ID) {
                high = mid - 1;
            } else low = mid + 1;
        }
        return -1;
    }
}
