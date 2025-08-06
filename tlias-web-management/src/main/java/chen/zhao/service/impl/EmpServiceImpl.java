package chen.zhao.service.impl;

import chen.zhao.mapper.EmpExprMapper;
import chen.zhao.mapper.EmpLogMapper;
import chen.zhao.mapper.EmpMapper;
import chen.zhao.pojo.*;
import chen.zhao.service.EmpLogService;
import chen.zhao.service.EmpService;
import chen.zhao.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    private EmpLogService empLogService;

//    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize) {
//        Long total = empMapper.count();
//        // 需要获取起始索引
//        Integer start = (page - 1) * pageSize;
//        List<Emp> rows = empMapper.list(start, pageSize);
//
//        // 封装结果 PageResult
//        return new PageResult<Emp>(total, rows);
//    }

    /**
     * PageHelper
     * @param page 页码
     * @param pageSize
     * @return
     * 注意事项：使用pagehelper的时候 sql后面不要加分号 因为pagehelper自动加上limit
     * 加上分号就不行了 不是正常的pagehelper了
     *
     * 2.仅仅能对紧跟在后面的第一个查询生效 也就是下面2.执行查询的那个
     * 如果再有的话就不行
     */
//    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end) {
//        //1.设置分页参数pagehelper
//        PageHelper.startPage(page, pageSize);
//        // 2.执行查询
//        List<Emp> empList = empMapper.list(name, gender, begin, end);
//        // 3.解析查询结果 并封装 强转
//        Page<Emp> p = (Page<Emp>) empList;
//        return new PageResult<Emp>(p.getTotal(), p.getResult());
//    }

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        //1.设置分页参数pagehelper
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        // 2.执行查询
        List<Emp> empList = empMapper.list(empQueryParam);
        // 3.解析查询结果 并封装 强转
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<Emp>(p.getTotal(), p.getResult());
    }


    // 这个rollbackfor是所有的异常都回滚 Exception.class
    //假如我们想让所有的异常都回滚，需要来配置@Transactional注解当中的rollbackFor属性，通过rollbackFor这个属性可以指定出现何种异常类型回滚事务。
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        // 1.保存员工基本信息
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            //2.保存员工基本信息
            empMapper.insert(emp);

            // 人为构建一个异常
            // 上面的基本信息已经保存成功 但是下面的工作经历保存失败了
            // int i = 1/0;

            //3. 保存员工的工作经历信息 - 批量
            Integer empId = emp.getId();    //mybatis主键返回
            List<EmpExpr> exprList = emp.getExprList();
            if(!CollectionUtils.isEmpty(exprList)){
                exprList.forEach(empExpr -> empExpr.setEmpId(empId));
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            // 在finally就是 无论上面怎么样 下面这段代码都执行
            // 记录操作日志 调用在service中的insertlog方法 所以要注入一个service而不是mapper 研究事物传播行为
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工: " + emp);
            empLogService.insertLog(empLog);
        }


    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        // 这两个操作必须同时成功同是失败所以必须加上事务控制@Transactonal
        // 1.批量删除员工基本信息
        empMapper.deleteByIds(ids);
        // 2.批量删除员工的工作经历信息
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

//因为包括了好多操作
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        // 1.根据id修改员工的基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
        // 2. 根据id修改员工的工作经历信息
        // 2.1 先删除
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        // 2.2 再添加
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public LoginInfo login(Emp emp) {
        // 1.调用mapper接口，根据用户名和密码查询员工信息
        Emp e = empMapper.selectByUsernameAndPassword(emp);
        // 2.判断是否存在这个员工 如果存在 组装登陆成功信息
        if (e != null) {
            log.info("登陆成功，员工信息：{}", e);
            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            String jwt = JwtUtils.generateToken(claims);
            return new LoginInfo(e.getId(), e.getUsername(),e.getName(), jwt);
        }
        // 3.不存在 返回null
        return null;
    }


}
