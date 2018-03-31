package org.crawler.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadUtil {
	
	/**
     * 下载文件到本地
     *
     * @param urlString
     *          被下载的文件地址
     * @param filename
     *          本地文件名
     * @param timeout
     *          超时时间毫秒
     * @throws Exception
     *           各种异常
     */
    public static boolean download(String urlString, String filename,int timeout, String referer){
        boolean ret = false;
        File file = new File(filename);
        try {
            if(file.exists()){
                ret = true;
            }else{
                // 构造URL
                URL url = new URL(urlString);
                // 打开连接
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setConnectTimeout(timeout);
                con.setReadTimeout(timeout);
                if(referer != null) {
                	con.setRequestProperty("Referer", referer);
                }
                con.connect();
                int contentLength = con.getContentLength();
                // 输入流
                InputStream is = con.getInputStream();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流

                File file2=new File(file.getParent());
                file2.mkdirs();
                if(file.isDirectory()){

                }else{
                    file.createNewFile();//创建文件
                }
                OutputStream os = new FileOutputStream(file);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
                if(contentLength != file.length()){
                    file.delete();
                    ret = false;
                }else{
                    ret = true;
                }
            }
        } catch (IOException e) {
            file.delete();
            ret = false;
            System.out.println("[VideoUtil:download]:\n" + " VIDEO URL：" + urlString + " \n NEW FILENAME:" + filename + " DOWNLOAD FAILED!! ");
        }finally {
        }
        return ret;
    }

    /**
     * 断点续传
     * @param urlString
     * @param filename
     * @param timeout
     * @return
     */
    public static boolean resumeDownload(String urlString, String filename,int timeout) throws Exception{
        boolean ret = false;
        File fileFinal = new File(filename);
        String tmpFileName = filename+".tmp";
        File file = new File(tmpFileName);

        try {
            if(fileFinal.exists()){
                ret = true;
            }else{
                long contentStart = 0;
                File file2=new File(file.getParent());

                if(file.exists()){
                    contentStart = file.length();
                }else{
                    file2.mkdirs();
                }
                // 构造URL
                URL url = new URL(urlString);
                // 打开连接
                HttpURLConnection con = (HttpURLConnection )url.openConnection();
                con.setConnectTimeout(timeout);
                con.setReadTimeout(timeout);
                //设置续传的点
                if(contentStart>0){
                    con.setRequestProperty("RANGE","bytes="+contentStart+"-");
                }
                con.connect();
                int contentLength = con.getContentLength();
                // 输入流
                InputStream is = con.getInputStream();
                // 100Kb的数据缓冲
                byte[] bs = new byte[100*1024];
                // 读取到的数据长度
                int len;
                RandomAccessFile oSavedFile = new RandomAccessFile(tmpFileName,"rw");
                oSavedFile.seek(contentStart);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    oSavedFile.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                oSavedFile.close();
                is.close();
                file.renameTo(fileFinal);
                ret = true;
            }
        } catch (IOException e) {
            file.delete();
            ret = false;
            System.out.println("[VideoUtil:download]:\n" + " VIDEO URL：" + urlString + " \n NEW FILENAME:" + filename + " DOWNLOAD FAILED!! ");
            throw new Exception(e);
        }finally {
        }
        return ret;
    }
    
    public static void main(String[] args) {
    	/*boolean success = download("http://113.215.224.108/vbd48002.baomihua.com/38770aaf01b4b984ca0cb3fc59627caa/5AB3D370/3671/36705432_9_02e16d28d40111e6a94b003048cc631e.mp4?wsrid_tag=5ab3d345_PSzjhsgdiy115_24461-11937&wsiphost=local",
				"E:\\Downloads\\crawler\\baidu\\flashFile.mp4", 3600, 
				"http://baishi.baidu.com/watch/06174005201658404849.html?page=videoMultiNeed");
    	if(success) {
			System.out.println("成功");
		}else {
			System.out.println("失败");
		}*/
    	long s = System.currentTimeMillis();
        Pattern pattern = Pattern.compile("(?<=url_encoded_fmt_stream_map\":\").*(?=\",)");
        Matcher matcher = pattern.matcher(",\"gpt_migration\":\"1\",\"url_encoded_fmt_stream_map\":\"type=video%2Fwebm%3B+codecs%3D%22vp8.0%2C+vorbis%22\\u0026itag=43\\u0026url=https%3A%2F%2Fr6---sn-ab5l6nsr.googlevideo.com%2Fvideoplayback%3Finitcwndbps%3D892500%26gir%3Dyes%26ratebypass%3Dyes%26pl%3D24%26source%3Dyoutube%26c%3DWEB%26mime%3Dvideo%252Fwebm%26fvip%3D3%26ei%3DX1u_WtmmFonp8gTL0KXoDA%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26key%3Dyt6%26ip%3D207.246.90.158%26dur%3D0.000%26lmt%3D1483548348918084%26id%3Do-ACeaAuPjgn1zZ6wL-IkdVKKRDJ-Fu7htiw3t3onS7wGR%26itag%3D43%26signature%3D49588ADCED6AD67748A443F4AC0CFFAF96401C00.705D6EA2A70031317F388B35E323994218D99438%26requiressl%3Dyes%26ipbits%3D0%26mn%3Dsn-ab5l6nsr%252Csn-tt1e7n7e%26mm%3D31%252C26%26ms%3Dau%252Conr%26clen%3D298603905%26mv%3Dm%26mt%3D1522490087%26expire%3D1522511807\\u0026quality=medium,type=video%2Fmp4%3B+codecs%3D%22avc1.42001E%2C+mp4a.40.2%22\\u0026itag=18\\u0026url=https%3A%2F%2Fr6---sn-ab5l6nsr.googlevideo.com%2Fvideoplayback%3Finitcwndbps%3D892500%26gir%3Dyes%26ratebypass%3Dyes%26pl%3D24%26source%3Dyoutube%26c%3DWEB%26mime%3Dvideo%252Fmp4%26fvip%3D3%26ei%3DX1u_WtmmFonp8gTL0KXoDA%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26key%3Dyt6%26ip%3D207.246.90.158%26dur%3D3010.467%26lmt%3D1483703107630500%26id%3Do-ACeaAuPjgn1zZ6wL-IkdVKKRDJ-Fu7htiw3t3onS7wGR%26itag%3D18%26signature%3D5DE6F41AB76EE544FBB46AC9421BB7FAD87140A8.7E8CF1060FC98D7B3C2BB99DE54F9D0EE0859E9A%26requiressl%3Dyes%26ipbits%3D0%26mn%3Dsn-ab5l6nsr%252Csn-tt1e7n7e%26mm%3D31%252C26%26ms%3Dau%252Conr%26clen%3D217258129%26mv%3Dm%26mt%3D1522490087%26expire%3D1522511807\\u0026quality=medium,type=video%2F3gpp%3B+codecs%3D%22mp4v.20.3%2C+mp4a.40.2%22\\u0026itag=36\\u0026url=https%3A%2F%2Fr6---sn-ab5l6nsr.googlevideo.com%2Fvideoplayback%3Finitcwndbps%3D892500%26gir%3Dyes%26pl%3D24%26source%3Dyoutube%26c%3DWEB%26mime%3Dvideo%252F3gpp%26fvip%3D3%26ei%3DX1u_WtmmFonp8gTL0KXoDA%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26key%3Dyt6%26ip%3D207.246.90.158%26dur%3D3010.513%26lmt%3D1483542898125022%26id%3Do-ACeaAuPjgn1zZ6wL-IkdVKKRDJ-Fu7htiw3t3onS7wGR%26itag%3D36%26signature%3D48BBE3DCB5BF7E45B63A3E8994BD58D9126031E2.29FDB4B4157DF7E0E6427D122F2AFF3CA138D815%26requiressl%3Dyes%26ipbits%3D0%26mn%3Dsn-ab5l6nsr%252Csn-tt1e7n7e%26mm%3D31%252C26%26ms%3Dau%252Conr%26clen%3D83950186%26mv%3Dm%26mt%3D1522490087%26expire%3D1522511807\\u0026quality=small,type=video%2F3gpp%3B+codecs%3D%22mp4v.20.3%2C+mp4a.40.2%22\\u0026itag=17\\u0026url=https%3A%2F%2Fr6---sn-ab5l6nsr.googlevideo.com%2Fvideoplayback%3Finitcwndbps%3D892500%26gir%3Dyes%26pl%3D24%26source%3Dyoutube%26c%3DWEB%26mime%3Dvideo%252F3gpp%26fvip%3D3%26ei%3DX1u_WtmmFonp8gTL0KXoDA%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26key%3Dyt6%26ip%3D207.246.90.158%26dur%3D3010.513%26lmt%3D1483542891941235%26id%3Do-ACeaAuPjgn1zZ6wL-IkdVKKRDJ-Fu7htiw3t3onS7wGR%26itag%3D17%26signature%3D6CDDAA6BB6789CC57A076C109BB48D80DE3C792F.CCAFB0A931F5E123642D4166FB23E3A4EAAE39AA%26requiressl%3Dyes%26ipbits%3D0%26mn%3Dsn-ab5l6nsr%252Csn-tt1e7n7e%26mm%3D31%252C26%26ms%3Dau%252Conr%26clen%3D30369809%26mv%3Dm%26mt%3D1522490087%26expire%3D1522511807\\u0026quality=small\",\"dclk\":true,\"author\":\"CCTV财");
        if(matcher.find()){
            String[] videoUrlInfos =  matcher.group().split("type=video%2F");
            for(int i=1; i<videoUrlInfos.length; i++){
                String videoUrlInfo = videoUrlInfos[i];
                String fileFormat = videoUrlInfo.substring(0, videoUrlInfo.indexOf("%3B"));
                Matcher matcher1 = Pattern.compile("(?<=url=).*(,)?").matcher(videoUrlInfo);
                if(matcher1.find()) {
                    String url = matcher1.group();
                    System.out.println(url);
                    //String fileName =
                    //DownloadUtil.download(url, )
                }
            }
        }

        System.out.println(System.currentTimeMillis()-s);
    }

}
