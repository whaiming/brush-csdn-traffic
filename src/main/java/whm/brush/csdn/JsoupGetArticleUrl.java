package whm.brush.csdn;

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
    public static List<String> getUrl(){
        List<String> urls = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("http://blog.csdn.net/w980994974")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();
            Element body = doc.body();
            Pattern compile = Pattern.compile("/w980994974/article/details/\\d{8}$");
            Elements es=body.select("a");
            /**
             * 用set去重
             */
            HashSet<String> set = new HashSet<>();
            for (Iterator it = es.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                if (compile.matcher(e.attr("href")).find()){
                    set.add("http://blog.csdn.net" + e.attr("href"));
                }
            }
            for( Iterator   it = set.iterator();  it.hasNext(); ){
                System.out.println("value="+it.next().toString());
            }
            urls.addAll(set);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }
}
