package cn.jantd.modules.demo.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import cn.jantd.modules.demo.test.entity.JantdOrderTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单机票
 * @Author xiagf
 * @Date: 2019-02-15
 * @Version: V1.0
 */
public interface JantdOrderTicketMapper extends BaseMapper<JantdOrderTicket> {

    /**
     * 通过主表外键批量删除客户
     *
     * @param mainId
     * @return
     */
    @Delete("DELETE FROM JANTD_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
    boolean deleteTicketsByMainId(String mainId);


    @Select("SELECT * FROM JANTD_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
    List<JantdOrderTicket> selectTicketsByMainId(String mainId);
}
