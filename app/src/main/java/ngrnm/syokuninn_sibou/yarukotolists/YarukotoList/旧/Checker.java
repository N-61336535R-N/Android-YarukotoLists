package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.旧;

import java.util.List;

/**
 * Created by M.R on 2017/08/01.
 */

public abstract class Checker {
    
    public Checker(String mode) {
        determPath(mode);
        check();
    }
    protected abstract void determPath(String mode);
    protected abstract void check();
    
    
    public abstract void addNew(String ItemName, String option);
    public abstract void remove(int posi);
    
    protected abstract void ReflectUpdate();
    
    
    public abstract int getSize();
    public abstract int getLIMIT_kind();
    public abstract List<String> getNames();
    
}
