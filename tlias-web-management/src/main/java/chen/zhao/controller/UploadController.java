package chen.zhao.controller;

import chen.zhao.pojo.Result;
import chen.zhao.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    private final AliyunOSSOperator aliyunOSSOperator;

    public UploadController(AliyunOSSOperator aliyunOSSOperator) {
        this.aliyunOSSOperator = aliyunOSSOperator;
    }

    /**
     * 本地磁盘存储方案
     * @param name
     * @param age
     * @param file
     * @return
     * @throws IOException
     */
//    @PostMapping("/upload")
//    public Result upload(@RequestParam("name") String name, @RequestParam("age") Integer age, @RequestParam("file") MultipartFile file) throws IOException {
//        log.info("接受参数: {},{},{}", name, age, file);
//        // 将图片本地存储
//        // 获取原始文件名 文件名如果一样的 会被覆盖掉
//        String originalFilename = file.getOriginalFilename(); //1.jpg 2.png
//        // 保存文件 转存到磁盘文件
//
//        //获取扩展名 获取最后一个点所在位置
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//        String newFileName = UUID.randomUUID().toString() + extension;
//        // 如何保证字符串不重复文件名？用UUID
//        file.transferTo(new File("/Users/zhaochen/Desktop/images/" + newFileName));
//        return Result.success();
//    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传: {}", file.getOriginalFilename());
        // 将文件交给OSS存储管理
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        log.info("文件上传OSS, url: {}", url);

        return Result.success(url);
    }
}
