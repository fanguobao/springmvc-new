package tools;


import utility.SysConst;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Vector;


public class StrTool {
    public static final String EQUAL = "=";

    public static final String GREATER = ">";

    public static final String GREATGE_EQUAL = ">=";

    public static final String LESS = "<";

    public static final String LESS_EQUAL = "<=";

    public static final String NOT_EQUAL = "!=";

    public static final String CONTAIN = "*";

    public static final String BETWEEN = ":";

    public static final String DATEDELIMITER = "/";

    public static final String VISADATEDELIMITER = "-";

    public static final String TIMEDELIMITER = ":";

    public static final String ADDRESSDELIMITER = "$$";

    public static final String DELIMITER = "^";

    public static final String PACKAGESPILTER = "|";

    public static final String OR = "~!";

    public static final int LENGTH_OR = 2;

    public static final String BETWEEN_AND = ":";

    public static final String BLANK = "?";

    public static final String DIRECTMODE = "Direct Mode!" + DELIMITER + DELIMITER;
    /**
     * 判断字符串是否为空
     * @param str
     * @return boolean
     */
    public static boolean isEmpty(String str){
        return (str == null || "".equals(str));
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return boolean
     */
    public static boolean isNull(String str){
        return (str == null);
    }

    /**
     * 判断字符串是否为字符串
     * @param str
     * @return boolean
     */
    public static boolean isEmptyStr(String str){
        return (str!= null&& "".equals(str));
    }

    /**
     * 将字符串按照指定的分隔字符进行拆分,返回从指定序号的分隔符到前一个分隔符之间的字符串
     * @param strMain String 主字符串
     * @param strDelimiters String 分隔符
     * @param intSerialNo int 分隔符序号
     * @return String 指定序号的分隔符到前一个分隔符之间的字符串,如果没有找到则返回"" 例如：值赋串类似于 值1|值2|值3|值4|
     * 则intSerialNo=0 return "" intSerialNo=1 return "值1" intSerialNo=5 return ""
     */
    public static String decodeStr(String strMain, String strDelimiters, int intSerialNo)
    {
        int intIndex = 0; /* 分隔符出现在主字符串中的起始位置 */
        int intCount = 0; /* 在扫描主字符串的过程中,第几次遇到分隔符字符串 */
        String strReturn = ""; /* 作为返回值的字符串 */

        if (strMain.length() < strDelimiters.length())
        {
            return ""; /* 若主字符串比分隔符串还要短的话,则返回空字符串 */
        }

        intIndex = strMain.indexOf(strDelimiters);
        if (intIndex == -1)
        {
            return ""; /* 若主字符串中不存在分隔符,则返回空字符串 */
        }

        while (intIndex != -1) /* 未找到分隔符时退出循环,并返回空字符串 */
        {
            strReturn = strMain.substring(0, intIndex);
            intCount++;
            if (intCount == intSerialNo)
            {
                if (intIndex == 0)
                {
                    return "";
                }
                else
                {
                    return strReturn.trim();
                }
            }
            strMain = strMain.substring(intIndex + 1);
            intIndex = strMain.indexOf(strDelimiters);
        };
        return "";
    }

    /**
     * 获取子串在主串中出现第 n 次的位置
     * @param strMain String 主字符串
     * @param strSub String 子字符串
     * @param intTimes int 出现次数
     * @return int 位置值,如果子串在主串中没有出现指定次数,则返回-1
     * abc2347adfas     2347
     */
    public static int getPos(String strMain, String strSub, int intTimes)
    {
        int intCounter = 0; // 循环记数
        int intPosition = 0; // 位置记录
        int intLength = strSub.length(); // 子串长度

        if (intTimes <= 0)
        {
            return -1;
        }
        while (intCounter < intTimes)
        {
            intPosition = strMain.indexOf(strSub, intPosition);
            if (intPosition == -1)
            {
                return -1;
            }
            intCounter++;
            intPosition += intLength;
        }
        return intPosition - intLength;
    }

    /**
     * 获取从指定位置开始子串在主串中出现第 n 次的位置
     * @param strMain String 主字符串
     * @param strSub String 子字符串
     * @param intStartIndex int 起始位置
     * @param intTimes int 出现次数
     * @return int 位置值,如果从起始位置起子串在主串中没有出现指定次数,则返回-1
     */
    public static int getPos(String strMain, String strSub, int intStartIndex, int intTimes)
    {
        if (strMain.length() - 1 < intStartIndex)
        {
            return -1;
        }
        int intPosition = getPos(strMain.substring(intStartIndex), strSub, intTimes);
        if (intPosition != -1)
        {
            intPosition += intStartIndex;
        }
        return intPosition;
    }

    /**
     * 转换字符串（主要是SQL语句的条件串）
     * @param strMessage String 待转换的字符串 (形如:<I>字段名^操作符^字段值^</I>) 新格式为：{[(]字段名
     * 操作符 字段值 [)]连接符号}版本格式串^
     * @return String 返回字符串(SQL语句中的 WHERE 条件子句,但不包括 'where'关键字)
     */
    public static String makeCondition(String strMessage)
    {
        String strSegment = "";
        String strField = "";
        String strOperator = "";
        String strValue = "";
        String strRemain = "";
        String strReturn = "1=1"; // 恒真条件
        int intPosition = 0;

        if (strMessage.indexOf(StrTool.DELIMITER) < 0)
        {
            return strMessage;
        }
        strRemain = strMessage;

        if (!strRemain.endsWith(StrTool.DIRECTMODE + StrTool.DELIMITER))
        {
            do
            {
                intPosition = getPos(strRemain, StrTool.DELIMITER, 3);
                if (intPosition < 0) // 分解完毕
                {
                    return strReturn;
                }
                strSegment = strRemain.substring(0, intPosition + 1).trim();
                strRemain = strRemain.substring(intPosition + 1);
                if (strSegment.length() < 1) // 分段结束
                {
                    break;
                }
                strField = decodeStr(strSegment, StrTool.DELIMITER, 1);
                strOperator = decodeStr(strSegment, StrTool.DELIMITER, 2);
                strValue = decodeStr(strSegment, StrTool.DELIMITER, 3);
                if (strValue.equals(BLANK))
                {
                    strValue = " ";
                }
                strReturn = strReturn + " AND " + "(";
                // 判断操作符
                if (strOperator.equals(BETWEEN))
                {
                    intPosition = strValue.indexOf(BETWEEN_AND);
                    strReturn = strReturn + strField + " BETWEEN '" + strValue.substring(0, intPosition).trim() + "'"
                            + " AND  '" + strValue.substring(intPosition + 1).trim() + "' ";
                }
                else if (strOperator.equals(CONTAIN))
                {
                    strReturn = strReturn + strField + " matches '" + strValue + "*' ";
                }
                else
                {
                    strSegment = "";
                    while (true)
                    {
                        intPosition = strValue.indexOf(OR);
                        if (intPosition < 0)
                        {
                            if (strSegment.equals(""))
                            {
                                strReturn = strReturn + strField + " " + strOperator + " '" + strValue + "' ";
                            }
                            else
                            {
                                strReturn = strReturn + strSegment + " OR " + strField + " " + strOperator + " '"
                                        + strValue.trim() + "' ";
                            }
                            break;
                        }
                        if (!strSegment.equals(""))
                        {
                            strSegment += " OR ";
                        }

                        strSegment = strSegment + strField + " " + strOperator + " '"
                                + strValue.substring(0, intPosition).trim() + "' ";
                        strValue = strValue.substring(intPosition + LENGTH_OR);
                    }
                }
                strReturn += ") ";
            }
            while (true);
        }
        else
        {
            strRemain = strRemain.substring(0, strRemain.length() - StrTool.DIRECTMODE.length()
                    - StrTool.DELIMITER.length());
            if (strRemain.trim().equals(""))
            {
                strRemain = "1=1";
            }
            strReturn = strRemain;
        }
        return strReturn;
    }

    /**
     * 将字符串转换为GBK字符串
     * @param strOriginal String 原串
     * @return String 将原串由ISO8859_1(Unicode)编码转换为GBK编码
     */
    public static String unicodeToGBK(String strOriginal)
    {
        if (strOriginal != null)
        {
            try
            {
                if (!isGBKString(strOriginal))
                {
                    return new String(strOriginal.getBytes("ISO8859_1"), "GBK");
                }
                else
                {
                    return strOriginal;
                }

            }
            catch (Exception exception)
            {
                System.out.println(exception.getMessage());
                return strOriginal;
            }
        }
        else
        {
            return "";
        }
    }

    /**
     * 将字符串转换为Unicode字符串
     * @param strOriginal String 原串
     * @return String 将原串由GBK编码转换为ISO8859_1(Unicode)编码
     */
    public static String GBKToUnicode(String strOriginal)
    {
        if (!SysConst.CHANGECHARSET)
        {
            return unicodeToGBK(strOriginal);
        }
        if (strOriginal != null)
        {
            try
            {
                if (isGBKString(strOriginal))
                {
                    return new String(strOriginal.getBytes("GBK"), "ISO8859_1");
                }
                else
                {
                    return strOriginal;
                }
            }
            catch (Exception exception)
            {
                return strOriginal;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * 将字符串转换为Unicode字符串
     * @param strOriginal String 原串
     * @param realConvert boolean 是否确认转换
     * @return String 将原串由GBK编码转换为ISO8859_1(Unicode)编码
     */
    public static String GBKToUnicode(String strOriginal, boolean realConvert)
    {
        if (!realConvert)
        {
            return unicodeToGBK(strOriginal);
        }
        if (strOriginal != null)
        {
            try
            {
                if (isGBKString(strOriginal))
                {
                    return new String(strOriginal.getBytes("GBK"), "ISO8859_1");
                }
                else
                {
                    return strOriginal;
                }
            }
            catch (Exception exception)
            {
                return strOriginal;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * 判断是否是GBK编码
     * @param tStr String
     * @return boolean
     */
    public static boolean isGBKString(String tStr)
    {
        int tlength = tStr.length();
        int t1 = 0;
        for (int i = 0; i < tlength; i++)
        {
            t1 = Integer.parseInt(Integer.toOctalString(tStr.charAt(i)));
            if (t1 > 511)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Unicode编码
     * @param tStr String
     * @return boolean
     */
    public static boolean isUnicodeString(String tStr)
    {
        int tlength = tStr.length();
        int t1 = 0;
        for (int i = 0; i < tlength; i++)
        {
            t1 = Integer.parseInt(Integer.toOctalString(tStr.charAt(i)));
            if (t1 > 511)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 使用指定类中的decode()方法解包给定字符串
     * @param strMessage String 字符串
     * @param intCount int 解包次数
     * @param cl Class 包含decode()方法的类,并且此类中含有FIELDNUM属性
     * @return Vector 将每个解包数据生成对应的类实例,并将这些实例作为返回Vector的元素
     * @throws Exception 如果查找decode方法出错、或者查找FIELDNUM字段出错、或者解包出错，方法抛出异常
     */
    public static Vector stringToVector(String strMessage, int intCount, Class cl) throws Exception
    {
        int intFieldNum = 0;
        Object object = null;
        Vector vec = new Vector();
        int intPosition = 0;
        Class[] parameters =
                { String.class };
        Method method = null;
        Field field = null;
        String[] therecord =
                { new String() };
        try
        {
            object = cl.newInstance();
            method = cl.getMethod("decode", parameters);
            field = cl.getField("FIELDNUM");
            intFieldNum = field.getInt(object);

            for (int i = 0; i < intCount; i++)
            {
                object = cl.newInstance();
                intPosition = StrTool.getPos(strMessage, StrTool.PACKAGESPILTER, intFieldNum);

                if (intPosition == strMessage.length() - 1)
                {
                    therecord[0] = strMessage;
                    method.invoke(object, therecord);
                    vec.addElement(object);
                    break;
                }
                else
                {
                    therecord[0] = strMessage.substring(0, intPosition + 1);

                    method.invoke(object, therecord);
                    vec.addElement(object);
                    strMessage = strMessage.substring(intPosition + 1);
                }
            }
        }
        catch (Exception exception)
        {
            throw exception;
        }
        finally
        {}
        return vec;
    }

    /**
     * 字符串替换函数
     * @param strMain String 原串
     * @param strFind String 查找字符串
     * @param strReplaceWith String 替换字符串
     * @return String 替换后的字符串，如果原串为空或者为""，则返回""
     */
    public static String replace(String strMain, String strFind, String strReplaceWith)
    {
        String strReturn = "";
        int intStartIndex = 0, intEndIndex = 0;

        if (strMain == null || strMain.equals(""))
        {
            return "";
        }

        while ((intEndIndex = strMain.indexOf(strFind, intStartIndex)) > -1)
        {
            strReturn = strReturn + strMain.substring(intStartIndex, intEndIndex) + strReplaceWith;
            intStartIndex = intEndIndex + strFind.length();
        }
        strReturn += strMain.substring(intStartIndex, strMain.length());
        return strReturn;
    }


    /**
     * 字符串转换成HTML格式
     * @param strInValue String 传入字符串
     * @return String 转换后返回
     */
    public static String toHTMLFormat(String strInValue)
    {
        String strOutValue = "";
        char c;

        for (int i = 0; i < strInValue.length(); i++)
        {
            c = strInValue.charAt(i);
            switch (c)
            {
                case '<':
                    strOutValue += "&lt;";
                    break;
                case '>':
                    strOutValue += "&gt;";
                    break;
                case '\n':
                    strOutValue += "<br>";
                    break;
                case '\r':
                    break;
                case ' ':
                    strOutValue += "&nbsp;";
                    break;
                default:
                    strOutValue += c;
                    break;
            }
        }
        return strOutValue;
    }

    /**
     * 字符串打包
     * @param strInValue String
     * @return String
     */
    public static String encode(String strInValue)
    {
        String strOutValue = "";
        char c;

        for (int i = 0; i < strInValue.length(); i++)
        {
            c = strInValue.charAt(i);
            switch (c)
            {
                case ':': // hardcode 同Common.js中 NAMEVALUEDELIMITER
                    // //域名与域值的分隔符
                    strOutValue += "："; // hardcode
                    break;
                case '|': // hardcode 同Common.js中 FIELDDELIMITER //域之间的分隔符
                    strOutValue += "┃";
                    break;
                case '\n':
                    strOutValue += "\\n";
                    break;
                case '\r':
                    strOutValue += "\\r";
                    break;
                case '\"':
                    strOutValue += "\\\"";
                    break;
                case '\'':
                    strOutValue += "\\\'";
                    break;
                case '\b':
                    strOutValue += "\\b";
                    break;
                case '\t':
                    strOutValue += "\\t";
                    break;
                case '\f':
                    strOutValue += "\\f";
                    break;
                case '\\':
                    strOutValue += "\\\\";
                    break;
                case '<':
                    strOutValue += "\\<";
                    break;
                case '>':
                    strOutValue += "\\>";
                    break;
                default:
                    strOutValue += c;
                    break;
            }
        }
        return strOutValue;
    }

    /**
     * 将二维数组进行数据打包
     * @param arr String[][] 存储数据的二维数组
     * @return String 按照编码规则将数组转换后得到的字符串
     */
    public static String encode(String arr[][])
    {
        System.out.println("使用StrTool下的encode方法打包");
        String strReturn = "";
        int rowcount = arr.length; // 取得数组的行数
        int colcount = arr[0].length; // 取得数组的列数
        int eleCount = rowcount * colcount;

        if (eleCount != 0)
        {
            strReturn = "0" + SysConst.PACKAGESPILTER + String.valueOf(rowcount) + SysConst.RECORDSPLITER;
            for (int i = 0; i < rowcount; i++)
            {
                for (int j = 0; j < colcount; j++)
                {
                    if (j != colcount - 1)
                    {
                        strReturn = strReturn + arr[i][j] + SysConst.PACKAGESPILTER;
                    }
                }
                if (i != rowcount - 1)
                {
                    strReturn += SysConst.RECORDSPLITER;
                }
            }
        }
        return strReturn;
    }

    /**
     * 生成给定长度的字符串
     * @param intLength int 字符串长度
     * @return String
     */
    public static String space(int intLength)
    {
        StringBuffer strReturn = new StringBuffer();
        for (int i = 0; i < intLength; i++)
        {
            strReturn.append(" ");
        }
        return new String(strReturn);
    }

    /**
     * 以指定内容生成给定长度的字符串,不足以空格补齐,超长截去
     * @param strValue String 指定内容
     * @param intLength int 字符串长度
     * @return String
     */
    public static String space(String strValue, int intLength)
    {
        int strLen = strValue.length();

        StringBuffer strReturn = new StringBuffer();
        if (strLen > intLength)
        {
            strReturn.append(strValue.substring(0, intLength));
        }
        else
        {
            if (strLen == 0)
            {
                strReturn.append(" ");
            }
            else
            {
                strReturn.append(strValue);
            }

            for (int i = strLen; i < intLength; i++)
            {
                strReturn.append(" ");
            }
        }
        return new String(strReturn);
    }

    /**
     * 得到转化等价字节数组的长度
     * @param strSource String
     * @return int
     */
    public static int getLength(String strSource)
    {
        return strSource.getBytes().length;
    }

    /**
     * 复制文件
     * @param fromFile String
     * @param toFile String
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    public static void copyFile(String fromFile, String toFile) throws FileNotFoundException, IOException, Exception
    {
        FileInputStream in = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);

        byte b[] = new byte[1024];
        int len;

        while ((len = in.read(b)) != -1)
        {
            out.write(b, 0, len);
        }
        out.close();
        in.close();
    }

    /**
     * 数字大写
     * @param intValue int
     * @return String
     */
    public static String toUpper(int intValue)
    {
        String strOutValue = "";
        String[] strTemp =
                { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        try
        {
            strOutValue = strTemp[intValue];
        }
        catch (Exception exception)
        {
            strOutValue = "";
        }
        return strOutValue;
    }

    /**
     * 得到单位
     * @param intValue int
     * @return String
     */
    public static String getUnit(int intValue)
    {
        String strOutValue = "";
        String[] strTemp =
                { "仟", "佰", "拾", "亿", "仟", "佰", "拾", "万", "仟", "佰", "拾", "", "", "" };

        try
        {
            strOutValue = strTemp[intValue];
        }
        catch (Exception exception)
        {
            strOutValue = "";
        }
        return strOutValue;
    }

    /**
     * 数字转换为大写(数据不能超过12位)
     * @param dblInValue double
     * @return String
     */
    public static String toChinese(double dblInValue)
    {
        String strOutValue = "";
        String strValue = new DecimalFormat("0").format(dblInValue * 100);
        String strTemp = "                 ";
        String strThat = "";
        int i = 0;
        int j = 0;
        int k = 0;

        k = strValue.length();
        if (k > 14)
        {
            // Log.printException("数字太大");
            return "";
        }

        strValue = strTemp.substring(0, 14 - k) + strValue;

        for (i = 14 - k; i < 14; i++)
        {

            j = new Integer(strValue.substring(i, i + 1)).intValue();
            if (j > 0)
            {
                strOutValue = strOutValue + strThat + toUpper(j) + getUnit(i);
                strThat = "";
            }
            else
            {
                if (i == 11)
                {
                    strOutValue += getUnit(i);
                }

                if (i == 7 && !strValue.substring(4, 8).equals("0000"))
                {
                    strOutValue += getUnit(i);
                }

                if (i == 3 && !strValue.substring(0, 4).equals("0000"))
                {
                    strOutValue += getUnit(i);
                }

                if (i < 11)
                {
                    strThat = toUpper(0);
                }

                if (i == 12)
                {
                    strThat = toUpper(0);
                }
            }
        }
        return strOutValue;
    }

    /**
     * 数字转换为大写
     * @param intInValue int
     * @return String
     */
    public static String toChinese(int intInValue)
    {
        return toChinese((double) intInValue);
    }

    /**
     * 数字转换为大写
     * @param longInValue long
     * @return String
     */
    public static String toChinese(long longInValue)
    {
        return toChinese((double) longInValue);
    }

    /**
     * 将输入的字符串进行转换，如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
     * @param tStr 输入字符串
     * @return 如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
     */
    public static String cTrim(String tStr)
    {
        String ttStr = "";
        if (tStr == null)
        {
            ttStr = "";
        }
        else
        {
            ttStr = tStr.trim();
        }
        return ttStr;
    }

    /**
     * 将字符串补数 将sourString的前面用cChar补足cLen长度的字符串
     * @param sourString String
     * @param cChar String
     * @param cLen int
     * @return String 字符串,如果字符串超长，则不做处理
     */
    public static String LCh(String sourString, String cChar, int cLen)
    {
        int tLen = sourString.length();
        int i, iMax;
        String tReturn = "";
        if (tLen >= cLen)
        {
            return sourString;
        }
        iMax = cLen - tLen;
        for (i = 0; i < iMax; i++)
        {
            tReturn += cChar;
        }
        tReturn = tReturn.trim() + sourString.trim();
        return tReturn;
    }

    /**
     * 将字符串补数 将sourString的前面用cChar补足cLen长度的字符串
     * @param sourString String
     * @param cChar String
     * @param cLen int
     * @return String 字符串,如果字符串超长，则不做处理
     */
    public static String RCh(String sourString, String cChar, int cLen)
    {
        int tLen = sourString.length();
        int i, iMax;
        String tReturn = "";
        if (tLen >= cLen)
        {
            return sourString;
        }
        iMax = cLen - tLen;
        for (i = 0; i < iMax; i++)
        {
            tReturn += cChar;
        }
        tReturn = tReturn.trim() + sourString.trim();
        return tReturn;
    }

    /**
     * 该函数得到c_Str中的第c_i个以c_Split分割的字符串
     * @param c_Str 目标字符串
     * @param c_i 位置
     * @param c_Split 分割符
     * @return 如果发生异常，则返回空
     */
    public static String getStr(String c_Str, int c_i, String c_Split)
    {
        String t_Str1 = "", t_Str2 = "", t_strOld = "";
        int i = 0, i_Start = 0;
        t_Str1 = c_Str;
        t_Str2 = c_Split;
        i = 0;
        try
        {
            while (i < c_i)
            {
                i_Start = t_Str1.indexOf(t_Str2, 0);
                if (i_Start >= 0)
                {
                    i += 1;
                    t_strOld = t_Str1;
                    t_Str1 = t_Str1.substring(i_Start + t_Str2.length(), t_Str1.length());
                }
                else
                {
                    if (i != c_i - 1)
                    {
                        t_Str1 = "";
                    }
                    break;
                }
            }

            if (i_Start >= 0)
            {
                t_Str1 = t_strOld.substring(0, i_Start);
            }
        }
        catch (Exception ex)
        {
            t_Str1 = "";
        }
        return t_Str1;
    }

    /**
     * 字符串替换函数
     * @param strMain String 原串
     * @param strFind String 查找字符串
     * @param strReplaceWith String 替换字符串,在替换时不区分大小写
     * @return String 替换后的字符串，如果原串为空或者为""，则返回""
     */
    public static String replaceEx(String strMain, String strFind, String strReplaceWith)
    {
        String strReturn = "";
        String tStrMain = strMain.toLowerCase();
        String tStrFind = strFind.toLowerCase();
        int intStartIndex = 0, intEndIndex = 0;

        if (strMain == null || strMain.equals(""))
        {
            return "";
        }

        while ((intEndIndex = tStrMain.indexOf(tStrFind, intStartIndex)) > -1)
        {
            strReturn = strReturn + strMain.substring(intStartIndex, intEndIndex) + strReplaceWith;
            intStartIndex = intEndIndex + strFind.length();
        }
        strReturn += strMain.substring(intStartIndex, strMain.length());
        return strReturn;
    }

    /**
     * 字符串比较函数(处理空值的情况,视null与空字符串相等) 比较字符串a,b
     * @param a String
     * @param b String
     * @return boolean 如果相同返回true,否则返回fasle
     */
    public static boolean compareString(String a, String b)
    {
        if (StrTool.unicodeToGBK(StrTool.cTrim(a)).equals(StrTool.unicodeToGBK(StrTool.cTrim(b))))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public static void main(String[] args) {
        String tStr1=null;
        String tStr2="";
        System.out.println(StrTool.isEmpty(tStr1));
        System.out.println(StrTool.isEmpty(tStr2));

        System.out.println(StrTool.isNull(tStr1));
        System.out.println(StrTool.isNull(tStr2));

        System.out.println(StrTool.isEmptyStr(tStr1));
        System.out.println(StrTool.isEmptyStr(tStr2));
        System.out.println("-------------");
        System.out.println(StrTool.decodeStr("xabcdxabc2xabcwwww","x",1));
        System.out.println(StrTool.decodeStr("xabcdxabc2xabcwwww","x",2));
        System.out.println(StrTool.decodeStr("xabcdxabc2xabcwwww","x",3));

        System.out.println(StrTool.getPos("xabcdxabc2xabcwwww","x",2));
        System.out.println(StrTool.getPos("xabcdxabc2xabcwwww","x",3));
        System.out.println(StrTool.LCh("abc","0",18));
        System.out.println("asdfas"+StrTool.toUpper(2));

        System.out.println(StrTool.toChinese(1222465610.22));

        String[] strTemp =
                { "仟", "佰", "拾", "亿", "仟", "佰", "拾", "万", "仟", "佰", "拾", "", "", "" };
        System.out.println(strTemp[0]);

    }
}
