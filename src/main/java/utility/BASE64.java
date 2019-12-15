package utility;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * BASE64特点：
 * 1)BASE64的加密解密是双向的，可以求反解。
 * 2)BASE64Encoder和BASE64Decoder是非官方JDK实现类。虽然可以在JDK里能找到并使用，但是在API里查不到。
 * 3）BASE64严格地说，属于编码格式，而非加密算法
 * 4）BASE加密后产生的字节位数是8的倍数，如果不够位数以=符号填充。
 * BASE64按照RFC2045的定义，被定义为：Base64内容传送编码被设计用来把任意序列的8位字节描述为一种不易被人直接识别的形式。
 * 常见于邮件、http加密，截取http信息，你就会发现登录操作的用户名、密码字段通过BASE64加密的。
 */
public class BASE64 {

    private  String message;
    /**
     * BASE64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) throws Exception{
        return new String((new BASE64Decoder()).decodeBuffer(key));
    }

    /**
     * BASE64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key)throws Exception{
        return (new BASE64Encoder()).encodeBuffer(key.getBytes());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws Exception{
        this.message = BASE64.decryptBASE64(message);
    }

    public static void main(String[] args) {
        String str = "root";
        try {
            String result1 = BASE64.encryptBASE64(str);
            System.out.println("result1=====加密数据=====>> "+result1);

            String str2 = BASE64.decryptBASE64(result1);
            System.out.println("str2=====解密数据=====>> "+str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}