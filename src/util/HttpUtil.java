package util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * <p>
 * HTTP工具类
 * </p>
 * 
 */
public class HttpUtil {

    public static String post(String url, String xmlParam, String charset){
        String resText = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(xmlParam, charset));
            CloseableHttpResponse response = httpClient.execute(post);
            resText = EntityUtils.toString(response.getEntity(), charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resText;
    }

}
