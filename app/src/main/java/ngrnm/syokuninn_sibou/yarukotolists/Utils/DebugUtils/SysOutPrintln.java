package ngrnm.syokuninn_sibou.yarukotolists.Utils.DebugUtils;

import java.util.List;

/**
 * Created by ryo on 2018/03/09.
 */

public class SysOutPrintln {
    public static final void printList(List<Object> outList) {
        for (Object obj : outList) {
            System.out.println(obj.toString());
        }
    }
}
