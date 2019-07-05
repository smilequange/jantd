package cn.jantd.modules.system.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xiagf
 * @date 2019-07-04
 */
@Data
public class SysUserRoleVO implements Serializable {
    private static final long serialVersionUID = 3368937857346949270L;

    /**
     * 部门id
     */
    private String roleId;
    /**
     * 对应的用户id集合
     */
    private List<String> userIdList;

    public SysUserRoleVO(String roleId, List<String> userIdList) {
        super();
        this.roleId = roleId;
        this.userIdList = userIdList;
    }

    public SysUserRoleVO() {

    }


}
