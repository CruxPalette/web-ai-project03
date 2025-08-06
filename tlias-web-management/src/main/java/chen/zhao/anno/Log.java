package chen.zhao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//修饰注解的注解叫原注解
@Target(ElementType.METHOD) // 注解只能加在方法上
@Retention(RetentionPolicy.RUNTIME)         //在runtime生效
public @interface Log {
}
