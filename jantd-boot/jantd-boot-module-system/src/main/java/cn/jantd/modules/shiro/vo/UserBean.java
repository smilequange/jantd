package cn.jantd.modules.shiro.vo;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * @author xiagf
 * @date 2019-07-04
 */
@Data
public class UserBean {
    private String username;
    private String password;
    /**
     * 用户所有角色值，用于shiro做角色权限的判断
     */
    private Set<String> roles = new HashSet<>();
    /**
     * 用户所有权限值，用于shiro做资源权限的判断
     */
    private Set<String> perms = new HashSet<>();
}
