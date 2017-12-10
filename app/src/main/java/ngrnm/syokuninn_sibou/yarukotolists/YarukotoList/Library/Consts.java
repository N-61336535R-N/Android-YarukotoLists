package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library;


import java.util.ArrayDeque;
import java.util.Deque;

public class Consts {
    private Consts() {
    }
    
    public static final int LIMIT_Category = 16;
    public static final int LIMIT_Lists = 16;
    public static final int LIMIT_Items = 5;
    
    public static final int Item_num = 3;
    public static final int LIMIT_histry = 10;
    
    // 画像などのファイルの保管場所のパス
    public static String rootPath;    // context.getFilesDir().getPath() + "/" で固定。
    public static String currentLibName="YList/";
    public static Deque<String> libraryName = new ArrayDeque<>();   // 末尾に "/" はつけない。基本、combinePath() とセットで使う
    public static int hierarchy = 0;
    public static String listName;   // 末尾に "/" を加えることを忘れないように。
    public static int ItemNumber;   // アイテムのファイル名は通し番号で管理するので、識別子は整数。
    
    
    public static final String LIB_IMG_NAME = "library_img.png";
    
    public static String combinePath(Deque<String> libraryName) {
        StringBuilder s = new StringBuilder("");
        for (String libN : libraryName) {
            s.append(libN).append("/");
        }
        s.append(currentLibName).append("/");
        return s.toString();
    }
    public static void inLibrary(String cLibName) {
        libraryName.push(currentLibName);
        currentLibName = cLibName;
    }
    public static void outLibrary() {
        if (libraryName.size() != 0) {
            currentLibName = libraryName.pop();
        }
    }
    
}