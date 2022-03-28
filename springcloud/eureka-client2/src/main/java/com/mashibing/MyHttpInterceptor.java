package com.mashibing;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/*自定义拦截器拦截restTemplate发的请求，和回来的结果*/
public class MyHttpInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("拦截了");
        System.out.println(request.getURI());
        ClientHttpResponse response = execution.execute(request, body);
        System.out.println(response.getHeaders());

        return response;
    }
}
