import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return System.getenv("SDU_Helping_Student_Bot").substring(';' + 1);
    }

    @Override
    public String getBotToken() {
        return System.getenv("SDU_Helping_Student_Bot").substring(0, System.getenv("SDU_Helping_Student_Bot").indexOf(';'));
    }

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();

        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(update.getMessage().getChatId().toString());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
