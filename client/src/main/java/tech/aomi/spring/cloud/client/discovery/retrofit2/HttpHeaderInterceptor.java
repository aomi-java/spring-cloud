package tech.aomi.spring.cloud.client.discovery.retrofit2;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class HttpHeaderInterceptor implements Interceptor {

    private final Map<String, String> headers;

    public HttpHeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        if (null != headers && headers.size() > 0) {
            LOGGER.debug("添加Header: {}", headers);
            headers.forEach(builder::addHeader);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);

    }

}
