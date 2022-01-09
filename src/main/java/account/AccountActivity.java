package account;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

@Data
public class AccountActivity {
    private SendMessage latestMessage;
    private SendPhoto latestPhoto;
    private SendDocument latestDocument;
    private SendAnimation latestAnimation;
    private SendAudio latestAudio;
    private DeleteMessage deleteMessage;

    private Integer latestMessageId;

    public AccountActivity() {}

    public AccountActivity(SendMessage latestMessage,
                           SendPhoto latestPhoto, SendDocument latestDocument,
                           SendAnimation latestAnimation, SendAudio latestAudio) {
        this.latestMessage = latestMessage;
        this.latestPhoto = latestPhoto;
        this.latestDocument = latestDocument;
        this.latestAnimation = latestAnimation;
        this.latestAudio = latestAudio;
    }
}
