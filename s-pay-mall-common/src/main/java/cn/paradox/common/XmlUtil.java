package cn.paradox.common;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.Writer;

public class XmlUtil {

    /**
     * 扩展XStream功能,自动加上![CDATA[]]
     */
    public static  XStream getMyXStream() {
        return  new XStream(new DomDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    //对所有xml节点添加CDATA标记
                    boolean cdata = true;

                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                    }

                    protected  void writeText(QuickWriter writer, String text) {
                        if(cdata && !StringUtils.isNumeric(text)) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        }else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
    }

    /**
     * bean转成微信xml
     */
    public static String beanToXml(Object obj) {
        XStream xstream = getMyXStream();
        xstream.alias("xml", obj.getClass());
        xstream.processAnnotations(obj.getClass());
        String xml = xstream.toXML(obj);
        if(!StringUtils.isEmpty(xml)) {
            return xml;
        }else {
            return null;
        }
    }


    /**
     * xml转bean
     */
    public static <T> T xmlToBean(String xml, Class clazz) {
        String classPath = System.getProperty("java.class.path");

        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{clazz});
        xstream.processAnnotations(new Class[]{clazz});
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("xml",clazz);
        return (T) xstream.fromXML(xml);
    }
}
