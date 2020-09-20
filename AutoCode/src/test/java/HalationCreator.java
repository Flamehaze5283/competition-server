import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalationCreator {

    //配置值
    static final String author = "halation";
    static final String driver = "com.mysql.jdbc.Driver";
    static final String dbName = "competition";
    static final String url = "jdbc:mysql://39.97.237.248:3306/" + dbName + "?useUnicode=true&useSSL=false&characterEncoding=utf8";
    static final String username = "root";
    static final String password = "123456";
    static final String parent = "ysu.edu";
    static final String dao = "mapper";
    static final String pojo = "pojo";
    static final String service = "service";
    static final String impl = service + ".impl";
    static final String controller = "controller";
    static final String pojoSuper = parent + ".pojo.BaseEntity";
    static final String ignoreField = "id";
    static final boolean all = false;
    static final String tables = "student";
    static final boolean pom = true;
    static final String subProjectDir = "/competition-student";
    static final String pojoProjectDir = "/pojo";

    //计算值
    static final String resource = "/src/main/resources/" + String.join("/", parent.split("\\."));
    static final String mapperXml = resource + "/" + dao;
    static final String java = "/src/main/java/" + String.join("/", parent.split("\\."));
    static final String pojoDir = java + "/" + pojo;
    static final String daoDir = java + "/" + dao;
    static final String serviceDir = java + "/" + service;
    static final String serviceImplDir = java + "/" + String.join("/", impl.split("\\."));
    static final String controllerDir = java + "/" + controller;

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        if (pom) gc.setOutputDir(projectPath + subProjectDir + "/src/main/java");
        else gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        // dsc.setSchemaName("public");
        dsc.setDriverName(driver);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //单个项目配置方法
        pc.setParent(parent);
        pc.setEntity(pojo);
        pc.setMapper(dao);
        pc.setService(service);
        pc.setServiceImpl(impl);
        pc.setController(controller);
        if (pom) {
            //分布式项目包配置方法
            Map<String, String> pathInfo = new HashMap<>();
            pathInfo.put("entity_path", projectPath + pojoProjectDir + pojoDir);
            pathInfo.put("mapper_path", projectPath + subProjectDir + daoDir);
            pathInfo.put("service_path", projectPath + subProjectDir + serviceDir);
            pathInfo.put("service_impl_path", projectPath + subProjectDir + serviceImplDir);
            pathInfo.put("controller_path", projectPath + subProjectDir + controllerDir);
            pc.setPathInfo(pathInfo);
        }
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templateXmlPath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templateXmlPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + (pom ? subProjectDir : "") + mapperXml
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(pojoSuper);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns(ignoreField.split(","));
        //此项说明要生成哪个表（多个表用逗号分隔），如果不写则生成库中所有表
        if (!all)
            strategy.setInclude(tables.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
