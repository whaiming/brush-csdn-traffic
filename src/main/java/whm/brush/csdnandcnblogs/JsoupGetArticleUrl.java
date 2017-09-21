package whm.brush.csdnandcnblogs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by whm on 2017/8/31.
 */
public class JsoupGetArticleUrl {
    /**
     * 获取所有个人博客地址
     * @return
     */
    public static List<String> getCsdnBlogsUrl(){
        List<String> urls = new ArrayList<String>();
        try {
            Document doc = getDoc("http://blog.csdn.net/w980994974");
            Element body = doc.body();
            Pattern compile = Pattern.compile("/w980994974/article/details/\\d{8}$");
            Elements es=body.select("a");
            /**
             * 用set去重
             */
            HashSet<String> set = new HashSet<String>();
            for (Iterator it = es.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                if (compile.matcher(e.attr("href")).find()){
                    set.add("http://blog.csdn.net" + e.attr("href"));
                }
            }
            urls.addAll(set);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    /**
     * 模拟浏览器行为的请求头获取Document
     * @param url
     * @return
     * @throws IOException
     */
    public static Document getDoc(String url) throws IOException {
        /**
         * 在爬之前最好看一下浏览器访问目标网站的Request Header信息，然后进行模仿
         */
        return Jsoup.connect(url)
//                .header("accept", "application/json, text/plain,*/*")
//                .header("Accept-Encoding", "gzip, deflate,br")
//                .header("Accept-Language", "zh-CN,zh;q=0.8")//,en-US;q=0.5,en;q=0.3
//                .header("Referer", "https://www.baidu.com/")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")//,en-US;q=0.5,en;q=0.3
                .header("Cache-Control","max-age=0")
                .header("Connection","keep-alive")
                .header("Host", "www.cnblogs.com")
                .header("Referer","http://www.cnblogs.com/WangHaiMing/")
                .header("Upgrade-Insecure-Requests","1")
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")// "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0
                .header("Cookie", "_ga=GA1.2.727269871.1498415016")
                .timeout(5000)
                .get();
    }
}
