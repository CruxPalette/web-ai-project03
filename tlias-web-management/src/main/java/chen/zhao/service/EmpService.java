package chen.zhao.service;

import chen.zhao.pojo.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
//    PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);

    @GetMapping
    public PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void delete(List<Integer> ids);

    Emp getInfo(Integer id);

    /**
     * 更新员工信息
     * @param emp
     */
    void update(Emp emp);

    LoginInfo login(Emp emp);
}
