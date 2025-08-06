package chen.zhao.service;

import chen.zhao.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();
    /**
     * 根据id删除部门
     */
    void deleteById(Integer id);

    void add(Dept dept);

    Dept getById(Integer id);

    void update(Dept dept);
}
