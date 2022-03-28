package com.userapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

//@RequestMapping("/user")
public interface UserApi {

    @RequestMapping("/alive")
    /**
     * 查看当前服务状态~~~
     * @return (* ￣ 3)(ε ￣ *)
     */
    public String alive();

    @RequestMapping("/getById")
    public String getById(Integer id);


    //post方式远程调用
    @PostMapping("getMap4")
    public Map getMap4(@RequestBody Map map);
}
