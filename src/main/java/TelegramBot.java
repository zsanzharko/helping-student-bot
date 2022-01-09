import account.Account;
import auth.Authorization;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return System.getenv("SDU_Helping_Student_Bot").substring(';' + 1);
    }

    @Override
    public String getBotToken() {
        return System.getenv("SDU_Helping_Student_Bot")
                .substring(0, System.getenv("SDU_Helping_Student_Bot").indexOf(';'));
    }

    @Override
    public void onUpdateReceived(Update update) {
        Account account;

        if (update.hasMessage()) {
            final String CHAT_ID = update.getMessage().getChatId().toString();

            final Long ID = update.getMessage().getForwardFrom().getId();
            final String first_name = update.getMessage().getForwardFrom().getFirstName(),
                    last_name = update.getMessage().getForwardFrom().getLastName();
            final String username = update.getMessage().getForwardFrom().getUserName() != null ?
                    update.getMessage().getForwardFrom().getUserName() :
                    "null";


            if (update.getMessage().hasText()) {
                final String TEXT = update.getMessage().getText();

                if (TEXT.charAt(0) == '/') {
                    if (TEXT.equals("/start")) {
                        //todo next time do like this
                        // connect to database and find this user
                        // if db will find, you can import personal data to this bot
                        // else you will authorized with this method
                        Authorization.authLogPerson(ID, CHAT_ID, username, first_name, last_name);
                        return;
                    }
                    account = Account.getAccountList().get(Account.binary_search(Account.getAccountList(), ID));
                    switch (TEXT) {
                        case "/stop" -> {
                            //todo realize removing in list with database
                            // or remove only in telegram, and when user will come back
                            // ur get account in database;
                            Account.getAccountList().remove(account);
                        }
                        case "/information" -> {
                            SendMessage information_message = new SendMessage();
                            information_message.setChatId(CHAT_ID);
                            information_message.setText("""
                                    Привет! Я бот в котором ты сможешь почуствовать себя анонимом среди университетов
                                    В мои обязанности входит исключительная безопастность и сокрытие данных от третьих
                                    лиц. На данный момент доступны функции:
                                        /start - данные начинают собираться (в целях автоматизировать ecosystem'у ботов)
                                        /stop - данные удаляются из активных участников (остаються в базе данных до окончания вашей учебы) \s
                                        /information - предоставляет возможности бота, о которых вы могли бы забыть\s
                                        /getconnection - показывает подключение между студентов без личных информаций
                                        /helpers - просмотр постов помощи
                                        /account
                                    """);
                            try {
                                execute(information_message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        case "/account" -> {
                            SendMessage accountInformationMessage = new SendMessage();
                            accountInformationMessage.setChatId(CHAT_ID);
                            accountInformationMessage.setText(
                                    Account.getAccountInformation(account)
                            );
                            accountInformationMessage.setReplyMarkup(
                                    SendMessages.two_columns_markup(
                                            List.of(new String[]{"Edit", "Remove Message"}),
                                            List.of(new String[]{"/edit_account", "/remove_edit_account_message"})));
                            try {
                                //todo save id message when user want to delete message
                                execute(accountInformationMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        case "/getconnection" -> {
                            final String connectionInformation = account.getUser().getConnectionAccountListInformation();
                            SendMessage informationConnectionMessage = new SendMessage();
                            informationConnectionMessage.setText(connectionInformation);
                            informationConnectionMessage.setChatId(CHAT_ID);
                            informationConnectionMessage.setReplyMarkup(
                                    SendMessages.two_columns_markup(
                                            List.of(new String[]{"Connect to", "Remove connect"}),
                                            List.of(new String[]{"/connect_to", "/remove_connect"})));
                        }
                        case "/helpers" -> {
                            String text = """
                                    What u are want to do?
                                    """;
                            SendMessage help_choice_message = new SendMessage();
                            help_choice_message.setText(text);
                            help_choice_message.setChatId(CHAT_ID);
                            help_choice_message.setReplyMarkup(
                                    SendMessages.two_columns_markup(
                                            List.of(new String[]{"Create", "Remove", "View Post"}),
                                            List.of(new String[]{"/create_post", "/remove_posts", "/view_posts"})
                                    )
                            );
                            try {
                                execute(help_choice_message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
