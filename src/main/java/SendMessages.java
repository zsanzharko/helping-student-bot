import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class SendMessages {
    public static InlineKeyboardMarkup two_choice_markup(String[] texts, String[] callback_data){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        InlineKeyboardButton first_choice = new InlineKeyboardButton();
        InlineKeyboardButton second_choice= new InlineKeyboardButton();

        first_choice.setText(texts[0]);
        first_choice.setCallbackData(callback_data[0]);

        second_choice.setText(texts[1]);
        second_choice.setCallbackData(callback_data[1]);

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(first_choice);
        row.add(second_choice);

        rowList.add(row);
        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup two_columns_markup(List<String> texts, List<String> callback_data) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        if (texts.size() != callback_data.size()) return new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (int i = 0; i < texts.size() / 2; i += 2) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = i; j < 2; j++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(texts.get(j));
                button.setCallbackData(callback_data.get(j));
                row.add(button);
            }
            rowList.add(row);
        }
        if (texts.size() % 2 == 1) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(texts.get(texts.size() - 1));
            button.setCallbackData(callback_data.get(callback_data.size() - 1));
            row.add(button);
            rowList.add(row);
        }
        markup.setKeyboard(rowList);
        return markup;
    }

    public static InlineKeyboardMarkup one_row_markup(List<String> texts, List<String> callback_data) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        if (texts.size() != callback_data.size()) return new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(texts.get(i));
            button.setCallbackData(callback_data.get(i));
            row.add(button);
            rowList.add(row);
        }
        markup.setKeyboard(rowList);

        return markup;
    }
}
