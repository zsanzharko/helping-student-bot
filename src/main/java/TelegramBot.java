import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import security.Authorization;

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
        SendMessage sendMessage = null;
        SendPhoto sendPhoto = null;
        SendAudio sendAudio = null;
        SendAnimation sendAnimation = null;
        SendContact sendContact = null;
        SendDocument sendDocument = null;

        if (update.hasMessage()) {
            final String CHAT_ID = update.getMessage().getChatId().toString();

            final Long ID = update.getMessage().getForwardFrom().getId();
            final String first_name = update.getMessage().getForwardFrom().getFirstName(),
                    last_name = update.getMessage().getForwardFrom().getLastName();
            final String username = update.getMessage().getForwardFrom().getUserName() != null ?
                    update.getMessage().getForwardFrom().getUserName() :
                    "null";

            Authorization.checkUser(first_name, last_name, ID, username);

            if (update.getMessage().hasText()) {
                final String TEXT = update.getMessage().getText();

                if (TEXT.charAt(0) == '/') {
                    switch (TEXT) {
                        case "/start" -> {

                        }
                        case "/stop" -> {

                        }
                        case "/information" -> {

                        }
                    }
                    return;
                }

            }

        }
        try {
            if (sendMessage != null) execute(sendMessage);
            if (sendAudio != null) execute(sendAudio);
            if (sendAnimation != null) execute(sendAnimation);
            if (sendContact != null) execute(sendContact);
            if (sendPhoto != null) execute(sendPhoto);
            if (sendDocument != null) execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
