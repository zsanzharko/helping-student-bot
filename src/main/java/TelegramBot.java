import account.Account;
import auth.Authorization;
import helpingpost.HelpPost;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "test_helping_student_bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("test_helping_student_bot");
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
                    int index = Account.binary_search(ID);
                    if (index != -1)
                        account = Account.getAccountList().get(index);
                    else {
                        SendMessage auth_not_found = new SendMessage(CHAT_ID, "Account not authorized");
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
                            SendMessage information_message =
                                    new SendMessage(CHAT_ID, """
                                            Привет! Я бот в котором ты сможешь почуствовать себя анонимом среди университетов
                                            В мои обязанности входит исключительная безопастность и сокрытие данных от третьих
                                            лиц. На данный момент доступны функции:
                                                /start - данные начинают собираться (в целях автоматизировать ecosystem'у ботов)
                                                /stop - данные удаляются из активных участников (остаються в базе данных до окончания вашей учебы) \s
                                                /information - предоставляет возможности бота, о которых вы могли бы забыть\s
                                                /getconnection - показывает подключение между студентов без личных информаций
                                                /helpers - просмотр постов помощи
                                                /account - просмотр профиля, и его настройка""");
                            try {
                                execute(information_message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        case "/account" -> {
                            SendMessage accountInformationMessage =
                                    new SendMessage(CHAT_ID, Account.getAccountInformation(account));
                            accountInformationMessage.enableMarkdownV2(true);
                            accountInformationMessage.setReplyMarkup(
                                    SendMessages.two_columns_markup(
                                            List.of(new String[]{"Edit", "Remove Message"}),
                                            List.of(new String[]{"/edit_account_", "/remove_edit_account_message"})));
                            try {
                                //todo save id message when user want to delete;
                                // you can check MessageAutoDeleteTimerChanged class
                                // and use this
                                execute(accountInformationMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        case "/getconnection" -> {
                            final String connectionInformation = account.getUser().getConnectionAccountListInformation();
                            SendMessage informationConnectionMessage = new SendMessage(CHAT_ID, connectionInformation);
                            if (account.getUser().getConnectionAccountList().size() == 0) {
                                informationConnectionMessage.setReplyMarkup(
                                        SendMessages.one_row_markup(
                                                List.of(new String[]{"Open Help Posts"}),
                                                List.of(new String[]{"/view_posts"})));
                            } else informationConnectionMessage.setReplyMarkup(
                                    SendMessages.two_columns_markup(
                                            List.of(new String[]{"Connect to", "Remove connect"}),
                                            List.of(new String[]{"/connect_to", "/remove_connect"})));
                            try {
                                execute(informationConnectionMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        case "/helpers" -> {
                            String text = """
                                    What u are want to do?
                                    """;
                            SendMessage help_choice_message = new SendMessage(CHAT_ID, text);
                            help_choice_message.setReplyMarkup(
                                    SendMessages.two_columns_markup(
                                            List.of(new String[]{"Create", "Remove", "View Post"}),
                                            List.of(new String[]{"/create_post", "/remove_posts", "/view_posts"})
                                    ));
                            try {
                                execute(help_choice_message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (account.getActivity().getLatestMessage() != null) {
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
                                execute(new SendMessage(CHAT_ID, "Please try again"));
                                return; // we didn't have to set the latest message to null
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //when user give answer bot otherwise send message that will show the account
                            try {
                                execute(new SendMessage(CHAT_ID, account.getInformationAccount()));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    account.getActivity().setLatestMessage(null);
                }
            }
        } else if (update.hasCallbackQuery()) {
            final String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            final Long ID = update.getCallbackQuery().getFrom().getId();
            final String callbackData = update.getCallbackQuery().getData();
            int indexAccount = Account.binary_search(ID);
            if (indexAccount == -1) return;
            account = Account.getAccountList().get(indexAccount);

            if (callbackData.contains("/edit_account_")) {
                //todo check this
                SendMessage edit_message = new SendMessage(chatId,
                        "Please, enter your " + callbackData
                                .replaceAll("_", " ")
                                .substring(callbackData.lastIndexOf('_') + 1));
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
                return;
            } else {
                // next step we're parsing id in callback data
                // second step we will define the command and do something about this
                Long idUser = Long.parseLong(callbackData.substring(callbackData.indexOf("&id=") + 5));
                if (callbackData.contains("/connect_to_")) {
                    //todo check correct parsing
                    if (!account.getUser().connectTo(idUser)) {
                        SendMessage wrong_connection = new SendMessage(chatId,
                                """
                                        Wrong connection
                                        Sorry about that, I send the problem to admin
                                        Wait...""");
                        //todo check latest connection or messages

                        try {
                            execute(wrong_connection);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    SendMessage correct_connection = new SendMessage(chatId,
                            """
                                    Awesome, I connected user with you!
                                    If you want to stop conversation send to message "Stop connection" or press to button
                                    Good luck!""");
                    try {
                        execute(correct_connection);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    // We don't have to check callback data again
                    return;
                } else if (callbackData.contains("/remove_connect_")) {
                    if (!account.getUser().removeConnection(idUser)) {
                        SendMessage wrong_remove = new SendMessage();
                        wrong_remove.setChatId(chatId);

                    }
                }
            }

            switch (callbackData) {
                //Edit some in account
                case "/edit_account" -> {
                    SendMessage editToolMessage = new SendMessage(chatId, "We change a ...");
                    editToolMessage.setReplyMarkup(
                            SendMessages.two_columns_markup(
                                    List.of(new String[]{
                                            "Fake Username", "Payment Card",
                                            "Name", "Surname", "Birthday",
                                            "Phone Number", "Email", "Student ID"}),
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
                //Main User connect to other User, with id
                case "/connect_to" -> {
                    SendMessage connect_to_message = new SendMessage(chatId, "Please, enter connection");

                    List<String> fakeUsernames = new ArrayList<>();
                    List<String> callback_idUsers = new ArrayList<>();

                    for (String result : account.getUser().getConnectionAccountList()) {
                        fakeUsernames.add(result.substring(result.indexOf('|') + 1));
                        callback_idUsers.add("/connect_to&id=" + result.substring(0, result.indexOf('|')));
                    }
                    connect_to_message.setReplyMarkup(SendMessages.one_row_markup(
                            fakeUsernames,
                            callback_idUsers));

                    try {
                        execute(connect_to_message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //Remove connection in Main User, but not to other User
                case "/remove_connect" -> {
                    SendMessage remove_connect_message =
                            new SendMessage(chatId, "Remove connection is finished successful");

                    // We check if id have in account list. Otherwise, we send fail removing
                    // todo if account in offline, maybe it can check in database
                    if (Account.binary_search(account.getUser().getCurrentConnection()) == -1) {
                        remove_connect_message.setText("""
                                Remove connection is 🥵 FAIL 🥵
                                Sorry about that, I send the problem to admin
                                Wait...""");
                    }
                    try {
                        execute(remove_connect_message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                // View all helping posts
                case "/view_posts" -> {
                    //todo maybe delete all latest messages like a new list
                    int random_index_post = (int)(Math.random() * HelpPost.getHelp_post_list().size());
                    int zone = 3;
                    for (int index = random_index_post, count = 1; index < zone; index++, count++) {
                        HelpPost helpPost = HelpPost.getHelp_post_list().get(index);
                        String post = helpPost.getInformationToString();
                        if (helpPost.isPhotoMessagePost()) {
                            List<InputFile> inputFiles = new ArrayList<>();
                            post = post.substring(post.indexOf('(') + 1);
                            do {
                                inputFiles.add(new InputFile(
                                        post.substring(0, post.indexOf('|'))));
                                //check method in HelpPost getInformationToString
                                // it is so bad
                                post = post.substring(post.indexOf('|') + 1);
                            } while (post.charAt(0) != ')');
                            post = post.substring(post.indexOf(')') + 1);
                            //fixme check documentation about input files and multiple photos
                            SendPhoto send_post = new SendPhoto(chatId, inputFiles.get(0));
                            send_post.setCaption(post);
                            if (count != zone) {
                                send_post.setReplyMarkup(SendMessages.one_row_markup(
                                        List.of(new String[]{"Apply"}),
                                        List.of(new String[]{
                                                "/post_apply&idPost=" + helpPost.getIdPost() +
                                                        "&newIdWatcher=" + account.getID()})));
                            } else {
                                send_post.setReplyMarkup(SendMessages.two_columns_markup(
                                        List.of(new String[]{"Apply", "◀️", "▶️"}),
                                        List.of(new String[]{
                                                "/post_apply&idPost=" + helpPost.getIdPost() +
                                                        "&newIdWatcher=" + account.getID(),
                                                "/post_back&index=" + (random_index_post - zone),
                                                "/post_next&index=" + (random_index_post + zone)
                                        })));
                            }

                        }
                    }
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
