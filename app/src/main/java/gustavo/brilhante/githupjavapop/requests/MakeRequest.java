package gustavo.brilhante.githupjavapop.requests;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import gustavo.brilhante.githupjavapop.constants.Config;
import gustavo.brilhante.githupjavapop.models.Argument;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Gustavo on 10/04/17.
 */

public class MakeRequest {

    public static Response get(String url, String token) throws IOException {
        ArrayList<Argument> emptyParams = new ArrayList<>();
        return get(url, emptyParams, token);
    }
    public static Response get(String url, ArrayList<Argument> parameters, String respType) throws IOException {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.retryOnConnectionFailure(true);
        b.readTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        b.writeTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        OkHttpClient client = b.build();
        if(parameters.size()>0)url += url.endsWith("?") ? "" : "?";
        for(int i=0; i< parameters.size();i++){
            String chave = parameters.get(i).getKey();
            if (i == 0) {
                url = url + chave+"="+parameters.get(i).getValue();
            } else {
                url = url +"&"+chave+"="+parameters.get(i).getValue();
            }
        }
        //url = url.replaceAll("\n", "").replaceAll(" ","");
        //url = "http://www.omdbapi.com/?s=Batman&page=2";
        //if(respType!=null)url = url +"&"+respType;
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        return client.newCall(request).execute();
    }
    public static Response post(String url, HashMap<String,String> paramValor) throws IOException{
        return post(url,paramValor, null);
    }
    public static Response post(String url, HashMap<String,String> paramValor, String token) throws IOException {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        b.writeTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        OkHttpClient client = b.build();
        Iterator<String> it = paramValor.keySet().iterator();
        FormBody.Builder formBody = new FormBody.Builder();
        while (it.hasNext()) {
            String chave = it.next();
            formBody.add(chave,paramValor.get(chave));
        }
        RequestBody body = formBody.build();
        Request.Builder builder = new Request.Builder();
        if(token != null){
            builder.addHeader("Authorization","Bearer " + token);
        }
        Request request = builder
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Authorization","Bearer " + token)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static Response put(String url, Map<String,String> paramValor, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(paramValor);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer " + token)
                .url(url)
                .put(RequestBody.create(JSON, json))
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response jsonPost(String url, Map<String,String> paramValor, String token) throws IOException {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        b.writeTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        OkHttpClient client = b.build();
        Gson gson = new Gson();
        String json = gson.toJson(paramValor);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer " + token)
                .url(url)
                .post(RequestBody.create(JSON, json))
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response jsonPost(String url, Map<String,String> paramValor) throws IOException {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        b.writeTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        OkHttpClient client = b.build();
        Gson gson = new Gson();
        String json = gson.toJson(paramValor);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .url(url)
                .post(RequestBody.create(JSON, json))
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response jsonPost(String url, String paramValor, String token) throws IOException {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(60, TimeUnit.SECONDS);
        b.writeTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = b.build();
        Gson gson = new Gson();
        String json = gson.toJson(paramValor);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(body);
        builder.addHeader("content-type", "application/json");
        if(token != null)
            builder.addHeader("authorization", "Bearer " + token);
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response jsonStringPost(String url, String stringValue, String token) throws IOException {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        b.writeTimeout(Config.REQUEST_TIME_OUT, TimeUnit.SECONDS);
        OkHttpClient client = b.build();
        String json = stringValue;
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(body);
        builder.addHeader("content-type", "application/json");
        if(token != null)
            builder.addHeader("authorization", "Bearer " + token);
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        return response;
    }

}
