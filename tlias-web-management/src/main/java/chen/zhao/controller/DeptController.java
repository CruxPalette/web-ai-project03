package chen.zhao.controller;

import chen.zhao.anno.Log;
import chen.zhao.pojo.Dept;
import chen.zhao.pojo.Result;
import chen.zhao.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//把所有的公共的地址统一抽取到这个类上 属于优化
@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    //方式一 或者直接在最上面@Slf4j
    //private static final Logger log = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    private DeptService deptService;

//    部门列表查询
//    requestmethod 是枚举 但是下面这个比较繁琐
//    @RequestMapping(value = "/depts", method = RequestMethod.GET)
    @GetMapping//("/depts")
    public Result list() {
//        System.out.println("查询全部部门数据");
        log.info("查询全部部门数据");

//        这个因为是找到所有的部门 肯定是要把所有的部门放在一个list里面
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }


    /**
     * 根据id删除部门 - delete http://localhost:8080/depts?id=1
     * 方式1️⃣ HttpServeletRequest
     * public Result delete(HttpServletRequest request){
     *     String idStr = request.getParameter("id");
     *     int id = Integer.parseInt(idStr);
     *
     *     System.out.println("根据ID删除部门: " + id);
     *     return Result.success();
     * 方式2️⃣
     * 如果请求参数名与形参变量名相同，直接定义方法形参即可接收。（省略@RequestParam）
     * public Result delete(@RequestParam("id") Integer deptId){
     * 一旦声明了@RequesrParam 该参数在请求时 必须传递 如果不传递将会报错
     * 不过可以@RequestParam(value = "id", required = false)
     */
//    方式三 前端传递的请求参数名与服务端方法形参名一致
    @Log
    @DeleteMapping//("/depts")
    public Result delete(Integer id) {
//        System.out.println("根据id删除部门: id=" + id);
        log.info("根据id删除部门: {}", id);      //占位符

        deptService.deleteById(id);
        return Result.success(); //因为这里的data可以为null
    }

    /**
     * 新增部门
     * JSON格式的数据可以封装到某一个对象当中 在请求体中
     * @return
     */
    @Log
    @PostMapping//("/depts")
    public Result add(@RequestBody Dept dept) {
//        System.out.println("新增部门: " + dept);
        log.info("新增部门: {}", dept);

        deptService.add(dept);
        return Result.success();
    }

    /**
     * 查询回显 点击编辑 然后将原本的内容展示出来写在文本框中
     * 根据id 查询
     * 在url中传递的参数 叫做路径参数 接收路径参数 需要@PathVariable
     * 需要deptId来接受路径参数id
     */
//    @GetMapping("/depts/{id}")
//    public Result getInfo(@PathVariable("id") Integer deptId) {
//        System.out.println("根据ID查询部门数据: " + deptId);
//        Dept dept = deptService.getInfo(deptId);
//        return Result.success(dept);
//    }
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
//        System.out.println("根据ID查询部门数据: " + id);
        log.info("根据ID查询部门数据: {}", id);

        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    // 也可以带好几个路径参数
//    @GetMapping("/depts/{id}/{sta}")
//    public Result getInfo(@PathVariable Integer id, @PathVariable Integer sta) {
//        //
//    }

    /**
     * 修改部门 - PUT http://localhost:8080/depts  请求参数：{"id":1,"name":"研发部"}
     */
    @Log
    @PutMapping//("/depts")
    public Result update(@RequestBody Dept dept){
        System.out.println("修改部门, dept=" + dept);
        log.info("修改部门, dept={}", dept);

        deptService.update(dept);
        return Result.success();
    }
}
