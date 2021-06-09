package tech.aomi.spring.cloud.client.discovery.retrofit2;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import tech.aomi.common.exception.ServiceException;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 请求异常处理
 * 抛出SDK内部异常
 *
 * @author 田尘殇Sean Create At 2018/10/10 20:58
 */
@Slf4j
public class RequestExceptionCallAdapter implements CallAdapter {

    private final Type returnType;

    public RequestExceptionCallAdapter(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public Type responseType() {
        return returnType;
    }

    @Override
    public Object adapt(Call call) {
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                LOGGER.error("服务器响应状态不成功:{}", response.code());
                ServiceException e = new ServiceException("服务处理失败:" + response.code());
                e.setPayload(response.code());
                throw e;
            }
            return response.body();
        } catch (IOException e) {
            LOGGER.error("请求服务失败: {}", e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

}
