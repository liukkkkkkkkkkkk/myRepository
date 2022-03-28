package socketStepServer.step3;

import java.io.IOException;

/**
 * @author 49178
 * @create 2022/2/3
 */
@FunctionalInterface
public interface IHanderInterface {
    void  handler(Request request, Response response) throws IOException;
}
