import org.junit.jupiter.api.Test;

/**
 * @author 49178
 * @create 2022/1/29
 */
public class TestService {
    public void test1(){
        TestEntity testEntity = new TestEntity();
        testEntity.test1();
    }
    @Test
    public void test2(){
      TestCommon testCommon =new TestCommon();
      testCommon.test1();
    }
}
