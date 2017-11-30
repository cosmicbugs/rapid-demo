package pageorder;

/**
 * 排序
 *
 * @author cosmicbugs
 * @date 2017/11/30
 */
public enum SysMenuOrder implements PageOrder {
    //创建时间
    CREATED_TIME("created_time", "createdTime"),
    //发布时间
    PUBLISH_TIME("publish_time", "publishTime"),
    //更新时间
    UPDATED_TIME("updated_time", "updatedTime"),
    //置顶
    TOP("is_top", "isTop"),
    //排序
    ORDERS("orders", "orders"),
    //阅读量
    VIEWS("views", "views"),
    //点击量
    CLICKS("clicks", "clicks"),;


    private String dbColumn;

    private String entityColumn;


    SysMenuOrder(String dbColumn, String entityColumn) {
        this.dbColumn = dbColumn;
        this.entityColumn = entityColumn;
    }


    /**
     * 根据 枚举变量 获取 枚举中DbColumn值
     *
     * @return
     */
    @Override
    public String getDbColumn() {
        return this.dbColumn;
    }

    /**
     * 根据 枚举变量 获取 枚举中EntityColumn值
     *
     * @return
     */
    @Override
    public String getEntityColumn() {
        return this.entityColumn;
    }
}
