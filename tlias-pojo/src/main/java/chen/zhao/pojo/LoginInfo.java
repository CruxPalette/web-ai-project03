package chen.zhao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装登陆结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    private Integer id;
    private String username;
    private String name;
    private String token;
}
