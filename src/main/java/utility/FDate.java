package utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FDate implements Cloneable {
    public CErrors mErrors = new CErrors();
    private final String pattern = "yyyy-MM-dd";
    private final String pattern1 = "yyyyMMdd";
    private SimpleDateFormat df;
    private SimpleDateFormat df1;

    public FDate() {
        df = new SimpleDateFormat(pattern);
        df1 = new SimpleDateFormat(pattern1);
    }

    /**
     * 克隆FDate对象
     * @return Object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        FDate cloned = (FDate) super.clone();
        return cloned;
    }

    /**
     * 输入符合格式要求的日期字符串，返回日期类型变量
     * @param dateString 日期字符串
     * @return 日期类型变量
     */
    public Date getDate(String dateString) {
        Date tDate = null;
        try {
            if (dateString.indexOf("-") != -1) {
                tDate = df.parse(dateString);
            } else {
                tDate = df1.parse(dateString);
            }
        } catch (Exception e) {
            CError tError = new CError();
            tError.moduleName = "FDate";
            tError.functionName = "getDate";
            tError.errorMessage = e.toString();
            this.mErrors.addOneError(tError);
        }

        return tDate;
    }

    /**
     * 输入日期类型变量，返回日期字符串
     * @param mDate 日期类型变量
     * @return 日期字符串
     */
    public String getString(Date mDate) {
        String tString = null;
        if (mDate != null) {
            tString = df.format(mDate);
        }
        return tString;
    }

    /**
     * MAIN方法，测试用
     * @param args String[]
     */
    public static void main(String[] args) {
//        FDate tFDate = new FDate();
//        System.out.println(tFDate.getDate("2002-10-8"));
    }
}
