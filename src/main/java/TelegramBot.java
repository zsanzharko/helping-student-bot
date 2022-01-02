import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                final String TEXT = update.getMessage().getText();
                final String CHAT_ID = update.getMessage().getChatId().toString();
                final Long ID = update.getMessage().getForwardFrom().getId();

                switch (TEXT) {
                    case "/start" -> {

                    }
                    case "/stop" -> {

                    }
                    case "/information" -> {

                    }
                }

            }

        }
    }
}
