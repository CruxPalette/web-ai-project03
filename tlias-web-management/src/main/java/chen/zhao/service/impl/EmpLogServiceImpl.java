package chen.zhao.service.impl;

import chen.zhao.mapper.EmpLogMapper;
import chen.zhao.pojo.EmpLog;
import chen.zhao.service.EmpLogService;
import chen.zhao.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpLogServiceImpl implements EmpLogService {

    @Autowired
    private EmpLogMapper empLogMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //需要在一个新的事务当中运行
    @Override
    public void insertLog(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }
}
