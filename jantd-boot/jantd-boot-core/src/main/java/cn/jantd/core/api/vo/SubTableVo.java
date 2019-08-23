package cn.jantd.core.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="一对多子表对象属性", description="一对多子表属性")
public class SubTableVo {
    private String tableName;
    private String entity;
    private String ftlDescription;
    private String[] foreignKeys;
    private String[] originalForeignKeys;
    private List<ColumnVo> colums;
    private List<ColumnVo> originalColumns;
    private String entityPackage;

    public SubTableVo() {
    }

}
