package tech.aomi.spring.cloud.client.discovery.retrofit2.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import tech.aomi.common.exception.ServiceException;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 收单系统响应参数转换
 *
 * @param <T>
 */
@Slf4j
final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final ObjectMapper mapper;

    private final Type returnType;

    ResponseBodyConverter(ObjectMapper mapper, Type type) {
        this.mapper = mapper;
        this.returnType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {

            JsonNode result = mapper.readValue(value.byteStream(), JsonNode.class);
//            JavaType javaType = mapper.getTypeFactory().constructType(Result.Entity.class);
//            ObjectReader reader = mapper.readerFor(javaType);
//            Result.Entity entity = reader.readValue(value.charStream());
            LOGGER.debug("返回结果: {}", result);

            JsonNode obj = result.get("payload");



            if (!result.get("success").booleanValue()) {
                String status = result.get("status") == null ? "" : result.get("status").toString();
                String describe = result.get("describe") == null ? "" : result.get("describe").toString();

                HashMap<Object, Object> payload = null;
                if (null != obj && obj.isNull()) {
                    payload = mapper.readValue(obj.toString(), new TypeReference<HashMap<Object, Object>>() {
                    });
                }

                LOGGER.error("处理失败: {}, {}, {}", status, describe, payload);
                ServiceException e = new ServiceException(describe);
                e.setErrorCode(status);
                e.setPayload(payload);
                throw e;
            }

            JavaType javaType = mapper.getTypeFactory().constructType(returnType);
            ObjectReader reader = mapper.readerFor(javaType);

            StringWriter writer = new StringWriter();
            if (null != obj && obj.isNull()) {
                mapper.writeValue(writer, obj);
            }
            return reader.readValue(writer.toString());
        } finally {
            value.close();
        }
    }
}