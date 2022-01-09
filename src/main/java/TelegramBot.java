import account.Account;
import auth.Authorization;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

            final Long ID = update.getMessage().getChat().getId();
            final String first_name = update.getMessage().getChat().getFirstName(),
                    last_name = update.getMessage().getChat().getLastName();
            final String username = update.getMessage().getChat().getUserName() != null ?
                    update.getMessage().getChat().getUserName() :
                    "null";


            if (update.getMessage().hasText() && !update.getMessage().getText().equals("")) {
                final String TEXT = update.getMessage().getText();

                if (TEXT.equals("/start")) {
                    // todo next time do like this
                    //  connect to database and find this user
                    //  if db will find, you can import personal data to this bot
                    //  else you will authorized with this method
                    Authorization.authLogPerson(ID, CHAT_ID, username, first_name, last_name);
                    return;
                } else {
                    int index = Account.binary_search(Account.getAccountList(), ID);
                    if (index != -1)
                        account = Account.getAccountList().get(index);
                    else {
                        SendMessage auth_not_found = new SendMessage();
                        auth_not_found.setText("Account not authorized");
                        auth_not_found.setChatId(CHAT_ID);
                        try {
                            execute(auth_not_found);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                if (TEXT.charAt(0) == '/') {
                    switch (TEXT) {
                        case "/stop" -> {
                            //todo realize removing in list with database
                            // or remove only in telegram, and when user will come back
                            // ur get account in database;
                            System.out.println("checking in database");
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
                else if (account.getActivity().getLatestMessage() != null) {
                    // we're checking the latest message to first character like "/"
                    // and if it is true, we change in account
                    if (account.getActivity().getLatestMessage().getText().charAt(0) == '/') {
                        //todo ENCRYPTION, DECRYPTION
                        String command = account.getActivity().getLatestMessage().getText();

                        if (!account.getUser().setInfoEdit(command, update.getMessage().getText())) {
                            try {
                                //if user will wrong give answer, we send a message with text
                                //At this moment we don't set to The Latest Message, cause all edit, will function
                                //not buttons
                                SendMessage warning_message = new SendMessage();
                                warning_message.setChatId(CHAT_ID);
                                warning_message.setText("Please try again");

                                execute(warning_message);
                                return; // we didn't have to set the latest message to null
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //when user give answer bot otherwise send message that will show the account
                            try {
                                SendMessage updating_message = new SendMessage();
                                updating_message.setChatId(CHAT_ID);
                                updating_message.setText(account.getInformationAccount());
                                execute(updating_message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    account.getActivity().setLatestMessage(null);
                }
            }
        }
        else if (update.hasCallbackQuery()) {
            final String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            final Long ID = update.getCallbackQuery().getFrom().getId();
            final String callbackData = update.getCallbackQuery().getData();
            int indexAccount = Account.binary_search(Account.getAccountList(), ID);
            if (indexAccount == -1) return;
            account = Account.getAccountList().get(indexAccount);

            if (callbackData.contains("/edit_account_")) {
                SendMessage edit_message = new SendMessage();
                //todo check this
                edit_message.setText("Please, enter your " + callbackData
                        .replaceAll("_", " ")
                        .substring(callbackData.lastIndexOf('_') + 1));
                edit_message.setChatId(chatId);

                try {
                    // first we usually send the message next step, we change text to callback
                    // and when we get message like about name, we will check latest saved Message
                    // and in text we find, what we have to change
                    execute(edit_message);
                    //todo callback with encryption, decryption
                    // import library
                    edit_message.setChatId(callbackData);
                    account.getActivity().setLatestMessage(edit_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            switch (callbackData) {
                //Edit some in account
                case "/edit_account" -> {
                    SendMessage editToolMessage = new SendMessage();
                    editToolMessage.setChatId(chatId);
                    editToolMessage.setText("We change a ...");
                    editToolMessage.setReplyMarkup(
                            SendMessages.two_columns_markup(
                                    List.of(new String[]{
                                            "Fake Username",
                                            "Payment Card",
                                            "Name",
                                            "Surname",
                                            "Birthday",
                                            "Phone Number",
                                            "Email",
                                            "Student ID"
                                    }),
                                    List.of(new String[]{
                                            "/edit_account_fake_username",
                                            "/edit_account_payment_card",
                                            "/edit_account_name",
                                            "/edit_account_surname",
                                            "/edit_account_birthday",
                                            "/edit_account_phone_number",
                                            "/edit_account_email",
                                            "/edit_account_student_id"
                                    })));

                    try {
                        //todo check this remember
                        execute(editToolMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //Remove message about account, because it's policy agreement
                case "/remove_edit_account_message" -> {
                }
                //Main User connect to other User, with id
                case "/connect_to" -> {
                }
                //Remove connection in Main User, but not to other User
                case "/remove_connect" -> {
                }
                // View all helping posts
                case "/view_posts" -> {
                }
                // Remove post in self, checking if user will do assigment
                case "/remove_posts" -> {
                }
                // Create post to present in other
                case "/create_post" -> {
                }
            }

        }
    }
}
