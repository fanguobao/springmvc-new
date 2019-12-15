package utility;


public class JdbcUrlBackUp
{
    private String DBType;
    private String IP;
    private String Port;
    private String DBName;
    private String ServerName;
    private String UserName;
    private String PassWord;

    public JdbcUrlBackUp()
    {
//        DBType = "WEBLOGICPOOL";
//        DBName = "MyPool";

//        DBType = "WEBSPHERE";
//        DBName = "MET";

//        DBType = "COMMONSDBCP";
//        DBName = "DB2";

//        DBType = "INFORMIX";
//        IP = "172.16.120.201";
//        Port = "12000";
//        DBName = "lis";
//        ServerName = "server_intel";
//        UserName = "informix";
//        PassWord = "informix";

//        DBType = "ORACLE";
//        IP = "192.168.71.244";
//        Port = "1521";
//        DBName = "lis";
//        UserName = "lis";
//        PassWord = "lis";

//        DBType = "DB2";
//        IP = "192.168.71.244";
//        Port = "50000";
//        DBName = "circ";
//        UserName = "lis";
//        PassWord = "lis";

		DBType = "ORACLE";
		IP = "192.168.1.7";
		Port = "1521";
		DBName = "lis";
		UserName = "lis";
		PassWord = "lis";

    }

    public String getDBType()
    {
        return DBType;
    }

    public String getIP()
    {
        return IP;
    }

    public String getPort()
    {
        return Port;
    }

    public String getDBName()
    {
        return DBName;
    }

    public String getServerName()
    {
        return ServerName;
    }

    public String getUserName()
    {
        return UserName;
    }

    public String getPassWord()
    {
        return PassWord;
    }

    public void setDBType(String aDBType)
    {
        DBType = aDBType;
    }

    public void setIP(String aIP)
    {
        IP = aIP;
    }

    public void setPort(String aPort)
    {
        Port = aPort;
    }

    public void setDBName(String aDBName)
    {
        DBName = aDBName;
    }

    public void setServerName(String aServerName)
    {
        ServerName = aServerName;
    }

    public void setUser(String aUserName)
    {
        UserName = aUserName;
    }

    public void setPassWord(String aPassWord)
    {
        PassWord = aPassWord;
    }

    public String getJdbcUrl()
    {
        StringBuffer sUrl = new StringBuffer(256);
        //Oracle
        if (DBType.trim().toUpperCase().equals("ORACLE"))
        {
            sUrl.append("jdbc:oracle:thin:@");
            sUrl.append(IP);
            sUrl.append(":");
            sUrl.append(Port);
            sUrl.append(":");
            sUrl.append(DBName);
        }
        //InforMix
        if (DBType.trim().toUpperCase().equals("INFORMIX"))
        {
            sUrl.append("jdbc:informix-sqli://");
            sUrl.append(IP);
            sUrl.append(":");
            sUrl.append(Port);
            sUrl.append(DBName);
            sUrl.append(":");
            sUrl.append("informixserver=");
            sUrl.append(ServerName);
            sUrl.append(";");
            sUrl.append("user=");
            sUrl.append(UserName);
            sUrl.append(";");
            sUrl.append("password=");
            sUrl.append(PassWord);
            sUrl.append(";");
        }
        if (DBType.trim().toUpperCase().equals("SQLSERVER"))
        {
//            sUrl = "jdbc:inetdae:" + IP + ":"
//                   + Port + "?sql7=true&database=" + DBName + "&charset=gbk";
            sUrl.append("jdbc:inetdae:");
            sUrl.append(IP);
            sUrl.append(":");
            sUrl.append(Port);
            sUrl.append("?sql7=true&database=");
            sUrl.append(DBName);
            sUrl.append("&charset=gbk");
        }
        if (DBType.trim().toUpperCase().equals("WEBLOGICPOOL"))
        {
            sUrl.append("jdbc:weblogic:pool:");
            sUrl.append(DBName);
        }
        if (DBType.trim().toUpperCase().equals("DB2"))
        {
            sUrl.append("jdbc:db2://");
            sUrl.append(IP);
            sUrl.append(":");
            sUrl.append(Port);
            sUrl.append("/");
            sUrl.append(DBName);
        }
        return sUrl.toString();
    }
}
