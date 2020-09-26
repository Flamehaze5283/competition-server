package ysu.edu.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageUtil {

    private static final String SEND_URL = "https://api.netease.im/sms/sendcode.action";//发送短信请求的URL
    private static final String CHECK_URL = "https://api.netease.im/sms/verifycode.action";//验证短信请求的URL

    private static final String APP_KEY = "101d06a634e7ff2511f0c2af2590bbc4";//网易云分配的账号
    private static final String APP_SECRET = "da8e6ba44c86";//密码
    private static final String MOULD_ID="14892528";//模板ID
    private static final String NONCE = "4tgggergigwow323t23t";//随机数
    private static final String CODE_LEN = "4";//验证码长度

    public static String sendTelMessage(String phone) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = createPost(SEND_URL);

        //设置请求参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("templateid", MOULD_ID));
        nameValuePairs.add(new BasicNameValuePair("codeLen", CODE_LEN));
        nameValuePairs.add(new BasicNameValuePair("mobile", phone));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

        //执行请求
        HttpResponse response = httpclient.execute(post);
        String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");

        return responseEntity;
    }

    public static String sendTextMessage(String phone) throws Exception{

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = createPost(SEND_URL);

        //设置请求参数
        List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mobile", phone));
        nameValuePairs.add(new BasicNameValuePair("code", getVerifyCode()));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

        //执行请求
        HttpResponse response=httpclient.execute(post);
        String responseEntity= EntityUtils.toString(response.getEntity(),"utf-8");

        //判断是否发送成功
        return JSON.parseObject(responseEntity).getString("code");
    }

    private static HttpPost createPost(String url) {

        HttpPost post = new HttpPost(url);

        String curTime=String.valueOf((new Date().getTime()/1000L));
        String checkSum=CheckSumBuilder.getCheckSum(APP_SECRET,NONCE,curTime);

        //设置请求的header
        post.addHeader("AppKey",APP_KEY);
        post.addHeader("Nonce",NONCE);
        post.addHeader("CurTime",curTime);
        post.addHeader("CheckSum",checkSum);
        post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        return post;
    }

    public static String codeMessage(String code, String phone) {
        switch (code) {
            case "200": return "验证码已发到" + phone + "号码中，请查收";
            case "315": return "您绑定的手机号" + phone + "IP限制;";
            case "301": return "您绑定的手机号" + phone + "被封禁!";
            case "403": return "您绑定的手机号" + phone + "非法操作或没有权限!";
            case "404": return "您绑定的手机号" + phone + "对象不存在!";
            case "414": return "您绑定的手机号" + phone + "参数错误!";
            case "500": return "您绑定的手机号" + phone + "服务器内部错误!";
            case "408": return "您绑定的手机号" + phone + "客户端请求超时!";
            case "416": return "您绑定的手机号" + phone + "请求短信的次数过多!";
            case "419": return "您绑定的手机号" + phone + "数量超过上限!";
            default: return "未知的错误";
        }
    }

    public static String check(String tel, String code) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = createPost(CHECK_URL);

        //设置请求参数
        List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mobile",tel));
        nameValuePairs.add(new BasicNameValuePair("code",code));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

        //执行请求
        HttpResponse response=httpclient.execute(post);
        String responseEntity= EntityUtils.toString(response.getEntity(),"utf-8");

        //判断是否发送成功
        return JSON.parseObject(responseEntity).getString("code");
    }

    private static String getVerifyCode() {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < Integer.parseInt(CODE_LEN); i++)
            buffer.append(new Double(Math.random()*10).intValue());
        return buffer.toString();
    }
}
