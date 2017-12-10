package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Items;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Checker;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.DirFile;

import static ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Consts.libraryName;


/**
 * Created by M.R on 2017/06/20.
 * 
 *  リスト内のItem（ファイル）を全て読み込んでリスト化する。
 *  Item=項目の順番は、Item.list の行番号に依存
 *  
 *  ファイル名：通し番号
 *  Item.list の中身：タイトル
 */

public class ItemsChecker extends Checker {
    private List<String> Titles;
    
    private int LIMIT_kind;
    private int kind_num;
    
    private String kPath;
    private File kindDir;
    private File Item_list;
    private File checkF;
    
    
    /**
     * 改良途中につき注意！！
     * 
     * @param mode
     */
    public ItemsChecker(String mode) {
        super(mode);
    }
    
    @Override
    protected void determPath(String mode) {
        kPath = Consts.rootPath + Consts.combinePath(libraryName) + Consts.listName + "/";
        // 「やったことリスト」の保存場所は、それぞれの Items のディレクトリの中の「fin」ディレクトリの中
        if (mode.equals("finish")) {
            kPath += "fin/";
        }
        LIMIT_kind = Consts.LIMIT_Items;
        kindDir = new File(kPath);
        Item_list = new File(kPath + "Item.list");
    }
    
    @Override
    public void check() {
        /**
         * Item.list がなかった場合は、初期化処理
         */
        kindDir.mkdirs();
        if (!Item_list.exists()) {
            try {
                Item_list.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * Item.list が存在しない ＝  なんらかの事情でデータが消失した or 初回起動
             * tem.list がないとどうしようもないので、
             * ok●まず、Item.list を作る。
             * ●初回起動の説明ページを表示。
             */
            /**
             *  ok●すでにディレクトリが存在しているのなら、
             *      ディレクトリの名前をリストに組み込む
             *      ※ 「データベースが破損しました。修復を試みますか？」
             *          というメッセージのあと、同意を得てからする。
             */
            List<String> fileList;
            int fileNum;
            StringBuilder sb = new StringBuilder();
            // Item.list ファイルの分はノーカウント
            if ((fileNum=(fileList=getFileList(kindDir)).size()-1) > 0) {
                if (fileNum > Consts.LIMIT_Items) {
                    kind_num = Consts.LIMIT_Items;
                } else {
                    kind_num = fileNum;
                }
                for (int i = 0; i < kind_num; i++) {
                    // Item.list ファイルの分はノーカウント
                    if (!fileList.get(i).endsWith(".list")) sb.append(fileList.get(i));
                    if (i != kind_num - 1) sb.append("\n");
                }
            } else {
                sb.append("項目タイトル");
            }
            DirFile.writeAll(Item_list, sb.toString());
        
        }
    
        // ○Item.list のタイトル一覧を（リストで）取得
        Titles = new ArrayList<>(Arrays.asList(DirFile.readAll(Item_list).split("\n", -1)));  // 自由に追加できるようにミュータブルに。
        // もし項目数制限にかかっていたら、それ以降を削除
        int kn = Titles.size();
        if (kn > Consts.LIMIT_Items) {
            for (int i = 0; i < kn - Consts.LIMIT_Items; i++) {
                Titles.remove(Consts.LIMIT_Items + i);
            }
            kind_num = Consts.LIMIT_Items;
        } else {
            kind_num = kn;
        }
    
        // アイテムファイルがなければ作る。ファイル名は、通し番号（Items画面で表示する順番）に対応
        for (int i = 0; i < kind_num; i++) {
            if (!(checkF = new File(kPath + i + "[summary].list")).exists())
                createNotFoundFile(checkF);
            if (!(checkF = new File(kPath + i + "[detail].list")).exists())
                createNotFoundFile(checkF);
        }
    }
    
    private static List<String> getFileList(File Dir) {
        File[] files = Dir.listFiles();
        List<String> fileList = new ArrayList<>();
        
        for (File file : files) {
            if (file.isFile() && getSuffix(file.getName()).equals("list")) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }
    
    /**
     * ファイル名から拡張子を返します。
     *
     * @param fileName ファイル名
     * @return ファイルの拡張子
     */
    public static String getSuffix(String fileName) {
        if (fileName == null)
            return null;
        int point = fileName.lastIndexOf("");
        if (point != -1) {
            return fileName.substring(point + 1);
        }
        return fileName;
    }
    
    private static void createNotFoundFile(File NFFiile) {
        try {
            NFFiile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void update_listF() {
        StringBuilder sb = new StringBuilder();
        /*  Items の個数制限周りの実装時に
        if (fileNum > Consts.LIMIT_Items) {
            kind_num = Consts.LIMIT_Items;
        } else {  kind_num=fileNum;  }
        */
        for (int i = 0; i < kind_num; i++) {
            sb.append(Titles.get(i));
            if (i != kind_num - 1) sb.append("\n");
        }
        DirFile.writeAll(Item_list, sb.toString());
        check();
    }
    
    
    // int posi を指定すると、指定された位置に追加
    public void addTitle(String title) {
        Titles.add(title);
    }
    public void addTitle(int posi, String title) {
        Titles.add(posi, title);
    }
    
    // 新しい項目を作成
    @Override
    public void addNew(String ItemName, String option) {
        makeNewItem(ItemName);
    }
    public void makeNewItem(String ItemName) {
        addTitle(ItemName);
        kind_num = Titles.size();
        // Items.list を更新
        update_listF();
        // ファイル作成
        Consts.ItemNumber = Titles.size() - 1;
        ItemIO.check();
    }
    
    //  終わった項目は、終わったもの置き場（・・・/項目名.finish）に移動
    public static boolean moveFinItem(int posi) {
    
        return false;
    }
    // 終わったもの置き場からも削除したい（または間違ったので完全に削除したい）場合
    @Override
    public void remove(int posi) {
        removeItem(posi);
    }
    private static boolean removeItem(int posi) {
    
        return false;
    }
    //  終わったもの置き場（・・・/項目名.finish）から元のリスト内に戻す
    public static boolean returnItem(int posi) {
    
        return false;
    }
    
    // 全て書き換えor変更箇所だけ書き換え
    // → 全て比較して、変更箇所のみ変更
    public void ReflectUpdate() {
    }
    
    
    
    @Override public List<String> getNames() {
        return Titles;
    }
    
    @Override
    public int getSize() {
        if (kind_num != Titles.size()) {
            throw new IllegalStateException("[Check]サイズが正しく記録できていません。");
        }
        return kind_num;
    }
    @Override public int getLIMIT_kind() {
        return LIMIT_kind;
    }
    
}
