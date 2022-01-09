package helpingpost;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class HelpPost {
    private final static List<HelpPost> help_post_list = new ArrayList<>();

    private InputFile photo;
    private String title;
    private String description;
    private int cost = 0;

    private Long id_creator;

    public HelpPost(InputFile photo, String title, String description, int cost, Long id_creator) {
        this.photo = photo;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.id_creator = id_creator;
    }

}
