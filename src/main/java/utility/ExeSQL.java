package utility;//package main.java.utility;
//
//
//import main.java.tools.Arith;
//import main.java.tools.PubFun;
//import main.java.tools.StrTool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.math.BigDecimal;
//import java.sql.*;
//import java.util.Vector;
//
//public class ExeSQL {
//    private Connection con;
//
//    /**
//     * mflag = true: 传入Connection mflag = false: 不传入Connection
//     */
//    private boolean mflag = false;
//
//    private FDate fDate = new FDate();
//
//    public CErrors mErrors = new CErrors(); // 错误信息
//
//    private Logger m_log =  LoggerFactory.getLogger(BaseDao.class);
//
//    public ExeSQL(Connection tConnection) {
//        con = tConnection;
//        mflag = true;
//    }
//
//    public ExeSQL() {
//    }
//
//    /**
//     * 获取唯一的返回值
//     * @param sql String
//     * @return String
//     */
//    public String getOneValue(String sql) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        String mValue = "";
//        // System.out.println("ExecSQL : " + sql);
//
//        // add by yt，如果没有传入连接，则类创建
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                // 其实并不是很合适，主要是因为有可能取得对象的数据类型有误
//                mValue = rs.getString(1);
//                break;
//            }
//            rs.close();
//            pstmt.close();
//            // 如果连接是类创建的，则关闭连接
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // @@错误处理
//            System.out.println("### Error ExeSQL at OneValue: " + sql);
//            CError.buildErr(this, e.toString());
//            // 设置返回值
//            mValue = "";
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        // Stringtools.log("",sql+"="+StrTool.cTrim(mValue),"");
//        return StrTool.cTrim(mValue);
//    }
//
//    /**
//     * 获取SQL的查询结果记录数
//     *
//     * @param sql   String
//     * @param pstmt PreparedStatement
//     * @param rs    ResultSet
//     * @return int
//     */
//    private int getResultCount(String sql, PreparedStatement pstmt, ResultSet rs) {
//        int iCount = 0;
//        // 此方法对不同数据库通用
//        sql = "select count(1) from (" + sql + ") rsc";
//        System.out.println("getResultCount : " + sql);
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            rs = pstmt.executeQuery();
//            // rs.next();
//            // 这样可以保证，没有查询到数据的时候，也返回正常
//            while (rs.next()) {
//                iCount = rs.getInt(1);
//                break;
//            }
//            rs.close();
//            pstmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString());
//            iCount = 0;
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            System.out.println("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                // 可能出现连接没有关闭
//            }
//        }
//        return iCount;
//    }
//
//    /**
//     * 从指定位置查询全部数据
//     *
//     * @param sql   String
//     * @param start int
//     * @return String
//     */
//    public String getEncodedResult(String sql, int start) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        StringBuffer mResult = new StringBuffer(256); // modified by liuqiang
//        System.out.println("ExecSQL : " + sql);
//
//        // add by Fanym
//        if (start <= 0) {
//            start = 1;
//        }
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//            // 查询字段的个数
//            int n = rsmd.getColumnCount();
//            // 查询记录的数量
//            int m = start + (SysConst.MAXSCREENLINES * SysConst.MAXMEMORYPAGES);
//
//            // 取得总记录数
//            int k = 0;
//
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//            while (rs.next()) {
//                dt.increase();
//                k++;
//                if ((k >= start) && (k < m)) {
//                    // only get record we needed
//                    for (int j = 1; j <= n; j++) {
//                        if (j == 1) {
//                            mResult.append(getDataValue(rsmd, rs, j));
//                        } else {
//                            mResult.append(SysConst.PACKAGESPILTER + getDataValue(rsmd, rs, j));
//                        }
//                    }
//                    mResult.append(SysConst.RECORDSPLITER);
//                }
//            }
//
//            if (k >= start) {
//                if (k > 10000) {
//                    System.out.println("建议采用大批量数据查询模式！");
//                }
//                // "0|"为查询成功标记，与CODEQUERY统一，MINIM修改
//                mResult.insert(0, "0|" + String.valueOf(k) + SysConst.RECORDSPLITER);
//                mResult.delete(mResult.length() - 1, mResult.length());
//            } else {
//                mResult.append("100|未查询到相关数据!");
//            }
//            rs.close();
//            pstmt.close();
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            System.out.println("### Error ExeSQL at getEncodedResult(String sql, int start): " + sql);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString());
//            mResult.setLength(0);
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return mResult.toString();
//    }
//
//    /**
//     * 从指定位置查询全部数据，此方法为大数据量查询
//     *
//     * @param sql   String
//     * @param start int
//     * @return String
//     */
//    public String getEncodedResultLarge(String sql, int start) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        StringBuffer mResult = new StringBuffer(256); // modified by liuqiang
//
//        // add by Fanym
//        if (start <= 0) {
//            start = 1;
//        }
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        // 取得总记录数 add by liuqiang
//        int iCount = getResultCount(sql, pstmt, rs);
//        // 如果记录数为0，表示没有查询的数据，这个时候，需要关闭连接
//        if (iCount <= 0) {
//            try {
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                // 可能连接会没有关闭
//            }
//            // 直接返回，查询结果为空
//            return "100|未查询到相关数据!";
//        }
//
//        try {
//            // 查询记录的数量
//            int m = start + (SysConst.MAXSCREENLINES * SysConst.MAXMEMORYPAGES);
//
//            // 根据数据库，查询指定范围数据集，采用此方法可以大幅度提高前台的分页查询效率
//            StringBuffer tSBql = new StringBuffer();
//            if (SysConst.DBTYPE.equals("ORACLE")) {
//                tSBql.append("select * from (select rownum rnm,rs.* from (");
//                tSBql.append(sql);
//                tSBql.append(") rs where rownum < ");
//                tSBql.append(m);
//                tSBql.append(") rss where rnm >= ");
//                tSBql.append(start);
//            } else {
//                tSBql.append("select * from (select rownumber() OVER () rnm ,rs.* from (");
//                tSBql.append(sql);
//                tSBql.append(") rs) rss WHERE rnm BETWEEN ");
//                tSBql.append(start);
//                tSBql.append(" and ");
//                tSBql.append(m - 1);
//            }
//
//            System.out.println("ExecSQL : " + tSBql.toString());
//
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(tSBql.toString()), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//            // 查询字段的个数
//            int n = rsmd.getColumnCount();
//
//            int k = 0; // 用来判定是否有数据
//            while (rs.next()) {
//                k++;
//                // 直接从位置2开始就ok了，呵呵，怎么没想到呢！！！
//                for (int j = 2; j <= n; j++) {
//                    if (j == 2) {
//                        mResult.append(getDataValue(rsmd, rs, j));
//                    } else {
//                        mResult.append(SysConst.PACKAGESPILTER + getDataValue(rsmd, rs, j));
//                    }
//                }
//                mResult.append(SysConst.RECORDSPLITER);
//            }
//
//            if (k > 0) {
//                // "0|"为查询成功标记，与CODEQUERY统一，MINIM修改
//                mResult.insert(0, "0|" + String.valueOf(iCount) + SysConst.RECORDSPLITER);
//                mResult.delete(mResult.length() - 1, mResult.length());
//            } else {
//                mResult.append("100|未查询到相关数据!");
//            }
//            rs.close();
//            pstmt.close();
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            System.out.println("### Error ExeSQL at getEncodedResultLarge(String sql, int start): " + sql);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString());
//            mResult.setLength(0);
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return mResult.toString();
//    }
//
//    /**
//     * 查询数据
//     *
//     * @param sql String
//     * @return String
//     */
//    public String getEncodedResult(String sql) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        StringBuffer mResult = new StringBuffer(256); // modified by liuqiang
//
//        System.out.println("ExecSQL : " + sql);
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            // 重新设置缓冲区，主要采用此中方式的查询数据量在几千左右
//            pstmt.setFetchSize(500);
//            rs = pstmt.executeQuery();
//            rs.setFetchSize(500);
//            rsmd = rs.getMetaData();
//
//            int n = rsmd.getColumnCount();
//
//            int k = 0;
//
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//            // 取得总记录数
//            while (rs.next()) {
//                dt.increase();
//                k++;
//                for (int j = 1; j <= n; j++) {
//                    if (j == 1) {
//                        mResult.append(getDataValue(rsmd, rs, j));
//                    } else {
//                        mResult.append(SysConst.PACKAGESPILTER);
//                        mResult.append(getDataValue(rsmd, rs, j));
//                    }
//                }
//                mResult.append(SysConst.RECORDSPLITER);
//            }
//            if (k > 0) {
//                // "0|"为查询成功标记，与CODEQUERY统一，MINIM修改
//                mResult.insert(0, "0|" + String.valueOf(k) + SysConst.RECORDSPLITER);
//                mResult.delete(mResult.length() - 1, mResult.length());
//            } else {
//                mResult.append("100|未查询到相关数据！");
//            }
//            rs.close();
//            pstmt.close();
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            System.out.println("### Error ExeSQL at getEncodedResult(String sql): " + sql);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString());
//            mResult.setLength(0);
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return mResult.toString();
//    }
//
//    /**
//     * 从指定位置查询定量数据
//     *
//     * @param sql    String
//     * @param start  int
//     * @param nCount int
//     * @return String
//     */
//    public String getEncodedResult(String sql, int start, int nCount) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        StringBuffer mResult = new StringBuffer(256); // modified by liuqiang
//
//        System.out.println("ExecSQL : " + sql);
//        // add by Fanym
//        if (start <= 0) {
//            start = 1;
//        }
//        if (nCount <= 0) {
//            nCount = 1;
//        }
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//        // 取得总记录数 add by liuqiang
//        // int iCount = getResultCount(sql,pstmt,rs);
//        // if (iCount <= 0) return "";
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//            int n = rsmd.getColumnCount();
//            int m = start + nCount;
//            // 取得总记录数
//            int k = 0;
//
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//            while (rs.next()) {
//                dt.increase();
//                k++;
//                // 如果超过要取的记录数，直接退出
//                if (k >= m) {
//                    break;
//                }
//                if ((k >= start) && (k < m)) {
//                    // only get record we needed
//                    for (int j = 1; j <= n; j++) {
//                        if (j == 1) {
//                            mResult.append(getDataValue(rsmd, rs, j));
//                        } else {
//                            mResult.append(SysConst.PACKAGESPILTER).append(getDataValue(rsmd, rs, j));
//                        }
//                    }
//                    mResult.append(SysConst.RECORDSPLITER);
//                }
//            }
//
//            if (k >= start) {
//                // "0|"为查询成功标记，与CODEQUERY统一，MINIM修改
//                mResult.insert(0, "0|" + String.valueOf(k) + SysConst.RECORDSPLITER);
//                mResult.delete(mResult.length() - 1, mResult.length());
//            } else {
//                mResult.append("100|未查询到相关数据！");
//            }
//            rs.close();
//            pstmt.close();
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            System.out.println("### Error ExeSQL at getEncodedResult(String sql, int start, int nCount): " + sql);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString());
//            mResult.setLength(0);
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return mResult.toString();
//    }
//
//    /**
//     * 把ResultSet中取出的数据转换为相应的数据值字符串
//     * 输出：如果成功执行，返回True，否则返回False，并且在Error中设置错误的详细信息
//     *
//     * @param rsmd ResultSetMetaData
//     * @param rs   ResultSet
//     * @param i    int
//     * @return String
//     */
//    public String getDataValue(ResultSetMetaData rsmd, ResultSet rs, int i) {
//        String strValue = "";
//
//        try {
//            int dataType = rsmd.getColumnType(i);
//            int dataScale = rsmd.getScale(i);
//            int dataPrecision = rsmd.getPrecision(i);
//            // 数据类型为字符
//            if ((dataType == Types.CHAR) || (dataType == Types.VARCHAR)) {
//                // 由于存入数据库的数据是GBK模式，因此没有必要做一次unicodeToGBK
//                // strValue = StrTool.unicodeToGBK(rs.getString(i));
//                strValue = rs.getString(i);
//            }
//            // 数据类型为日期、时间
//            else if ((dataType == Types.TIMESTAMP) || (dataType == Types.DATE)) {
//                strValue = fDate.getString(rs.getDate(i));
//            }
//            // 数据类型为浮点
//            else if ((dataType == Types.DECIMAL) || (dataType == Types.FLOAT)) {
//                // strValue = String.valueOf(rs.getFloat(i));
//                // 采用下面的方法使得数据输出的时候不会产生科学计数法样式
//                strValue = String.valueOf(rs.getBigDecimal(i));
//                // 去零处理
//                strValue = PubFun.getInt(strValue);
//            }
//            // 数据类型为整型
//            else if ((dataType == Types.INTEGER) || (dataType == Types.SMALLINT)) {
//                strValue = String.valueOf(rs.getInt(i));
//                strValue = PubFun.getInt(strValue);
//            }
//            // 数据类型为浮点
//            else if (dataType == Types.NUMERIC) {
//                if (dataScale == 0) {
//                    if (dataPrecision == 0) {
//                        // strValue = String.valueOf(rs.getDouble(i));
//                        // 采用下面的方法使得数据输出的时候不会产生科学计数法样式
//                        strValue = String.valueOf(rs.getBigDecimal(i));
//                    } else {
//                        strValue = String.valueOf(rs.getLong(i));
//                    }
//                } else {
//                    // strValue = String.valueOf(rs.getDouble(i));
//                    // 采用下面的方法使得数据输出的时候不会产生科学计数法样式
//                    strValue = String.valueOf(rs.getBigDecimal(i));
//                }
//                strValue = PubFun.getInt(strValue);
//            }
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return StrTool.cTrim(strValue);
//    }
//
//    /**
//     * 功能：执行数据库更新Sql 输入：cSQL，在ExeSQL类初始化的时候建立连接。 <br>
//     * 输出：如果成功执行，返回True，否则返回False，并且在Error中设置错误的详细信息
//     *
//     * @param sql String
//     * @return boolean
//     */
//    public boolean execUpdateSQL(String sql) {
//        PreparedStatement pstmt = null;
//        System.out.println("ExecSQL : " + sql);
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            // 这里是否可以修改，还需要测试一下
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//
//            pstmt.executeUpdate();
//            // int operCount = pstmt.executeUpdate();
//            pstmt.close();
//
//            if (!mflag) {
//                con.commit();
//                con.close();
//            }
//        } catch (SQLException e) {
//            // @@错误处理
//            System.out.println("### Error ExeSQL at execUpdateSQL: " + sql);
//            CError.buildErr(this, e.toString());
//
//            try {
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.rollback();
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                // 在这个地方，有可能会没有关闭连接
//                ex.printStackTrace();
//                return false;
//            }
//
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 功能：可以执行输入的任意查询SQL语句。 <br>
//     * 输入：任意一个查询语句的字符串csql 返回：一个SSRS类的实例，内为查询结果
//     *
//     * @param sql String
//     * @return SSRS
//     */
//    public SSRS execSQL(String sql) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        SSRS tSSRS = null;
//
//        System.out.println("ExecSQL : " + sql);
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//
//            int n = rsmd.getColumnCount();
//            tSSRS = new SSRS(n);
//
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//            // 取得总记录数
//            while (rs.next()) {
//                dt.increase();
//                for (int j = 1; j <= n; j++) {
//                    tSSRS.SetText(getDataValue(rsmd, rs, j));
//                }
//            }
//
//            rs.close();
//            pstmt.close();
//
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            System.out.println("### Error ExeSQL at execSQL(String sql): " + sql);
//            e.printStackTrace();
//
//            // @@错误处理
//            CError.buildErr(this, e.toString());
//
//            tSSRS = null;
//
//            // tSSRS.ErrorFlag = true;
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return tSSRS;
//    }
//
//    public SSRS execSQL(String sql, int start, int nCount) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        SSRS tSSRS = null;
//
//        System.out.println("ExecSQL : " + sql);
//        // add by Fanym
//        if (start <= 0) {
//            start = 1;
//        }
//
//        if (nCount <= 0) {
//            nCount = 1;
//        }
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//
//            int n = rsmd.getColumnCount();
//            tSSRS = new SSRS(n);
//
//            int m = start + nCount;
//            int k = 0;
//
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//            // 取得总记录数
//            while (rs.next()) {
//                dt.increase();
//                k++;
//
//                // 只取特定范围内的记录行
//                if ((k >= start) && (k < m)) {
//                    for (int j = 1; j <= n; j++) {
//                        tSSRS.SetText(getDataValue(rsmd, rs, j));
//                    }
//                }
//            }
//
//            rs.close();
//            pstmt.close();
//
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            System.out.println("### Error ExeSQL at execSQL(String sql, int start, int nCount): " + sql);
//            e.printStackTrace();
//
//            // @@错误处理
//            CError.buildErr(this, e.toString());
//
//            tSSRS = null;
//
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return tSSRS;
//    }
//
//    //	硬解析改造--20150709--hp--增加开始
//    // ***********************************************************************************//
//    // 绑定变量执行组件添加开始
//    // **********************************************************************************//
//    public SSRS execSQL(VData tVData) {
//        PreparedStatement pstmt = null;
//        String sql = (String) tVData.get(0);
//        TransferData tParam = new TransferData();
//        tParam = tVData.get(1) == null ? (new TransferData())
//                : (TransferData) tVData.get(1);
//
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        SSRS tSSRS = null;
//
//        m_log.debug("ExecSQL BV: " + sql);
//        System.out.println("ExecSQL : " + sql);
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql),
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            // 赋值
//            setBindValues(pstmt, tParam);
//
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//
//            int n = rsmd.getColumnCount();
//            tSSRS = new SSRS(n);
//
//            // Kevin 2006-08-15
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//
//            // 取得总记录数
//            while (rs.next()) {
//                dt.increase();
//
//                for (int j = 1; j <= n; j++) {
//                    tSSRS.SetText(getDataValue(rsmd, rs, j));
//                }
//            }
//
//            rs.close();
//            pstmt.close();
//
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            m_log.error("### Error ExeSQL at execSQL(String sql): " + sql);
//            printVData(tVData);
//            e.printStackTrace();
//
//            // @@错误处理
//            CError.buildErr(this, e.toString(), mErrors);
//
//            tSSRS = null;
//
//            // tSSRS.ErrorFlag = true;
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            m_log.debug("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } finally {
//            close(rs);
//            close(pstmt);
//            if (!mflag) {
//                close(con);
//            }
//        }
//
//        if (tSSRS != null && tSSRS.getMaxRow() > 2000) {
//            m_log.debug("01 01 01 SSRS行数为:" + tSSRS.getMaxRow() + ";SQL为:"
//                    + sql);
//        }
//
//        return tSSRS;
//    }
//
//    public boolean execUpdateSQL(VData tVData) {
//        PreparedStatement pstmt = null;
//        String sql = (String) tVData.get(0);
//        TransferData tParam = new TransferData();
//        tParam = tVData.get(1) == null ? (new TransferData())
//                : (TransferData) tVData.get(1);
//        m_log.debug("execUpdateSQL BV : " + sql);
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            // 这里是否可以修改，还需要测试一下
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql),
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//            // 赋值
//            setBindValues(pstmt, tParam);
//
//            pstmt.executeUpdate();
//            // int operCount = pstmt.executeUpdate();
//            pstmt.close();
//
//            if (!mflag) {
//                con.commit();
//                con.close();
//            }
//        } catch (SQLException e) {
//            // @@错误处理
//            m_log.error("### Error ExeSQL at execUpdateSQL: " + sql);
//            printVData(tVData);
//            CError.buildErr(this, e.toString(), mErrors);
//
//            try {
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            m_log.debug("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.rollback();
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                // 在这个地方，有可能会没有关闭连接
//                ex.printStackTrace();
//                return false;
//            }
//
//            return false;
//        } finally {
//            close(pstmt);
//            if (!mflag) {
//                close(con);
//            }
//        }
//        return true;
//    }
//
//    public String getOneValue(VData tVData) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        String mValue = "";
//        String sql = (String) tVData.get(0);
//        TransferData tParam = new TransferData();
//        tParam = tVData.get(1) == null ? (new TransferData())
//                : (TransferData) tVData.get(1);
//
//        m_log.debug("ExecSQL : " + sql);
//        System.out.println("ExecSQL : " + sql);
//
//        // add by yt，如果没有传入连接，则类创建
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql),
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            // 赋值
//            setBindValues(pstmt, tParam);
//
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                // 其实并不是很合适，主要是因为有可能取得对象的数据类型有误
//                mValue = rs.getString(1);
//                break;
//            }
//            rs.close();
//            pstmt.close();
//            // 如果连接是类创建的，则关闭连接
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            // @@错误处理
//            m_log.error("### Error ExeSQL at OneValue: " + sql);
//            printVData(tVData);
//            CError.buildErr(this, e.toString(), mErrors);
//            // 设置返回值
//            mValue = "";
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            m_log.debug("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } finally {
//            close(rs);
//            close(pstmt);
//            if (!mflag) {
//                close(con);
//            }
//        }
//        return StrTool.cTrim(mValue);
//    }
//
//    private void setBindValues(PreparedStatement pstmt, TransferData tParam)
//            throws SQLException {
//        Vector tNames = tParam.getValueNames();
//        // 增加异常处理，这里可能抛出类型转换的异常
//        try {
//            for (int i = 0; i < tNames.size(); i++) {
//                String tTypeAndValue = (String) tParam
//                        .getValueByName((String) tNames.get(i));
//                String[] tParams = tTypeAndValue.split(":");
//                String tType = tParams[0];
//                String tValue = "";
//                if (tParams.length == 2) {
//                    tValue = tParams[1];
//                }
//                m_log.debug("tType:" + tType + ":tValue:" + tValue);
//                if (tType.toLowerCase().equals("string")) {
//                    if (tValue == null || tValue.equals("")) {
//                        pstmt.setNull(i + 1, 12);
//                    } else {
//                        pstmt.setString(i + 1, tValue);
//                    }
//                } else if (tType.toLowerCase().equals("double")) {
//                    pstmt.setDouble(i + 1, Double.parseDouble(tValue));
//                } else if (tType.toLowerCase().equals("int")) {
//                    pstmt.setInt(i + 1, Integer.parseInt(tValue));
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//    public String getEncodedResult(VData tVData, int start) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        StringBuffer mResult = new StringBuffer(256); // modified by liuqiang
//        String sql = (String) tVData.get(0);
//        TransferData tParam = new TransferData();
//        tParam = tVData.get(1) == null ? (new TransferData())
//                : (TransferData) tVData.get(1);
//        m_log.debug("ExecSQLBV : " + sql);
//
//        // add by Fanym
//        if (start <= 0) {
//            start = 1;
//        }
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql),
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            setBindValues(pstmt, tParam);
//
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//            // 查询字段的个数
//            int n = rsmd.getColumnCount();
//            // 查询记录的数量
//            int m = start + (SysConst.MAXSCREENLINES * SysConst.MAXMEMORYPAGES);
//
//            // 取得总记录数
//            int k = 0;
//
//            // Kevin 2006-08-15
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//
//            while (rs.next()) {
//                dt.increase();
//
//                k++;
//                if ((k >= start) && (k < m)) {
//                    // only get record we needed
//                    for (int j = 1; j <= n; j++) {
//                        if (j == 1) {
//                            mResult.append(getDataValue(rsmd, rs, j));
//                        } else {
//                            mResult.append(SysConst.PACKAGESPILTER
//                                    + getDataValue(rsmd, rs, j));
//                        }
//                    }
//                    mResult.append(SysConst.RECORDSPLITER);
//                }
//            }
//
//            if (k >= start) {
//                if (k > 10000) {
//                    m_log.debug("建议采用大批量数据查询模式！");
//                }
//                // "0|"为查询成功标记，与CODEQUERY统一，MINIM修改
//                mResult.insert(0, "0|" + String.valueOf(k)
//                        + SysConst.RECORDSPLITER);
//                mResult.delete(mResult.length() - 1, mResult.length());
//            } else {
//                mResult.append("100|未查询到相关数据!");
//            }
//            rs.close();
//            pstmt.close();
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            m_log.error("### Error ExeSQL at getEncodedResult(String sql, int start): "
//                    + sql);
//            printVData(tVData);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString(), mErrors);
//            mResult.setLength(0);
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            m_log.debug("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } finally {
//            close(rs);
//            close(pstmt);
//            if (!mflag) {
//                close(con);
//            }
//        }
//        return mResult.toString();
//    }
//
//    public String getEncodedResultEx(VData tVData, int start) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        StringBuffer mResult = new StringBuffer(256); // modified by liuqiang
//        String sql = (String) tVData.get(0);
//        TransferData tParam = new TransferData();
//        tParam = tVData.get(1) == null ? (new TransferData())
//                : (TransferData) tVData.get(1);
//        m_log.debug("ExecSQLBV : " + sql);
//
//        // add by Fanym
//        if (start <= 0) {
//            start = 1;
//        }
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql),
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            setBindValues(pstmt, tParam);
//
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//            // 查询字段的个数
//            int n = rsmd.getColumnCount();
//            // 查询记录的数量
//            // int m = start + (SysConst.MAXSCREENLINES *
//            // SysConst.MAXMEMORYPAGES);
//            // 突破默认200条数据限制,但不能过分放开,经和张荣讨论,暂定最多为1000条
//            int m = start + 1000;
//
//            // 取得总记录数
//            int k = 0;
//
//            // Kevin 2006-08-15
//            DBThreshold dt = new DBThreshold();
//            dt.setSQL(sql);
//
//            while (rs.next()) {
//                dt.increase();
//
//                k++;
//                if ((k >= start) && (k < m)) {
//                    // only get record we needed
//                    for (int j = 1; j <= n; j++) {
//                        if (j == 1) {
//                            mResult.append(getDataValue(rsmd, rs, j));
//                        } else {
//                            mResult.append(SysConst.PACKAGESPILTER
//                                    + getDataValue(rsmd, rs, j));
//                        }
//                    }
//                    mResult.append(SysConst.RECORDSPLITER);
//                }
//            }
//
//            if (k >= start) {
//                if (k > 10000) {
//                    m_log.debug("建议采用大批量数据查询模式！");
//                }
//                // "0|"为查询成功标记，与CODEQUERY统一，MINIM修改
//                mResult.insert(0, "0|" + String.valueOf(k)
//                        + SysConst.RECORDSPLITER);
//                mResult.delete(mResult.length() - 1, mResult.length());
//            } else {
//                mResult.append("100|未查询到相关数据!");
//            }
//            rs.close();
//            pstmt.close();
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            m_log.error("### Error ExeSQL at getEncodedResult(String sql, int start): "
//                    + sql);
//            printVData(tVData);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString(), mErrors);
//            mResult.setLength(0);
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            m_log.debug("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } finally {
//            close(rs);
//            close(pstmt);
//            if (!mflag) {
//                close(con);
//            }
//        }
//        return mResult.toString();
//    }
//
//    public String getEncodedResultLarge(VData tVData, int start) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        ResultSetMetaData rsmd = null;
//        StringBuffer mResult = new StringBuffer(256); // modified by liuqiang
//        String sql = (String) tVData.get(0);
//        TransferData tParam = new TransferData();
//        tParam = tVData.get(1) == null ? (new TransferData())
//                : (TransferData) tVData.get(1);
//        m_log.debug("ExecSQLBV : " + sql);
//        // add by Fanym
//        if (start <= 0) {
//            start = 1;
//        }
//
//        // add by yt
//        if (!mflag) {
//            con = DBConnPool.getConnection();
//        }
//
//        // 取得总记录数 add by liuqiang
//        int iCount = getResultCount(sql, pstmt, rs, tParam);
//        // 如果记录数为0，表示没有查询的数据，这个时候，需要关闭连接
//        if (iCount <= 0) {
//            try {
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                // 可能连接会没有关闭
//            }
//            // 直接返回，查询结果为空
//            return "100|未查询到相关数据!";
//        }
//
//        try {
//            // 查询记录的数量
//            int m = start + (SysConst.MAXSCREENLINES * SysConst.MAXMEMORYPAGES);
//
//            // 根据数据库，查询指定范围数据集，采用此方法可以大幅度提高前台的分页查询效率
//            StringBuffer tSBql = new StringBuffer();
//            if (SysConst.DBTYPE.equals("ORACLE")) {
//                tSBql.append("select * from (select rownum rnm,rs.* from (");
//                tSBql.append(sql);
//                tSBql.append(") rs where rownum < ");
//                tSBql.append(m);
//                tSBql.append(") rss where rnm >= ");
//                tSBql.append(start);
//            } else {
//                tSBql.append("select * from (select rownumber() OVER () rnm ,rs.* from (");
//                tSBql.append(sql);
//                tSBql.append(") rs) rss WHERE rnm BETWEEN ");
//                tSBql.append(start);
//                tSBql.append(" and ");
//                tSBql.append(m - 1);
//            }
//
//            m_log.debug("ExecSQL : " + tSBql.toString());
//
//            pstmt = con.prepareStatement(
//                    StrTool.GBKToUnicode(tSBql.toString()),
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            setBindValues(pstmt, tParam);
//            rs = pstmt.executeQuery();
//            rsmd = rs.getMetaData();
//            // 查询字段的个数
//            int n = rsmd.getColumnCount();
//
//            int k = 0; // 用来判定是否有数据
//            while (rs.next()) {
//                k++;
//                // 直接从位置2开始就ok了，呵呵，怎么没想到呢！！！
//                for (int j = 2; j <= n; j++) {
//                    if (j == 2) {
//                        mResult.append(getDataValue(rsmd, rs, j));
//                    } else {
//                        mResult.append(SysConst.PACKAGESPILTER
//                                + getDataValue(rsmd, rs, j));
//                    }
//                }
//                mResult.append(SysConst.RECORDSPLITER);
//            }
//
//            if (k > 0) {
//                // "0|"为查询成功标记，与CODEQUERY统一，MINIM修改
//                mResult.insert(0, "0|" + String.valueOf(iCount)
//                        + SysConst.RECORDSPLITER);
//                mResult.delete(mResult.length() - 1, mResult.length());
//            } else {
//                mResult.append("100|未查询到相关数据!");
//            }
//            rs.close();
//            pstmt.close();
//            if (!mflag) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            m_log.error("### Error ExeSQL at getEncodedResultLarge(String sql, int start): "
//                    + sql);
//            printVData(tVData);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString(), mErrors);
//            mResult.setLength(0);
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            m_log.debug("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } finally {
//            close(rs);
//            close(pstmt);
//            if (!mflag) {
//                close(con);
//            }
//        }
//        return mResult.toString();
//    }
//
//    private int getResultCount(String sql, PreparedStatement pstmt,
//                               ResultSet rs, TransferData tParam) {
//        int iCount = 0;
//        // 此方法对不同数据库通用
//        sql = "select count(1) from (" + sql + ") rsc";
//        m_log.debug("getResultCount : " + sql);
//
//        try {
//            pstmt = con.prepareStatement(StrTool.GBKToUnicode(sql),
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//
//            setBindValues(pstmt, tParam);
//
//            rs = pstmt.executeQuery();
//            // rs.next();
//            // 这样可以保证，没有查询到数据的时候，也返回正常
//            while (rs.next()) {
//                iCount = rs.getInt(1);
//                break;
//            }
//            rs.close();
//            pstmt.close();
//        } catch (SQLException e) {
//            m_log.error("### Error ExeSQL at getEncodedResultLarge(String sql, int start): "
//                    + sql);
//            e.printStackTrace();
//            // @@错误处理
//            CError.buildErr(this, e.toString(), mErrors);
//            iCount = 0;
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pstmt != null) {
//                    // 由于描述的问题，导致执行的sql错误百出，因此pstmt的关闭需要特殊处理
//                    try {
//                        pstmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        try {
//                            m_log.debug("Sql's bug is very big: " + sql);
//                            pstmt.close();
//                        } catch (SQLException ex) {
//                        }
//                    }
//                }
//                if (!mflag) {
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                // 可能出现连接没有关闭
//            }
//        } finally {
//            close(rs);
//            close(pstmt);
//            if (!mflag) {
//                close(con);
//            }
//        }
//        return iCount;
//    }
//
//
//    public void printVData(VData tVData) {
//
//        TransferData tParam = new TransferData();
//        tParam = (TransferData) tVData.getObjectByObjectName("TransferData", 0);
//        Vector tNames = tParam.getValueNames();
//
//        for (int i = 0; i < tNames.size(); i++) {
//            String tTypeAndValue = (String) tParam
//                    .getValueByName((String) tNames.get(i));
//            String[] tParams = tTypeAndValue.split(":");
//            String tType = tParams[0];
//            String tValue = "";
//            if (tParams.length == 2) {
//                tValue = tParams[1];
//            }
//            m_log.error("变量类型:" + tType + ":变量值:" + tValue);
//        }
//    }
//
//
//    private static void close(ResultSet rs) {
//        if (rs != null) {
//            try {
//                rs.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private static void close(Statement stmt) {
//        if (stmt != null) {
//            try {
//                stmt.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private static void close(Connection conn) {
//        if (conn != null) {
//            try {
//                conn.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
////	硬解析改造--20150709--hp--增加结束
//
//    /**
//     * 测试函数
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        // System.out.println(PubFun1.CreateMaxNo("zhuxf", "86110001"));
//        ExeSQL tExeSQL = new ExeSQL();
//        for (int i = 1; i <= 1; i++) {
//            // tExeSQL.getOneValue("null");
//            tExeSQL.execSQL("select remark from lcgrpcont where grpcontno='230101000033'");
//            SSRS tSSRS = tExeSQL.execSQL("select remark from lcgrpcont where grpcontno='230101000033'");
//            System.out.println(tSSRS.GetText(1, 1));
//            // String a = tExeSQL.getOneValue("select remark from lcgrpcont
//            // where grpcontno='230101000033'");
//        }
//        // Stringtools a = new Stringtools();
//        double a = 9999999999999999.000;
//        BigDecimal ccc = new BigDecimal(Arith.round(a, 2));
//
//        System.out.println(Stringtools.stringtostring(ccc.toString(), 2));
//        System.out.println(Stringtools.stringtostring("999999999999999999999999.001", 2));
//        System.out.println(Stringtools.stringtostring("999999999999999999999999.002", 2));
//        System.out.println(Stringtools.stringtostring("999999999999999999999999.003", 2));
//    }
//}