package chen.zhao.mapper;

import chen.zhao.pojo.Emp;
import chen.zhao.pojo.EmpQueryParam;
import chen.zhao.pojo.LoginInfo;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 操作员工基本信息
 *
 */
@Mapper
public interface EmpMapper {


    // --------------------------原始分页查询实现------------------
    /**
     * 查询总记录数
     */
//    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
//    public Long count();
//
//    /**
//     * 分页查询
//     * 后面返回的List<Emp>没有包括部门名称 需要加上
//     * 起一个别名deptName
//     */
//    @Select("select e.*, d.name deptName from emp e left outer join dept d on e.dept_id = d.id order by e.update_time desc " +
//            "limit #{start}, #{pageSize}")
//    public List<Emp> list(Integer start, Integer pageSize);


//全新pageHelper方法
//    @Select("select e.*, d.name deptName from emp e left outer join dept d on e.dept_id = d.id order by e.update_time desc ")
//    public List<Emp> list(String name, Integer gender, LocalDate begin, LocalDate end);

    /**
     * 条件查询员工映射信息
     * @param empQueryParam
     * @return
     */
    List<Emp> list(EmpQueryParam empQueryParam);

    /**
     * 主键返回 @Options 使用生成的主键 并获取 mybatis里面的 主键返回
     * @param emp
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)" +
            "values(#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    /**
     * 根据ID批量删除员工的基本信息
     * @param ids
     */
    void deleteByIds(List<Integer> ids);

    Emp getById(Integer id);

//    根据id来更新字段
    void updateById(Emp emp);


    /**
     * 员工统计 查询返回结果
     * 员工职位人数
     * pos和num
     */
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();


    @MapKey("name")
    List<Map> countEmpGenderData();

    /**
     * 根据用户名和密码查询用户
     * @param emp
     * @return
     */
    @Select("select id, username, name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);
}
