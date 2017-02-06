package utilsTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/29.
 */
public class RegexTest {
    public static void main(String[] args) {
        String URL_LIST = "http://www\\.51ztzj\\.com/dbizhi/category\\w+.htm";
        String URL_POST = "https://www\\.umei\\.cc/[/\\w\\.-]*/\\d+.htm";

        String text_list = "http://www.51ztzj.com/dbizhi/category_25_1.htm";
        String text_post = "https://www.umei.cc/p/gaoqing/rihan/20150909184924.htm";
        int group = 0;

        Pattern regex = Pattern.compile(URL_LIST, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(text_list);
        boolean isFind = matcher.find();
        System.out.println(isFind);
        System.out.println(matcher.groupCount());
        if(isFind) {
            System.out.println(matcher.group(group));
        }

        regex = Pattern.compile(URL_POST, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        matcher = regex.matcher(text_post);
        isFind = matcher.find();
        System.out.println(isFind);
        System.out.println(matcher.groupCount());
        if(isFind) {
            System.out.println(matcher.group(group));
        }
    }
}
