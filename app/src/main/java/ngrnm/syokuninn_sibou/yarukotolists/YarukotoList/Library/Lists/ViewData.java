package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Lists;

/**
 * Created by M.R on 2017/06/05.
 */

public class ViewData {
    private String imagePath;
    private String title;
    
    public ViewData(String imagePath, String title) {
        this.imagePath = imagePath;
        this.title = title;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    public String getTitle() {
        return title;
    }
}
