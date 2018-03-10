package ngrnm.syokuninn_sibou.yarukotolists.Settings;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YPathTable;

public class Consts {
    private Consts() {}
    
    public static final String realmDBname = "YDB.realm";
    public static final Class[] realmClasses = {YList.class, YItem.class};
    public static final Class[] realmOptClasses = {YPathTable.class};
    public static ArrayList<Class> realmAllClasses = new ArrayList<>();
    static {
        LinkedList<Class> tmpList = new LinkedList<>();
        tmpList.addAll(Arrays.asList(realmClasses));
        tmpList.addAll(Arrays.asList(realmOptClasses));
        realmAllClasses.addAll(tmpList);
    }
    
    public static final int LIMIT_Category = 9;
    public static final int LIMIT_Lists = 9;
    public static final int LIMIT_Items = 9;
    
    public static final int Item_num = 3;
    public static final int LIMIT_histry = 10;
    
    // 画像などのファイルの保管場所のパス
    public static String rootPath;    // context.getFilesDir().getPath() + "/" で固定。
    
    
    
    /**
     * 仕様
     * ○ primary key : id
     *      ※ Realm のインデックス用。実際に使われることはまずない。
     *      なので、Restore 時には心置きなく書き換えていい。というか、そもそもBackup に含めなくてもOK
     *      パフォーマンス向上のためだけに用意する。
     * 
     * ○ unique key : path
     *      primary key が後々変更できない
     *      ※ Category も含めて、同じ階層内で同じ名前は許さないこととする。
     *
     * 
     */
    
    
    
    
    
    
    
    
    
    
    
    
    /* 旧時代の遺物 */
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