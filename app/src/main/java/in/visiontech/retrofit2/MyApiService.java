package in.visiontech.retrofit2;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApiService {
  @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
  @POST("jdCommoneposServiceRes?wsdl")
  Call<ResponseBody> postXmlData(@Body RequestBody xml);
}


/*
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApiService {

  */
/*  @Headers({
            "Content-Type: text/xml",
            "SOAPAction: your_soap_action" // Provide the SOAP action from your WSDL
    })*//*

    @POST("http://localhost:8003/ePosServiceNE3_1/jdCommoneposServiceRes")
    Call<ResponseBody> postXmlData(@Body RequestBody xmlData);
}
*/
