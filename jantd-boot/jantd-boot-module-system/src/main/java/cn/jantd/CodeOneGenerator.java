package cn.jantd;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 控制台输入模块表名回车自动生成对应项目目录中
 *
 * @author xiagf
 * @date 2019-07-18
 */
public class CodeOneGenerator {

    private static final ResourceBundle JANTD_CONFIG = ResourceBundle.getBundle("templates/jantd_config");

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
        InjectionConfig injectionConfig = new InjectionConfig() {
            //自定义属性注入:abc
            //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(16);
                map.put("pageFilterFields", JANTD_CONFIG.getString("page_filter_fields"));
                this.setMap(map);
            }
        };
        return injectionConfig;
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
        String vueTemplatePath = "/templates/codeOneTemplates/list.vue.ftl";
        String vueModelTemplatePath = "/templates/codeOneTemplates/modal.vue.ftl";
        List<FileOutConfig> focList = new ArrayList<>();
        // ##List.vue生成
        focList.add(new FileOutConfig(vueTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return JANTD_CONFIG.getString("project_path") + JANTD_CONFIG.getString("source_root_package") + "/cn/jantd/modules/" + pc.getModuleName() + "/vue"
                        + "/" + tableInfo.getEntityName() + "List" + ".vue";
            }
        });
        // ##modal.vue生成
        focList.add(new FileOutConfig(vueModelTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return JANTD_CONFIG.getString("project_path") + JANTD_CONFIG.getString("source_root_package") + "/cn/jantd/modules/" + pc.getModuleName() + "/vue/modules"
                        + "/" + tableInfo.getEntityName() + "Modal" + ".vue";
            }
        });
        cfg.setFileOutConfigList(focList);
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
        // 指定自定义模板路径，注意不要带上.ftl, 会根据使用的模板引擎自动识别
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
        pc.setParent(JANTD_CONFIG.getString("bussi_package"));
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
        DataSourceConfig dsc = new DataSourceConfig().setDbQuery(new MySqlQuery() {

            /**
             * 重写父类预留查询自定义字段<br>
             * 这里查询的 SQL 对应父类 tableFieldsSql 的查询字段，默认不能满足你的需求请重写它<br>
             * 模板中调用：  table.fields 获取所有字段信息，
             * 然后循环字段获取 field.customMap 从 MAP 中获取注入字段如下  NULL 或者 PRIVILEGES
             */
            @Override
            public String[] fieldCustom() {
                return new String[]{"NULL", "PRIVILEGES"};
            }
        });
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl(JANTD_CONFIG.getString("url"));
        dsc.setDriverName(JANTD_CONFIG.getString("diver_name"));
        dsc.setUsername(JANTD_CONFIG.getString("username"));
        dsc.setPassword(JANTD_CONFIG.getString("password"));
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
        String projectPath = JANTD_CONFIG.getString("project_path");
        gc.setOutputDir(projectPath + JANTD_CONFIG.getString("source_root_package"));
        gc.setAuthor(JANTD_CONFIG.getString("author"));
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
