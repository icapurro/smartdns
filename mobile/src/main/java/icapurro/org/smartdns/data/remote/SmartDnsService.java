package icapurro.org.smartdns.data.remote;


import com.google.gson.annotations.SerializedName;

import icapurro.org.smartdns.data.model.APIResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface SmartDnsService {

    class Response implements APIResponse {

        @SerializedName("Message")
        private String message;

        @SerializedName("Status")
        private Integer status;

        public String getMessage() {
            return message;
        }
    }

    String ENDPOINT = "https://www.smartdnsproxy.com";

    @GET("/api/IP/update/{id}")
    Observable<Response> getIpUpdate(@Path("id") String id);

    class Creator {

        public static SmartDnsService newSmartDnsService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SmartDnsService.ENDPOINT)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(SmartDnsService.class);
        }
    }
}
