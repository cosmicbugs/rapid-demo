package pageorder;

import org.junit.Test;

import java.util.List;

/**
 * @author cosmicbugs
 * @date 2017/11/30
 */
public class TestOrder {

    @Test
    public void orderTest() {
//        tt(SysMenuOrder.valueOf("TOP"));
        sorts(SysMenuOrder.values());
    }

    public void tt(PageOrder pageOrder) {
        System.out.println(pageOrder.getDbColumn());
    }

    public void t2(Enum enum2) {

    }

    private static <E extends Enum> void sorts(E[] e) {
        for (E e1 : e) {
            System.out.println(e1.name());
            System.out.println(((PageOrder) e1).getDbColumn());
        }

    }




}
