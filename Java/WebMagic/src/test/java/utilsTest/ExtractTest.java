package utilsTest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import utils.ExtractUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mian on 2017/1/27.
 * ExtractTest
 */
public class ExtractTest {
    public static void main(String[] args) {
        getUrlByParamsMapTest();
    }

    private static void extraRequestParamsTest(){
        NameValuePair[] nameValuePairs = {
                new BasicNameValuePair("key1", "value1"),
                new BasicNameValuePair("page", "0"),
                new BasicNameValuePair("key3", "value3"),
        };

        Map<String, String> extraValues = new HashMap<>();
        extraValues.put("page", "1");

        for (int i = 1; i <= 10; i++) {
            System.out.println(Arrays.toString(ExtractUtils.getExtraRequestParams(nameValuePairs, extraValues, i)));
        }
    }

    private static void getUrlByParamsMapTest(){
        String url = "http://baozoumanhua.com/text/fresh";

        NameValuePair[] nameValuePairs = {
                new BasicNameValuePair("page", "1"),
                new BasicNameValuePair("sv", "1486094701"),
        };

        System.out.println(ExtractUtils.getUrlByParamsMap(url, nameValuePairs));
    }
}
