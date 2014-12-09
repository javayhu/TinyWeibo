package weibo4j.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * hujiawei 20141209
 * <p/>
 * 这里是Java版本的SinaWeibo SDK
 * 它需要config.properties文件中的配置信息
 */
public class WeiboConfig {
    public WeiboConfig() {
    }

    private static Properties props = new Properties();

//    client_ID = 146833241
//    client_SERCRET = a32c1f950c15ebadac9c001416b454f3
//            redirect_URI = http://www.sina.com
//    baseURL=https://api.weibo.com/2/
//    accessTokenURL=https://api.weibo.com/oauth2/access_token
//    authorizeURL=https://api.weibo.com/oauth2/authorize
//    rmURL=https://rm.api.weibo.com/2/


    //hujiawei 20141209 replace the file with some codes
    static {
        props = new Properties();
        props.setProperty("client_ID","146833241");
        props.setProperty("client_SERCRET","a32c1f950c15ebadac9c001416b454f3");
        props.setProperty("redirect_URI","http://www.sina.com");
        props.setProperty("baseURL","https://api.weibo.com/2/");
        props.setProperty("accessTokenURL","https://api.weibo.com/oauth2/access_token");
        props.setProperty("authorizeURL","https://api.weibo.com/oauth2/authorize");
        props.setProperty("rmURL","https://rm.api.weibo.com/2/");
//        try {
//            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public static void updateProperties(String key, String value) {
        props.setProperty(key, value);
    }
}
