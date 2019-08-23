package cn.jantd.modules.demo.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import cn.jantd.modules.demo.test.entity.JantdOrderCustomer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单客户
 * @Author xiagf
 * @Date: 2019-02-15
 * @Version: V1.0
 */
public interface JantdOrderCustomerMapper extends BaseMapper<JantdOrderCustomer> {

    /**
     * 通过主表外键批量删除客户
     *
     * @param mainId
     * @return
     */
    @Delete("DELETE FROM JANTD_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
    boolean deleteCustomersByMainId(String mainId);

    @Select("SELECT * FROM JANTD_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
    List<JantdOrderCustomer> selectCustomersByMainId(String mainId);
}
