package chen.zhao.mapper;

import chen.zhao.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工工作经历操作
 */
@Mapper
public interface EmpExprMapper {

    /**
     * 批量保存员工的工作经历
     * 需要动态变化插入 动态sql
     */
    void insertBatch(List<EmpExpr> exprList);

    void deleteByEmpIds(List<Integer> ids);
}
