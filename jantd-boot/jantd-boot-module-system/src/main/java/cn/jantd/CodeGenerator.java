package cn.jantd;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    private static final ResourceBundle r = ResourceBundle.getBundle("templates/jantd_config");

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {

        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        globalConfigSetUp(autoGenerator);

        // 数据源配置
        dataSourceSetUp(autoGenerator);

        // 包配置
        PackageConfig pc = packageConfigSetUp(autoGenerator);

        // 自定义配置
        InjectionConfig cfg = injectionConfigSetUp();
        selfDefineSetUp(autoGenerator, pc, cfg);

        // 配置模板
        templateConfigSetUp(autoGenerator);

        // 策略配置
        strategyConfigSetUp(autoGenerator, pc);
        // 代码生成
        autoGenerator.execute();
    }

    /**
     * 注入设置
     *
     * @return
     */
    private static InjectionConfig injectionConfigSetUp() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
    }

    /**
     * 自定义配置
     *
     * @param autoGenerator
     * @param pc
     * @param cfg
     */
    private static void selfDefineSetUp(AutoGenerator autoGenerator, PackageConfig pc, InjectionConfig cfg) {
        // 自定义输出配置
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        List<FileOutConfig> focList = new ArrayList<>();
        cfg.setFileOutConfigList(focList);
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return r.getString("project_path") + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        autoGenerator.setCfg(cfg);
    }

    /**
     * 策略配置
     *
     * @param autoGenerator
     * @param pc
     */
    private static void strategyConfigSetUp(AutoGenerator autoGenerator, PackageConfig pc) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        autoGenerator.setStrategy(strategy);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
    }

    /**
     * 配置模板
     *
     * @param autoGenerator
     */
    private static void templateConfigSetUp(AutoGenerator autoGenerator) {
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setController("/templates/codeOneTemplates/controller.java");
        templateConfig.setEntity("/templates/codeOneTemplates/entity.java");
        templateConfig.setService("/templates/codeOneTemplates/service.java");
        templateConfig.setServiceImpl("/templates/codeOneTemplates/serviceImpl.java");
        templateConfig.setMapper("/templates/codeOneTemplates/mapper.java");
        templateConfig.setXml("/templates/codeOneTemplates/mapper.xml");

        autoGenerator.setTemplate(templateConfig);
    }

    /**
     * 包设置
     *
     * @param autoGenerator
     * @return
     */
    private static PackageConfig packageConfigSetUp(AutoGenerator autoGenerator) {
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent(r.getString("bussi_package"));
        System.out.println(pc.getParent());
        autoGenerator.setPackageInfo(pc);
        return pc;
    }

    /**
     * 数据源设置
     *
     * @param autoGenerator
     */
    private static void dataSourceSetUp(AutoGenerator autoGenerator) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl(r.getString("url"));
        dsc.setDriverName(r.getString("diver_name"));
        dsc.setUsername(r.getString("username"));
        dsc.setPassword(r.getString("password"));
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });
        autoGenerator.setDataSource(dsc);
    }

    /**
     * 全局配置
     *
     * @param autoGenerator
     */
    private static void globalConfigSetUp(AutoGenerator autoGenerator) {
        GlobalConfig gc = new GlobalConfig();
        String projectPath = r.getString("project_path");
        gc.setOutputDir(projectPath + r.getString("source_root_package"));
        gc.setAuthor(r.getString("author"));
        gc.setOpen(false);
        // XML columList
        gc.setBaseColumnList(true);
        // 是否覆盖文件
        gc.setFileOverride(true);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("I%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        autoGenerator.setGlobalConfig(gc);
    }

}
