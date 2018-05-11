package org.crawler.util;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.Random;

public class TestUtil {

/*    public static void main(String[] args) throws Exception {
        String result = execCmd("java -version", null);
        System.out.println(result);
    }*/

    /**
     * 执行系统命令, 返回执行结果
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufIn = null;
        BufferedReader bufError = null;
        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
            bufError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // 读取输出
            String line = null;
            while ((line = bufIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufError.readLine()) != null) {
                result.append(line).append('\n');
            }
        } finally {
            closeStream(bufIn);
            closeStream(bufError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }
        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

/*    public static void main(String[] args) throws Exception {
        System.out.println("To enable your free eval account and get "
                +"CUSTOMER, YOURZONE and YOURPASS, please contact "
                +"sales@luminati.io");
        HttpHost proxy = new HttpHost("zproxy.lum-superproxy.io", 22225);
        String res = Executor.newInstance()
                .auth(proxy, "lum-customer-mobduos-zone-residential-country-br", "978a9804b213")
                .execute(Request.Get("https://www.google.com").viaProxy(proxy))
                .returnContent().asString();
        System.out.println(res);
    }*/

    public static void main(String[] args) throws IOException {
        System.out.println("To enable your free eval account and get "
                +"CUSTOMER, YOURZONE and YOURPASS, please contact "
                +"sales@luminati.io");
        System.out.println("Performing request(s)");
        Client client = new Client("ie");
        try {
            // Put complete scraping sequence below:
            System.out.println(client.request("https://www.boxun.com"));
            // System.out.println(client.request(...second request...));
        } finally {
            client.close();
        }
    }
}

class Client {

    public static final String username = "lum-customer-mobduos-zone-residential";
    public static final String password = "978a9804b213";
    public static final String hostname = "zproxy.lum-superproxy.io";
    public static final int port = 22225;
    public String session_id = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
    public CloseableHttpClient client;

    public Client(String country) {
        String login = username+(country!=null ? "-country-"+country : "")
                        +"-session-" + session_id;
        HttpHost super_proxy = new HttpHost(hostname, port);
        CredentialsProvider cred_provider = new BasicCredentialsProvider();
        cred_provider.setCredentials(new AuthScope(super_proxy), new UsernamePasswordCredentials(login, password));
        client = HttpClients.custom()
                            .setConnectionManager(new BasicHttpClientConnectionManager())
                            .setProxy(super_proxy)
                            .setDefaultCredentialsProvider(cred_provider)
                            .build();
    }

    public String request(String url) throws IOException {
        HttpGet request = new HttpGet(url);
//        RequestConfig config = RequestConfig.custom()
//                                .setConnectTimeout(10000)
//                                .setRedirectsEnabled(true)
//                                .build();
//        request.setConfig(config);
//        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        //request.setHeader("cookie", "NID=129=SN7HIldBxowCViGYazYwgbxcdPUtSmhzlJKh5iAHaHzNImofQ77aj2ptSIgKLrOfwJM2Jy85LUxG_z-7XsSJrMijlmpMWdtm-B2VdtpvbAmgDVm2zp1EOwBEkYq9dh6TCW0SqE3xCNJFM8PjbKCRpD_8herfn515WIdM1rZDPv885PNNy78sgDt0WIqQzyTXa9t32NAcHtfSK2srwP-OQzuXBqNzGZ_g0Wv5P0uJ-CHFfRA_xDjrAO3yV-FYI583oL8; DV=g9BfJ1BW3qIvoPugrFVU4MYNaq3YMxZ24gV5ztaau6lfAAA; 1P_JAR=2018-05-08-12");
        //request.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //request.setHeader("accept-encoding", "utf-8");
        //request.setHeader("accept-language", "zh-CN,zh;q=0.9");
        //request.setHeader("upgrade-insecure-requests", "1");
        //request.setHeader("x-client-data", "CJa2yQEIprbJAQjBtskBCKmdygEIsp3KAQioo8oBGJKjygE=");
        CloseableHttpResponse response = client.execute(request);
        try {
            return EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
    }

    public void close() throws IOException {
        client.close();
    }
}