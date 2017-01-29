package UtilsTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/29.
 */
public class RegexTest {
    public static void main(String[] args) {
        String URL_LIST = "http://baozoumanhua\\.com/text";
        String URL_POST = "http://baozoumanhua\\.com/articles/\\d+";

        String text_list = "http://baozoumanhua.com/text";
        String text_post = "http://baozoumanhua.com/articles/39830319";
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
