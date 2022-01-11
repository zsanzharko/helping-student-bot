package helpingpost;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class HelpPost {
    private final static List<HelpPost> help_post_list = new ArrayList<>();

    private final InputFile[] photos = new InputFile[10];
    private final String title;
    private final String description;
    private int cost = 0;

    private Long idPost;
    private Long idCreator;
    private Long idWorker;
    private List<Long> idWatchers;

    public HelpPost(String title, String description, int cost,
                    Long idCreator, Long idWorker, List<Long> idWatchers) {
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.idCreator = idCreator;
        this.idWorker = idWorker;
        this.idWatchers = idWatchers;
    }

    public boolean addWatcherToPost(Long idPost, Long idWatcher) {
        int index = HelpPost.binary_search(idPost);
        if (index == -1) return false;
        help_post_list.get(index).addIdWatcher(idWatcher);
        return true;
    }

    private void addIdWatcher(Long idWatcher) {
        //todo maybe max people will be 5
        // cause user will check, and it will see hard
        // and we can add callback
        idWatchers.add(idWatcher);
    }

    public boolean isPhotoMessagePost() {
        for (InputFile photo : photos) {
            if (photo != null) return true;
        }
        return false;
    }

    public String getInformationToString() {
        StringBuilder information = new StringBuilder();
        if (photos.length != 0) {
            information.append("(");
            for (InputFile photo : photos) {
                //fixme CHECK INFORMATION ABOUT INPUT FILES
                information.append(photo.getAttachName());
//                if (i != photos.length - 1)
                information.append("|");
            }
            information.append(")");
        }
        information.append("Post: ")
                .append(idPost).append("\n\n")
                .append("▶️").append(title).append("◀️\n")
                .append("Description:\n\t").append(description).append("\n")
                //fixme if u want to change currency do this
                // Bold me too
                .append("Cost ").append(cost).append("tg.\n")
                .append("Browsing now ").append(idWatchers.size()).append(" people.");
        return information.toString();
    }

    //fixme watch generics and fix this
    private static int binary_search(Long ID) {
        int low = 0;
        int high = HelpPost.help_post_list.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Long guess = HelpPost.help_post_list.get(mid).getIdPost();
            if (Objects.equals(guess, ID)) {
                return mid;
            }
            if (guess > ID) {
                high = mid - 1;
            } else low = mid + 1;
        }
        return -1;
    }

    public static List<HelpPost> getHelp_post_list() {
        return help_post_list;
    }
}
