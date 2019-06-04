package com.wudy.timer.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName HttpClientUtils
 * Description http post调用
 *
 * @Author wudy
 * @Date 2019/6/4 15:28
 * @Version 1.0
 **/
public class HttpClientUtils {

    public static CloseableHttpResponse postMap(String url, Map<String,String> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();
        CloseableHttpResponse response = null;
        for (Map.Entry<String,String> entry:params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
             response = client.execute(post);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static CloseableHttpResponse postJson(String url, Map<String,String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new ByteArrayEntity(JSON.toJSONBytes(params)));
            response = httpClient.execute(post);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                if(response != null)
                {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
