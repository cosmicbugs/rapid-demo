package pageorder;

/**
 * @author cosmicbugs
 * @date 2017/11/30
 */
public interface PageOrder {


    /**
     * 根据 枚举变量 获取 枚举中DbColumn值
     *
     * @return
     */
    String getDbColumn();

    /**
     * 根据 枚举变量 获取 枚举中EntityColumn值
     *
     * @return
     */
    String getEntityColumn();


}
