import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloSLF4J {
/*    目前所有的Java项目，都离不开日志的功能。大多数项目会使用比较成熟的日志框架，比如Log4J、LogBack、JDK自带的日志等等，SLF4J提供了不同日志的框架的统一接口，让项目中所有模块都能够用统一的门面来调用日志框架*/
    public static void main(String[] args) {
        //slf4j相当于jdbc的接口，具体实现可能是log4j,logback等具体实现，只要有一个实现，slf4j就会自动发现这个实现，把日志记录下来
        Logger logger = LoggerFactory.getLogger(HelloSLF4J.class);
        logger.trace("111111");
        logger.debug("222222");
        logger.info("3333333");
        logger.warn("44444");
        logger.error("5555555");

        logger.info("hello world {} ,{}","java","python" );

    }}
