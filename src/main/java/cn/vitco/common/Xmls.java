package cn.vitco.common;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by Sterling on 2017/12/11.
 */
public class Xmls {
    /**
     * 将一个 Map 转换成 XML 类似:
     *
     * <pre>
     * &lt;xml&gt;
     * &lt;key1&gt;value1&lt;/key1&gt;
     * &lt;key2&gt;value2&lt;/key2&gt;
     * &lt;/xml&gt;
     * </pre>
     *
     * @param map
     *            Map
     * @return XML 字符串
     */
    public static String mapToXml(Map<String, Object> map, boolean isCDATA) {
        return mapToXml("xml", map, isCDATA);
    }

    @SuppressWarnings("rawtypes")
    private static String mapToXml(String root, Map map, boolean isCDATA){
        StringBuffer sb = new StringBuffer("<"+root+">");
        mapToXml(map, sb, isCDATA);
        sb.append("</"+root+">");
        return sb.toString();
    }

    @SuppressWarnings("rawtypes")
    private static void mapToXml(Map map, StringBuffer sb, boolean isCDATA) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext();) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value)
                value = "";
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXml(hm, sb, isCDATA);
                }
                sb.append("</" + key + ">");
            } else {
                if (value instanceof HashMap) {
                    sb.append("<" + key + ">");
                    mapToXml((HashMap) value, sb, isCDATA);
                    sb.append("</" + key + ">");
                } else {
                    if(isCDATA)
                        sb.append("<" + key + "><![CDATA[" + value + "]]></" + key + ">");
                    else
                        sb.append("<" + key + ">" + value + "</" + key + ">");
                }
            }
        }
    }


    /**
     * 根据xml消息体转化为Map
     * @param xml
     * @param rootElement
     * @return
     * @throws DocumentException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> xmlToMap(String xml, String rootElement) throws DocumentException {
        org.dom4j.Document doc = DocumentHelper.parseText(xml);
        Element body = doc.getRootElement();
        Map vo = __buildXmlBody2map(body);
        return vo;
    }

    /**
     *
     * 此方法描述的是：将InputStream中的xml转化为map
     *
     * @author: yangbiao
     * @version: 2017年4月5日 下午4:45:16
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> xmlToMap(InputStream inputStream)
            throws Exception {
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        Map map = __buildXmlBody2map(root);
        // 释放资源
        // inputStream.close();
        inputStream = null;
        return map;
    }

    /**
     * 在这里未考虑多层级的迭代问题
     * @param body
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Map __buildXmlBody2map(Element body) {
        Map vo = new HashMap();
        if (body != null) {
            List<Element> elements = body.elements();
            for (Element element : elements) {
                String key = element.getName();
                if (!Strings.isBlank(key)) {
                    String text = element.getText().trim();
                    vo.put(key, text);
                }
            }
        }
        return vo;
    }

}
