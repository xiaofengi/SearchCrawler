package org.crawler.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

public class TestUtil {

    public static void main(String[] args) throws Exception {
        String result = execCmd("java -version", null);
        System.out.println(result);
    }

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

}
