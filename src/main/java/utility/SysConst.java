package utility;

/**
 ****************************************************************
 *               Program NAME: 系统常量类
 *                 programmer: Ouyangsheng
 *                Create DATE: 2002.04.17
 *             Create address: Beijing
 *                Modify DATE:
 *             Modify address:
 *****************************************************************
 *
 *                    保存系统中的常量。
 *
 *****************************************************************
 */
public class SysConst
{
    /* 系统信息 */
    public static final int FAILURE = -1;
    public static final int SUCCESS = 0;
    public static final int NOTFOUND = 100;

    /* 系统变量 */
    public static final String EMPTY = null;
    public static final boolean CHANGECHARSET = false; // Unicode to GBK

    /* 信息分隔符 */
    public static final String PACKAGESPILTER = "|";
    public static final String RECORDSPLITER = "^";
    public static final String ENDOFPARAMETER = "^";
    public static final String EQUAL = "=";
    public static final String CONTAIN = "*";

    /* 查询显示设置 */
    public static final int MAXSCREENLINES = 10; //每一页最大显示的行数
    public static final int MAXMEMORYPAGES = 20; //内存中存储的最大的页数

    /* 设置信息 */
    public static final String ZERONO = "00000000000000000000"; //对于没有号码的字段的默认值
    public static final String POOLINFO = "poolname";
    public static final String PARAMETERINFO = "parameterbuf";
    public static final String POOLTYPE = "pooltype";
    public static final String MAXSIZE = "maxsize";
    public static final String MINSIZE = "minsize";

    public static final String USERLOGPATH = "userlogpath";
    public static final String SYSLOGPATH = "syslogpath";

    public static final String COMP = "comp";
    public static final String ENCRYPT = "encrypt";
    public static final String MACFLAG = "macflag";
    public static final String SIGNFLAG = "signflag";
    public static final String SRC = "src";
    public static final String SND = "snd";
    public static final String RCV = "rcv";
    public static final String PRIOR = "prior";

    /* 交费间隔 */
    public static final String PayIntvMonth = "月交";
    public static final String PayIntvQuarter = "季交";
    public static final String PayIntvHalfYear = "半年交";
    public static final String PayIntvYear = "年交";

    /*建议书数据同步*/
    public static final int Number = 5000;

    /*报表系统保险公司编码*/
    public static final String CorpCode = "000085";

    /**
     * 一年的天数
     * 在PubFun的AccountManage中计算利息时用到
     */
    public static final String DAYSOFYEAR = "365";

    /*系统号码管理类型：SysMaxNo实现类的后缀，如民生的实现类为SysMaxNoMinSheng*/
    public static final String MAXNOTYPE = "TK";

    /*数据库类型：DB2、ORACLE等*/
//    public static final String DBTYPE = "DB2";
    public static final String DBTYPE = "ORACLE";

    //大批量数据查询时，使用的缓冲区大小
    public static final int FETCHCOUNT = 2000;

    //给付责任初步筛选：初步筛选出客户所有的给付责任和给付责任给付
    public static String GETDUTYGET = "GetDutyGetImpl";

    //自动责任匹配
    public static String AUTOCHOOSEDUTY = "AutoClaimDutyMapImpl";

}
