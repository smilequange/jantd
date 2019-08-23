package cn.jantd.core.util;

import cn.jantd.core.api.vo.ColumnVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * @author xiagf
 * @description 获取表结构信息
 * @date 2019-08-05
 */
public class CodeGenerateUtil {
    private CodeGenerateUtil() {
    }

    private static final ResourceBundle JANTD_CONFIG = ResourceBundle.getBundle("templates/jantd_config");

    public static List<ColumnVo> getTableColumns(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
                                                 String tableName, String dataBase) throws SQLException {
        List<ColumnVo> columnVoList = new ArrayList<>();
        JdbcUtils jdbcUtils = new JdbcUtils(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);

        String columnSql = String.format("select column_name,data_type,column_key,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable from information_schema.columns where table_name = '%s' AND table_schema = '%s'", tableName, dataBase);
        ColumnVo columnVo = null;
        List<Map> columnList = jdbcUtils.selectByParams(columnSql, null);
        Iterator<Map> columnIterator = columnList.iterator();
        while (columnIterator.hasNext()) {
            Map<String, String> currColumn = columnIterator.next();
            columnVo = new ColumnVo();

            String columnName = currColumn.get("COLUMN_NAME");
            String methodName = columnToJava(columnName);

            // db字段名称
            columnVo.setFieldDbName(columnName);
            // db字段转为驼峰名称
            columnVo.setFieldName(StringUtils.uncapitalize(methodName));
            // 数据库字段类型
            columnVo.setFieldDbType(currColumn.get("DATA_TYPE"));
            // 列注释
            columnVo.setFiledComment(currColumn.get("COLUMN_COMMENT"));
            // 主键
            columnVo.setColumnKey(currColumn.get("COLUMN_KEY"));

            // 属性类型对应java的类型
            columnVo.setFieldType(JANTD_CONFIG.getString(columnVo.getFieldDbType()));
            // 是否必输
            columnVo.setNullable(currColumn.get("IS_NULLABLE"));

            columnVoList.add(columnVo);

        }
        return columnVoList;
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 将列名转为get set字段名 例如order_id ==>OrderId
     *
     * @param var0
     * @return
     */
    public static String columnExchange(String var0) {
        String[] var1 = var0.split("_");
        var0 = "";
        int var2 = 0;

        for (int var3 = var1.length; var2 < var3; ++var2) {
            if (var2 > 0) {
                String var4 = var1[var2].toLowerCase();
                var4 = var4.substring(0, 1).toUpperCase() + var4.substring(1, var4.length());
                var0 = var0 + var4;
            } else {
                var0 = var0 + var1[var2].toLowerCase();
            }
        }

        var0 = var0.substring(0, 1).toUpperCase() + var0.substring(1);
        return var0;
    }
}
