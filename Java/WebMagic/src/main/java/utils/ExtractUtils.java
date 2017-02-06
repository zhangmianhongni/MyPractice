package utils;

import constant.FieldSourceType;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Map;

/**
 * Created by mian on 2017/1/29.
 * 抽取帮助类
 */
public class ExtractUtils {
    public static String REQUEST_DEPTH_STR = "RequestDepth";
    public static String REQUEST_PARAMS_STR = "nameValuePair";
    public static String SOURCE_REQ_URL_STR = "sourceRequestUrl";

    public static String IMAGE_NAME_STR = "imageName";
    public static String IMAGE_SOURCE_STR = "imageSource";


    public static Selectable getSelectable(Page page, FieldSourceType fieldSourceType){
        Selectable extractContent = null;
        if(fieldSourceType == FieldSourceType.Html){
            extractContent = page.getHtml();
        }else if(fieldSourceType == FieldSourceType.Url){
            extractContent = page.getUrl();
        }else if(fieldSourceType == FieldSourceType.Json){
            extractContent = page.getJson();
        }

        return extractContent;
    }

    public static NameValuePair[] getExtraRequestParams(NameValuePair[] initParams, Map<String, String> extraValues, int requestDepth){
        NameValuePair[] extraParams = new NameValuePair[initParams.length];
        for (int i = 0; i < initParams.length; i++) {
            NameValuePair initParam = initParams[i];
            if(extraValues != null && extraValues.containsKey(initParam.getName())){
                String value = extraValues.get(initParam.getName());
                if(StringUtils.isNotEmpty(value) && StringUtils.isNumeric(initParam.getValue()) && StringUtils.isNumeric(value)){
                    Integer finalValue = Integer.parseInt(initParam.getValue()) + (Integer.parseInt(value) * requestDepth);
                    extraParams[i] = new BasicNameValuePair(initParam.getName(), finalValue.toString());
                }else{
                    extraParams[i] = new BasicNameValuePair(initParam.getName(), initParam.getValue());
                }
            }else{
                extraParams[i] = new BasicNameValuePair(initParam.getName(), initParam.getValue());
            }
        }

        return extraParams;
    }

    /**
     * 将map转换成url
     */
    public static String getUrlByParamsMap(String url, NameValuePair[] params) {
        if (params == null) {
            return url;
        }
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(url) && url.contains("?")){
            url = url.substring(0, url.indexOf("?"));
        }
        sb.append(url + "?");
        for (NameValuePair param : params) {
            sb.append(param.getName() + "=" + param.getValue());
            sb.append("&");
        }

        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }

        return s;
    }
}
