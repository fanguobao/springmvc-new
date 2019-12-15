package tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PubFun {
    private static String  datepattern="yyyy-MM-dd";
    private static String  timepattern="HH:mm:ss";
    /**
     * 获取系统当前日期
     * @return
     */
    public static String getCurrentDate(String pattern)
    {
        if(StrTool.isEmpty(pattern)){
            pattern=datepattern;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String tString = df.format(today);
        return tString;
    }

    /**
     * 获取系统当前时间
     * @return
     */
    public static String getCurrentTime(String pattern)
    {
        if(StrTool.isEmpty(pattern)){
            pattern=timepattern;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String tString = df.format(today);
        return tString;
    }

    /**
     * 由给定日期字符串获取格式日期(年/月/日)
     * @param strYear String 年
     * @param strMonth String 月
     * @param strDay String 日
     * @return String 格式日期：年/月/日
     */
    public static String getDate(String strYear, String strMonth, String strDay)
    {
        String strReturn = "";
        int intYear = 0;
        int intMonth = 0;
        int intDay = 0;
        if ((strYear != null) && (strMonth != null) && (strDay != null) && (strYear.trim().length() > 0)
                && (strMonth.trim().length() > 0) && (strDay.trim().length() > 0))
        {
            try
            {
                intYear = new Integer(strYear).intValue();
                intMonth = new Integer(strMonth).intValue();
                intDay = new Integer(strDay).intValue();
            }
            catch (Exception exception)
            {
                return strReturn;
            }

            if ((intYear <= 0) || (intMonth <= 0) || (intMonth > 12) || (intDay <= 0) || (intDay > 31))
            {
                strReturn = "";
            }
            else
            {
                strReturn = "" + intYear;

                if (intMonth < 10)
                {
                    strReturn += StrTool.DATEDELIMITER + "0" + intMonth;
                }
                else
                {
                    strReturn += StrTool.DATEDELIMITER + intMonth;
                }

                if (intDay < 10)
                {
                    strReturn += StrTool.DATEDELIMITER + "0" + intDay;
                }
                else
                {
                    strReturn += StrTool.DATEDELIMITER + intDay;
                }
            }
        }
        return strReturn;
    }

    /**
     * 获得系统日期(年/月/日)
     * @return String 格式日期：年/月/日
     */
    public static String getDate()
    {
        String strReturn = "";
        int intYear = Calendar.getInstance().get(Calendar.YEAR);
        int intMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int intDate = Calendar.getInstance().get(Calendar.DATE);
        strReturn = "" + intYear;

        if (intMonth < 10)
        {
            strReturn += StrTool.DATEDELIMITER + "0" + intMonth;
        }
        else
        {
            strReturn += StrTool.DATEDELIMITER + intMonth;
        }

        if (intDate < 10)
        {
            strReturn += StrTool.DATEDELIMITER + "0" + intDate;
        }
        else
        {
            strReturn += StrTool.DATEDELIMITER + intDate;
        }
        return strReturn;
    }

    /**
     * 由给定日期字符串获取格式日期(年/月)
     * @param strYear String 年
     * @param strMonth String 月
     * @return String 格式日期：年/月
     */
    public static String getDate(String strYear, String strMonth)
    {
        String strReturn = "";
        int intYear = 0;
        int intMonth = 0;
        if ((strYear != null) && (strMonth != null) && (strYear.trim().length() > 0) && (strMonth.trim().length() > 0))
        {
            intYear = new Integer(strYear).intValue();
            intMonth = new Integer(strMonth).intValue();
            if ((intYear <= 0) || (intMonth <= 0) || (intMonth > 12))
            {
                strReturn = "";
            }
            else
            {
                strReturn = "" + intYear + StrTool.DATEDELIMITER + intMonth;
            }
        }
        return strReturn;
    }

    /**
     * 获取日期值中的年份
     * @param strDate String 日期（年/月/日）
     * @return String 年
     */
    public static String getYear(String strDate)
    {
        int intPosition = 0;
        String strReturn = "";
        int intYear = 0;

        if ((strDate != null) && (strDate.trim().length() > 0))
        {
            intPosition = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 1);
            if (intPosition > 0)
            {
                strReturn = strDate.substring(0, intPosition);
                intYear = new Integer(strReturn).intValue();
                if ((intYear <= 0))
                {
                    strReturn = "";
                }
                else
                {
                    strReturn = "" + intYear;
                }

                if ((intYear < 10) && (!strReturn.equals("")))
                {
                    strReturn = "0" + strReturn;
                }
            }
        }
        return strReturn;
    }

    /**
     * 获取系统日期中的年份
     * @return String 年
     */
    public static String getYear()
    {
        String strReturn = "";
        int intYear = Calendar.getInstance().get(Calendar.YEAR);
        strReturn = "" + intYear;
        return strReturn;
    }

    /**
     * 获取日期值中的月份
     * @param strDate String 日期
     * @return String 月
     */
    public static String getMonth(String strDate)
    {
        int intPosition1 = 0, intPosition2 = 0;
        String strReturn = "";
        int intMonth = 0;
        if ((strDate != null) && (strDate.trim().length() > 0))
        {
            intPosition1 = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 1);
            intPosition2 = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 2);
            if ((intPosition1 > 0) && intPosition2 > intPosition1)
            {

                strReturn = strDate.substring(intPosition1 + 1, intPosition2);

                intMonth = new Integer(strReturn).intValue();
                if ((intMonth <= 0) || (intMonth > 12))
                {
                    strReturn = "";
                }
                else
                {
                    strReturn = "" + intMonth;
                }

                if ((intMonth < 10) && (!strReturn.equals("")))
                {
                    strReturn = "0" + strReturn;
                }
            }
        }
        return strReturn;
    }

    /**
     * 获取系统日期中的月份
     * @return String 月
     */
    public static String getMonth()
    {
        String strReturn = "";
        int intMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (intMonth < 10)
        {
            strReturn = "0" + intMonth;
        }
        else
        {
            strReturn = "" + intMonth;
        }
        return strReturn;
    }

    /**
     * 获取给定日期值中的天
     * @param strDate String 日期
     * @return String 天
     */
    public static String getDay(String strDate)
    {
        int intPosition = 0;
        String strReturn = "";
        int intDay = 0;
        if ((strDate != null) && (strDate.trim().length() > 0))
        {
            intPosition = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 2);
            if (intPosition > 0)
            {

                strReturn = strDate.substring(intPosition + 1);

                intDay = new Integer(strReturn).intValue();

                if ((intDay <= 0) || (intDay > 31))
                {
                    strReturn = "";
                }
                else
                {
                    strReturn = "" + intDay;
                }

                if ((intDay < 10) && (!strReturn.equals("")))
                {
                    strReturn = "0" + strReturn;
                }
            }
        }
        return strReturn;
    }

    /**
     * 获取系统日期值中的天
     * @return String 天
     */
    public static String getDay()
    {
        String strReturn = "";
        int intDate = Calendar.getInstance().get(Calendar.DATE);
        if (intDate < 10)
        {
            strReturn = "0" + intDate;
        }
        else
        {
            strReturn = "" + intDate;
        }

        return strReturn;
    }

    /**
     * 获取系统时间值
     * @return String 小时: 分钟：秒
     */
    public static String getTime()
    {
        return PubFun.getHour() + ":" + PubFun.getMinute() + ":" + PubFun.getSecond();
    }

    /**
     * 获取系统时间值中的小时
     * @return String 小时
     */
    public static String getHour()
    {
        String strReturn = "";
        int intHour = Calendar.getInstance().get(Calendar.HOUR) + (Calendar.HOUR_OF_DAY + 1)
                * Calendar.getInstance().get(Calendar.AM_PM);
        if (intHour < 10)
        {
            strReturn = "0" + intHour;
        }
        else
        {
            strReturn = "" + intHour;
        }

        return strReturn;
    }

    /**
     * 获取系统时间值中的分钟
     * @return String 分钟
     */
    public static String getMinute()
    {
        String strReturn = "";
        int intMinute = Calendar.getInstance().get(Calendar.MINUTE);
        if (intMinute < 10)
        {
            strReturn = "0" + intMinute;
        }
        else
        {
            strReturn = "" + intMinute;
        }

        return strReturn;
    }

    /**
     * 获取系统时间值中秒数
     * @return String 秒数
     */
    public static String getSecond()
    {
        String strReturn = "";
        int intSecond = Calendar.getInstance().get(Calendar.SECOND);
        if (intSecond < 10)
        {
            strReturn = "0" + intSecond;
        }
        else
        {
            strReturn = "" + intSecond;
        }

        return strReturn;
    }

    /**
     * 获取以"-"分隔日期值中的年份
     * @param strDate String 日期
     * @return String 年
     */
    public static String getVisaYear(String strDate)
    {
        int intPosition = 0;
        String strReturn = "";
        int intYear = 0;

        if ((strDate != null) && (strDate.trim().length() > 0))
        {
            intPosition = StrTool.getPos(strDate, StrTool.VISADATEDELIMITER, 1);
            if (intPosition > 0)
            {
                strReturn = strDate.substring(0, intPosition);
                intYear = new Integer(strReturn).intValue();
                if ((intYear <= 0))
                {
                    strReturn = "";
                }
                else
                {
                    strReturn = "" + intYear;
                }

                if ((intYear < 10) && (!strReturn.equals("")))
                {
                    strReturn = "0" + strReturn;
                }
            }
        }
        return strReturn;
    }

    /**
     * 获取以"-"分隔日期值中的月份
     * @param strDate String 日期
     * @return String 月
     */
    public static String getVisaMonth(String strDate)
    {
        int intPosition1 = 0, intPosition2 = 0;
        String strReturn = "";
        int intMonth = 0;
        if ((strDate != null) && (strDate.trim().length() > 0))
        {
            intPosition1 = StrTool.getPos(strDate, StrTool.VISADATEDELIMITER, 1);
            intPosition2 = StrTool.getPos(strDate, StrTool.VISADATEDELIMITER, 2);
            if ((intPosition1 > 0) && intPosition2 > intPosition1)
            {

                strReturn = strDate.substring(intPosition1 + 1, intPosition2);

                intMonth = new Integer(strReturn).intValue();
                if ((intMonth <= 0) || (intMonth > 12))
                {
                    strReturn = "";
                }
                else
                {
                    strReturn = "" + intMonth;
                }

                if ((intMonth < 10) && (!strReturn.equals("")))
                {
                    strReturn = "0" + strReturn;
                }
            }
        }
        return strReturn;
    }

    /**
     * 获取以"-"分隔日期值中的天
     * @param strDate String 日期
     * @return String 天
     */
    public static String getVisaDay(String strDate)
    {
        int intPosition = 0;
        String strReturn = "";
        int intDay = 0;
        if ((strDate != null) && (strDate.trim().length() > 0))
        {
            intPosition = StrTool.getPos(strDate, StrTool.VISADATEDELIMITER, 2);
            if (intPosition > 0)
            {

                strReturn = strDate.substring(intPosition + 1);

                intDay = new Integer(strReturn).intValue();

                if ((intDay <= 0) || (intDay > 31))
                {
                    strReturn = "";
                }
                else
                {
                    strReturn = "" + intDay;
                }

                if ((intDay < 10) && (!strReturn.equals("")))
                {
                    strReturn = "0" + strReturn;
                }
            }
        }
        return strReturn;
    }

    /**
     * 获取以"-"分隔日期值的中文表示
     * @param strDate String 日期
     * @return String YYYY年MM月DD日
     */
    public static String getChnDate(String strDate)
    {
        String strReturn = getVisaYear(strDate) + "年" + getVisaMonth(strDate) + "月" + getVisaDay(strDate) + "日";
        return strReturn;
    }



//    /**
//     *<p>生成流水号的函数<p>
//     *<p>号码规则：机构编码  日期年  校验位   类型    流水号<p>
//     *<p>          1-6     7-10   11     12-13   14-20<p>
//     * @param cNoType 为需要生成号码的类型
//     * @param cNoLimit 为需要生成号码的限制条件
//     * @return 生成的符合条件的流水号，如果生成失败，返回空字符串""
//     */
//    public static String CreateMaxNo(String cNoType, String cNoLimit)
//    {
//        try
//        {
//            //动态载入类
//            System.out.println("sysmaxnotype:" + SysConst.MAXNOTYPE);
//            String className = "com.sinosoft.lis.pubfun.SysMaxNo" + SysConst.MAXNOTYPE;
//            Class cc = Class.forName(className);
//            com.sinosoft.lis.pubfun.SysMaxNo tSysMaxNo = (com.sinosoft.lis.
//                    pubfun.
//                    SysMaxNo) (cc.newInstance());
//            return tSysMaxNo.CreateMaxNo(cNoType, cNoLimit);
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//            return null;
//        }
//
//    }
}
