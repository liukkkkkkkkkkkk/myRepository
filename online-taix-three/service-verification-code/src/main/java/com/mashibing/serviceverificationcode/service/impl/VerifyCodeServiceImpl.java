package com.mashibing.serviceverificationcode.service.impl;

import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.IdentityConstant;
import com.mashibing.internalcommon.constant.RedisKeyPrefixConstant;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.service.verifycode.response.VerifyCodeResponse;
import com.mashibing.internalcommon.dto.servicevificationcode.VerificationCodeResponse;
import com.mashibing.serviceverificationcode.service.VerifyCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 49178
 * @create 2022/2/15
 */

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseResult<VerificationCodeResponse> generate(int identity, String phoneNumber) {

        //校验 发送时限，三挡验证，不能无限制发短信
//        checkSendCodeTimeLimit(phoneNumber);

        // 0.9*9=8.1+1 9,去掉首位为0的情况。 0.11225478552211(0.0-<1)
        String code = String.valueOf((int)((Math.random() * 9 + 1) * Math.pow(10,5)));

        /**
         * 有人用这种写法。生成6位code，错误用法，虽然大部分情况下结果正确，但不能这么用，偶尔位数不够？
         */
//        String code = String.valueOf(new Random().nextInt(1000000));

        //生成redis key
        String keyPre = generateKeyPreByIdentity(identity);
        String key = keyPre + phoneNumber;
        //存redis，2分钟过期
        BoundValueOperations<String, String> codeRedis = redisTemplate.boundValueOps(key);

//        Boolean aBoolean = codeRedis.setIfAbsent(code);
//        if (aBoolean){
//            codeRedis.expire(2,TimeUnit.MINUTES);
//        }
        codeRedis.set(code,5, TimeUnit.MINUTES);
//        codeRedis.expire(2,TimeUnit.MINUTES);  Set expire要写在一起，防止刚Set完，服务就挂掉了

        //返回
        VerifyCodeResponse result = new VerifyCodeResponse();
        result.setCode(code);
        return ResponseResult.success(result);
    }

    /**
     * 用户传进来的验证码和redis中验证码一致，校验通过，否则不过
     * @param identity
     * @param phoneNo
     * @param code
     * @return
     */
    public ResponseResult verify(int identity,String phoneNo,String code){
        //三档验证


        //生成redis key
        String keyPre = generateKeyPreByIdentity(identity);
        String key = keyPre + phoneNo;
        BoundValueOperations<String, String> codeRedis = redisTemplate.boundValueOps(key);
        String redisCode = codeRedis.get();

        if(StringUtils.isNotBlank(code)
                && StringUtils.isNotBlank(redisCode)
                && code.trim().equals(redisCode.trim())) {

            return ResponseResult.success("");
        }else {
            return ResponseResult.fail(CommonStatusEnum.VERIFY_CODE_ERROR.getCode(), CommonStatusEnum.VERIFY_CODE_ERROR.getValue());
        }
    }


    /**
     * 判断此手机号发送时限限制
     * @param phoneNumber
     * @return
     */
    private ResponseResult checkSendCodeTimeLimit(String phoneNumber){
        //判断是否有 限制1分钟，10分钟，24小时。

        return ResponseResult.success("");
    }

    /**
     * 三档验证校验
     * @param phoneNumber
     * @param code
     * @return
     */
    private ResponseResult checkCodeThreeLimit(String phoneNumber,String code){
        //看流程图

        return ResponseResult.success("");
    }

    /** 如何提升QPS？
     *  提高并发数
     *  （1. 使用多线程，
     *  2. 增加各种连接数 tomcat mysql redis等
     *  3. 服务无状态，便于横向扩展（扩机器）
     *  4. 让服务能力对等平均分配 (serviceUrl打乱顺序，不要让一个eurekaServer负载过大，其它的闲着）
     *  ）
     *  减少响应时间
     *  （
     *  1.异步（最终一致性，不需要及时） 流量削锋
     *  2. 缓存 （减少db读取，减少磁盘IO，读多，写少情况）
     *  3. 数据库优化
     *  4. 大量数据，分批次返回
     *  5. 减少调用链
     *  6. 长连接 （关于订单状态，不要司机端轮询去查询，使用长连接）
     *
     *
     *
     * 估算线程数的方法
     * 16核 应该开几个线程
     * 线程数 = cpu可用核数 /(1-阻塞系数)         (阻塞系数:IO密集型接近1，计算密集型接近0) 阻塞系数没有定量的值，只能定性的分析
     * @param args
     */
    public static void main(String[] args) {
        //效率低
        int sum =10000;
        long start =System.currentTimeMillis();
        for (int i = 0; i <sum ; i++) {
            String substring = (Math.random() + "").substring(2, 8);
            System.out.println(substring);
        }
        long end =System.currentTimeMillis();
        System.out.println("time:"+(end-start));

        //这种效率要高些
        long start2 =System.currentTimeMillis();
        for (int i = 0; i <sum ; i++) {
           String code = String.valueOf((int)((Math.random()*9+1)*Math.pow(10,5)));
            System.out.println(code);

        }
        long end2 =System.currentTimeMillis();
        System.out.println("time2:"+(end2-start2));

        System.out.println((int)((Math.random()*9+1)));
        /**
         * 有问题 不能总取出6位
         */
    /*    for (int i = 0; i <1000 ; i++) {
            String code =String.valueOf(new Random().nextInt(100000));
            System.out.println(code);
        }*/

    }

    /**
     * 根据身份类型生成对应的缓存key
     * @param identity
     * @return
     */
    private String generateKeyPreByIdentity(int identity){
        String keyPre = "";
        if (identity == IdentityConstant.PASSENGER){
            keyPre = RedisKeyPrefixConstant.PASSENGER_LOGIN_CODE_KEY_PRE;
        }else if (identity == IdentityConstant.DRIVER){
            keyPre = RedisKeyPrefixConstant.DRIVER_LOGIN_CODE_KEY_PRE;
        }
        return keyPre;
    }

}
