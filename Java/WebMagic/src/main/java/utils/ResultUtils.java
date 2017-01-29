package utils;

import model.ExpressionType;
import model.ExtractField;
import model.FieldSourceType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/27.
 * 结果处理工具类
 */
public class ResultUtils {
    private static Logger logger = LoggerFactory.getLogger(ResultUtils.class);

    public static String IS_MULTI_ITEMS_STR = "isMultiItems";
    public static String MULTI_ITEMS_STR = "multiItems";
    public static String URL_STR = "url";

    public static List<Map<String, Object>> splitMapList(Map<String, Object> mapList){
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            if (mapList.values().iterator().next() instanceof ArrayList) {
                int arrLength = ((ArrayList) mapList.values().iterator().next()).size();
                Object[] keys = mapList.keySet().toArray();

                for (int i = 0; i < arrLength; i++) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (Object key : keys) {
                        ArrayList list = (ArrayList) mapList.get(key);
                        map.put(key.toString(), list.get(i));
                    }

                    results.add(map);
                }
            }
        }catch (Exception e){
            logger.warn("分割列表结果集失败，value长度不一致！", e);
        }

        return results;
    }

    public static List<Map<String, Object>> parseResultItems(ResultItems resultItems){
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            if(resultItems.getAll().containsKey(IS_MULTI_ITEMS_STR) && (Boolean)resultItems.get(IS_MULTI_ITEMS_STR)){
                results = resultItems.get(MULTI_ITEMS_STR);
            }else{
                results.add(resultItems.getAll());
            }
        }catch (Exception e){
            logger.warn("结果集转换失败失败！", e);
        }
        return results;
    }

    public static void extractDetailFields(Page page, List<ExtractField> extractFields){
        if(extractFields != null){
            extractFields.stream().filter(field -> field != null).forEach(field -> {
                String fieldName = field.getFieldName();
                ExpressionType expressionType = field.getExpressionType();
                FieldSourceType fieldSourceType = field.getFieldSourceType();
                Selector selector = field.getSelector();

                Selectable extractContent = ExtractUtils.getSelectable(page, fieldSourceType);

                if (extractContent != null && selector != null) {
                    if (field.isMulti()) {
                        List<String> results = page.getHtml().selectDocumentForList(selector);
                        if (field.isNeed() && results.size() == 0) {
                            //如果是必须字段，字段内容为空的时候跳过这页面
                            page.setSkip(true);
                        } else {
                            page.putField(fieldName, results);
                        }
                    } else {
                        if (expressionType != ExpressionType.JsonPath) {
                            String result = extractContent.select(selector).toString();
                            if (field.isNeed() && result == null) {
                                //如果是必须字段，字段内容为空的时候跳过这页面
                                page.setSkip(true);
                            } else {
                                page.putField(fieldName, result);
                            }
                        } else {
                            String expressionValue = field.getExpressionValue();
                            if (StringUtils.isNotEmpty(expressionValue)) {
                                String result = extractContent.jsonPath(expressionValue).get();
                                if (field.isNeed() && result == null) {
                                    //如果是必须字段，字段内容为空的时候跳过这页面
                                    page.setSkip(true);
                                } else {
                                    page.putField(fieldName, result);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
