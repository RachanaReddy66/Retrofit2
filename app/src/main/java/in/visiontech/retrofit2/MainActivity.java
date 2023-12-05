package in.visiontech.retrofit2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private OkHttpClient client;
    Button get;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get=findViewById(R.id.hit);
        context = this;
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                try {
                    System.setProperty("javax.net.ssl.keyStoreProvider", "PKCS12");
                    //InputStream privateKeyInputStream = context.getResources().openRawResource(R.raw.eposservice_jk_gov_in);
                    InputStream privateKeyInputStream = context.getResources().getAssets().open("eposservice_jk_gov_in.crt");
                    System.out.println("privatekeyInputStream:" + privateKeyInputStream);
                    KeyStore keyStore = KeyStore.getInstance("PKCS12");
                    keyStore.load(null, null);
                    System.out.println("After loading keystore");
                    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    keyManagerFactory.init(keyStore, null);
                    System.out.println("KeyManager initialized");
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
                    System.out.println("SSLContext:" + sslContext);


                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null); // Initialize an empty trust store
                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                    X509Certificate rootCACertificate = (X509Certificate) certificateFactory.generateCertificate(privateKeyInputStream);
                    trustStore.setCertificateEntry("root_ca_alias", rootCACertificate);

// Create a TrustManagerFactory and initialize it with the trust store
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(trustStore);


                    httpClient.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0]);
                } catch (Exception e) {
                    System.out.println("Error in catch block :" + e);
                }

                client = httpClient.build();

                Retrofit retrofit = new Retrofit.Builder()
                        //.baseUrl("https://eposservice.jk.gov.in/ePosServiceNE3_1/jdCommoneposServiceRes?wsdl")
                        .baseUrl("https://eposservice.jk.gov.in/ePosServiceNE3_1/")
                        .client(client)
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();

                MyApiService apiService = retrofit.create(MyApiService.class);

                String xmlData = "<?xml version='1.0' encoding='UTF-8' standalone='no' ?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns1=\"http://service.fetch.rationcard/\"><SOAP-ENV:Body><ns1:getPDSFpsNoDetails><VersionNo>3.1</VersionNo><deviceID>8309217739</deviceID><token>7797602c3da57f23e57a259b60358622</token><key>111</key><simID>89918680408025225797</simID><checkSum>0c9eaeea14854328f5a9f6b030518f14  </checkSum><longtude>0.000</longtude><latitude>0.000</latitude><vendorId>VISIONTEKNE</vendorId><simStatus>89918680408025225797</simStatus><macId>7C:F0:BA:F4:BA:D9</macId></ns1:getPDSFpsNoDetails></SOAP-ENV:Body></SOAP-ENV:Envelope>";
                MediaType mediaType = MediaType.parse("application/xml; charset=utf-8");
                RequestBody xmlRequestBody = RequestBody.create(xmlData, mediaType);

                Call<ResponseBody> call = apiService.postXmlData(xmlRequestBody);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                System.out.println("Response success");
                                String xmlResponse = response.body().string();
                                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                                intent.putExtra("XmlString",xmlResponse);
                                startActivity(intent);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Retrofit Error", "Failed to make the request", t);

                    }
                });
            }
        });
    }
}
