package chen.zhao.service.impl;

import chen.zhao.exception.BusinessException;
import chen.zhao.mapper.ClazzMapper;
import chen.zhao.mapper.StudentMapper;
import chen.zhao.pojo.Clazz;
import chen.zhao.pojo.PageResult;
import chen.zhao.service.ClazzService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void save(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }


    @Override
    public PageResult page(String name, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Clazz> dataList = clazzMapper.list(name,begin,end);
        Page<Clazz> p = (Page<Clazz>) dataList;

        return new PageResult(p.getTotal(), p.getResult());
    }


    @Override
    public Clazz getInfo(Integer id) {
        return clazzMapper.getInfo(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public void deleteById(Integer id) {
        //1. 查询班级下是否有学员
        Integer count = studentMapper.countByClazzId(id);
        if(count > 0){
            throw new BusinessException("班级下有学员, 不能直接删除~");
        }
        //2. 如果没有, 再删除班级信息
        clazzMapper.deleteById(id);
    }

    @Override
    public List<Clazz> findAll() {
        return clazzMapper.findAll();
    }
}
