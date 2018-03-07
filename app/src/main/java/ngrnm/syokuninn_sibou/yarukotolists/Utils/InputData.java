package ngrnm.syokuninn_sibou.yarukotolists.Utils;

/**
 * Created by M.R on 2017/06/27.
 */

public class InputData {
    private boolean flag;
    private String inStr;
    
    public InputData(boolean flag, String inStr) {
        this.flag = flag;
        this.inStr = inStr;
    }
    
    public boolean isFlag() {
        return flag;
    }
    public String getInStr() {
        return inStr;
    }
}
