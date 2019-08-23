package cn.jantd.core.api.vo;

import lombok.Data;

@Data
public class ColumnVo {
    private String fieldDbName;
    private String fieldName;
    private String filedComment = "";
    private String fieldType = "";
    private String fieldDbType = "";
    private String charmaxLength = "";
    private String precision;
    private String scale;
    private String nullable;
    private String columnKey;

    public ColumnVo() {
    }
}
