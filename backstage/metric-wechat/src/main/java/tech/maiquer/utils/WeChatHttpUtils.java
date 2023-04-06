package tech.maiquer.utils;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author: pixel-revolve
 * @date: 2022/2/25 19:56
 */
public class WeChatHttpUtils {

    public static CloseableHttpClient getClient(){
        HttpClientBuilder builder=HttpClientBuilder.create();
        return builder.build();

    }

}
