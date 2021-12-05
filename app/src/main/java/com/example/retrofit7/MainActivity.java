package com.example.retrofit7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    ImageView mg;
    List<Model> movieList=new ArrayList<>();
    RecyclerView recyclerView;
    //String urlOfSite="https://jsonplaceholder.typicode.com/";
    String urlOfSite="https://run.mocky.io/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.my_recycler);
        tv=findViewById(R.id.tv);
        mg=findViewById(R.id.image);


        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(urlOfSite)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient().build())
                .build();

        MyApi api=retrofit.create(MyApi.class);
        Call<List<Model>> call=api.getModels();

        call.enqueue(new Callback<List<Model>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if(response.code() !=200){
                    String t="Check connection";
                    tv.setText(t);
                    return;
                }
                List<Model> data=response.body();
                for (int i = 0; i <data.size() ; i++) {
                    HttpsTrustManager.allowAllSSL();
                    movieList.addAll(data);
                PutDataIntoRecyclerView(movieList);


                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<Model>> call, Throwable t) {
                tv.setText(t.getMessage());

            }
        });
    }

    private void PutDataIntoRecyclerView(List<Model> movieList) {
        Adaptery adaptery=new Adaptery(this,movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptery);
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}