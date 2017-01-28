package utils;

import model.FieldSourceType;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Created by mian on 2017/1/29.
 * 抽取帮助类
 */
public class ExtractUtils {
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
}
