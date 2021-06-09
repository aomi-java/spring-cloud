package tech.aomi.spring.cloud.client.discovery.retrofit2;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import tech.aomi.spring.cloud.client.discovery.retrofit2.converter.ArgsConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Sean Create At 2019-12-13
 */
public class ClientFactory {

    private final OkHttpClient.Builder clientBuilder;

    private final Retrofit.Builder retrofitBuilder;

    private Retrofit retrofit;

    private boolean build = false;

    private ClientFactory() {
        this.clientBuilder = new OkHttpClient.Builder();

        this.retrofitBuilder = new Retrofit.Builder();
        this.retrofitBuilder.addCallAdapterFactory(new RequestExceptionCallAdapterFactory());
        this.retrofitBuilder.addConverterFactory(ArgsConverterFactory.create());

    }

    public ClientFactory addInterceptor(Interceptor interceptor) {
        this.clientBuilder.addInterceptor(interceptor);
        return this;
    }

    public ClientFactory addNetworkInterceptor(Interceptor interceptor) {
        this.clientBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    public ClientFactory readTimeout(long timeout, TimeUnit unit) {
        this.clientBuilder.readTimeout(timeout, unit);
        return this;
    }

    public ClientFactory writeTimeout(long timeout, TimeUnit unit) {
        this.clientBuilder.writeTimeout(timeout, unit);
        return this;
    }

    public ClientFactory baseUrl(String baseUrl) {
        this.retrofitBuilder.baseUrl(baseUrl);
        return this;
    }

    public ClientFactory addCallAdapterFactory(CallAdapter.Factory factory) {
        this.retrofitBuilder.addCallAdapterFactory(factory);
        return this;
    }

    public ClientFactory addConverterFactory(Converter.Factory factory) {
        this.retrofitBuilder.addConverterFactory(factory);
        return this;
    }

    public Retrofit build() {
        if (this.build) {
            return this.retrofit;
        }
        this.build = true;
        this.retrofitBuilder.client(clientBuilder.build());
        this.retrofit = this.retrofitBuilder.build();
        return this.retrofit;
    }

    public static ClientFactory builder() {
        return new ClientFactory();
    }

}
