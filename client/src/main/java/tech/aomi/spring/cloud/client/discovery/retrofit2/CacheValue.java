package tech.aomi.spring.cloud.client.discovery.retrofit2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CacheValue {

    private Retrofit retrofit;

    private OkHttpClient client;

}
