package whm.brush.csdn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static whm.brush.csdn.JsoupGetArticleUrl.getUrl;


/**
 * Created by whm on 2017/8/31.
 */
public class JsoupGetIp {
    /**
     * 获取代理IP地址
     * @param url
     * @return
     */
    public static List<AgencyIp> getIp(String url) {
        List<AgencyIp> ipList = null;
        try {
            //1.向ip代理地址发起get请求，拿到代理的ip
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();

            //2,将得到的ip地址解析除字符串
            String ipStr = doc.body().text().trim().toString();

            ipList = new ArrayList<AgencyIp>();

            List<String> ips = new ArrayList<>();
            String lines[] = ipStr.split("\r\n");
            for(int i=0; i<lines.length;i++){
                //out.println(lines[i]+"<br />");
                //匹配到ip地址
                Pattern ipreg = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} \\d{4}");
                Matcher ip = ipreg.matcher(lines[i]);
                while(ip.find()){
                    System.out.println(ip.group().replace(" ",":"));
                    ips.add(ip.group().replace(" ",":"));
                }
            }
            //4.循环遍历得到的ip字符串，封装成AgencyIp的bean

            for(final String ip : ips) {
                System.out.println(ip);
                AgencyIp AgencyIp = new AgencyIp();
                String[] temp = ip.split(":");
                AgencyIp.setAddress(temp[0].trim());
                AgencyIp.setPort(temp[1].trim());
                ipList.add(AgencyIp);
            }
        } catch (IOException e) {
            System.out.println("加载文档出粗");
        }
        return ipList;
    }

    /**
     * 访问文章
     * @param blogUrl
     * @param ipList
     */
    public static void visit(String blogUrl,List<AgencyIp> ipList){
        int time = 100;
        int count = 0;

        for(int i = 0; i< time; i++) {
            //2.设置ip代理
            for(final AgencyIp AgencyIp : ipList) {
                System.setProperty("http.maxRedirects", "50");
                System.getProperties().setProperty("proxySet", "true");
                System.getProperties().setProperty("http.proxyHost", AgencyIp.getAddress());
                System.getProperties().setProperty("http.proxyPort", AgencyIp.getPort());

                try {
                    Document doc = Jsoup.connect(blogUrl)
                            .userAgent("Mozilla")
                            .cookie("auth", "token")
                            .timeout(1000)
                            .get();
                    if(doc != null) {
                        count++;
                        System.out.println("成功刷新次数: " + count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        //1.想http代理地址api发起请求，获得想要的代理ip地址
        String url = "http://www.xicidaili.com/nn/";
        List<AgencyIp> ipList = getIp(url);
        List<String> urls = getUrl();
        for (String u:urls){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("文章地址:" + u);
                    visit(u,ipList);
                }
            }).start();
        }
    }

}
