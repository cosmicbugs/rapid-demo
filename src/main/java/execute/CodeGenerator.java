package execute;


import cn.org.rapid_framework.generator.GeneratorFacade;

public class CodeGenerator {

    public static void main(String[] args) throws Exception {
        generator();
    }

    /**
     * 根据模板生成文件
     *
     * @throws Exception
     */
    public static void generator() throws Exception {
        // 模板地址
        String templatePath = System.getProperty("user.dir") + "/rapid-test/template";
        GeneratorFacade g = new GeneratorFacade();
        g.getGenerator().addTemplateRootDir(templatePath);
        // 删除生成器的输出目录//
        g.deleteOutRootDir();
        // 通过数据库表生成文件
        g.generateByTable("sys_menu");
        // 自动搜索数据库中的所有表并生成文件,template为模板的根目录
        // g.generateByAllTable();
        // 按table名字删除文件
        // g.deleteByTable("table_name", "template");
    }
}
