package utility;//package main.java.utility;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//import com.sinosoft.lis.vschema.LMRiskSet;
//
///**
// * <p>
// * Title: Webҵ��ϵͳ
// * </p>
// * <p>
// * ClassName: RSWrapper
// * </p>
// * <p>
// * Description: ResultSet������ �������4GL���α��FETCH����,��Ҫ���ڴ�������������.
// * </p>
// * @author Yang YaLin
// * @version 1.0
// */
//public class RSWrapper
//{
//
//	// @Field
//	private Connection con;
//
//	// private DBOper db;
//
//	/**
//	 * flag = true: ����Connection flag = false: ������Connection
//	 */
//	private boolean useOutterConnection = false;
//
//	public CErrors mErrors = new CErrors(); // ������Ϣ
//
//	/**
//	 * Ϊ����������׼���������α����
//	 */
//	private ResultSet mResultSet = null;
//
//	private Statement mStatement = null;
//
//	private SchemaSet container = null;
//
//	// @Constructor
//	public RSWrapper(Connection tConnection)
//	{
//		con = tConnection;
//		useOutterConnection = true;
//	}
//
//	public RSWrapper()
//	{
//		con = null;
//		useOutterConnection = false;
//	}
//
//	/**
//	 * ׼�����ݲ�ѯ����
//	 * @param strSQL String
//	 * @return boolean
//	 */
//	public boolean prepareData(SchemaSet container, String strSQL)
//	{
//		this.container = container;
//
//		System.out.println("prepareData:" + strSQL);
//
//		if (mResultSet != null)
//		{
//			// @@������
//			CError tError = new CError();
//			tError.moduleName = "LMDutyDB";
//			tError.functionName = "prepareData";
//			tError.errorMessage = "���ݼ��ǿգ�������׼�����ݼ�֮��û�йرգ�";
//			this.mErrors.addOneError(tError);
//			return false;
//		}
//
//		if (!useOutterConnection)
//		{
//			con = DBConnPool.getConnection();
//		}
//		try
//		{
//			mStatement = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			mResultSet = mStatement.executeQuery(StrTool.GBKToUnicode(strSQL));
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			// @@������
//			CError tError = new CError();
//			tError.moduleName = "RSWrapper";
//			tError.functionName = "prepareData";
//			tError.errorMessage = e.toString();
//			this.mErrors.addOneError(tError);
//			try
//			{
//				mResultSet.close();
//			}
//			catch (Exception ex2)
//			{}
//			try
//			{
//				mStatement.close();
//			}
//			catch (Exception ex3)
//			{}
//			if (!useOutterConnection)
//			{
//				try
//				{
//					con.close();
//				}
//				catch (Exception et)
//				{}
//			}
//			return false;
//		}
//
//		System.out.println("success prepared...");
//		return true;
//	}
//
//	/**
//	 * ��ȡ���ݼ�
//	 * @return boolean
//	 */
//	private boolean next()
//	{
//		boolean flag = true;
//		if (null == mResultSet)
//		{
//			CError tError = new CError();
//			tError.moduleName = "LMDutyDB";
//			tError.functionName = "hasMoreData";
//			tError.errorMessage = "���ݼ�Ϊ�գ�����׼�����ݼ���";
//			this.mErrors.addOneError(tError);
//			return false;
//		}
//		try
//		{
//			flag = mResultSet.next();
//		}
//		catch (Exception ex)
//		{
//			CError tError = new CError();
//			tError.moduleName = "LMDutyDB";
//			tError.functionName = "hasMoreData";
//			tError.errorMessage = ex.toString();
//			this.mErrors.addOneError(tError);
//			try
//			{
//				mResultSet.close();
//				mResultSet = null;
//			}
//			catch (Exception ex2)
//			{}
//			try
//			{
//				mStatement.close();
//				mStatement = null;
//			}
//			catch (Exception ex3)
//			{}
//			if (!useOutterConnection)
//			{
//				try
//				{
//					con.close();
//				}
//				catch (Exception et)
//				{}
//			}
//			return false;
//		}
//		return flag;
//	}
//
//	/**
//	 * ��ȡ��������
//	 * @return SchemaSet
//	 */
//	public SchemaSet getData()
//	{
//		container.clear();
//
//		if (null == mResultSet)
//		{
//			CError tError = new CError();
//			tError.moduleName = "RSWrapper";
//			tError.functionName = "getData";
//			tError.errorMessage = "���ݼ�Ϊ�գ�����׼�����ݼ���";
//			this.mErrors.addOneError(tError);
//			return null;
//		}
//		try
//		{
//			int tCount = 0;
//			String clzName = container.getClass().getName();
//			clzName = clzName.substring(clzName.lastIndexOf(".") + 1, clzName.length() - 3) + "Schema";
//
//			// ע��mResultSet.next()������
//			while (tCount++ < SysConst.FETCHCOUNT && mResultSet.next())
//			{
//				Schema schema = (Schema) Class.forName("com.sinosoft.lis.schema." + clzName).newInstance();
//				schema.setSchema(mResultSet, 1);
//				container.add(schema);
//			}
//
//			System.out.println("RSWrapper Actual Fetched Row:" + tCount);
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			CError tError = new CError();
//			tError.moduleName = "RSWrapper";
//			tError.functionName = "getData";
//			tError.errorMessage = ex.toString();
//			this.mErrors.addOneError(tError);
//			try
//			{
//				mResultSet.close();
//				mResultSet = null;
//			}
//			catch (Exception ex2)
//			{}
//			try
//			{
//				mStatement.close();
//				mStatement = null;
//			}
//			catch (Exception ex3)
//			{}
//			if (!useOutterConnection)
//			{
//				try
//				{
//					con.close();
//				}
//				catch (Exception et)
//				{}
//			}
//			return null;
//		}
//		return container;
//	}
//
//	/**
//	 * �ر����ݼ�
//	 * @return boolean
//	 */
//	public boolean close()
//	{
//		boolean flag = true;
//		try
//		{
//			if (null == mResultSet)
//			{
//				CError tError = new CError();
//				tError.moduleName = "RSWrapper";
//				tError.functionName = "closeData";
//				tError.errorMessage = "���ݼ��Ѿ��ر��ˣ�";
//				this.mErrors.addOneError(tError);
//				flag = false;
//			}
//			else
//			{
//				mResultSet.close();
//				mResultSet = null;
//			}
//		}
//		catch (Exception ex2)
//		{
//			CError tError = new CError();
//			tError.moduleName = "RSWrapper";
//			tError.functionName = "closeData";
//			tError.errorMessage = ex2.toString();
//			this.mErrors.addOneError(tError);
//			flag = false;
//		}
//		try
//		{
//			if (null == mStatement)
//			{
//				CError tError = new CError();
//				tError.moduleName = "RSWrapper";
//				tError.functionName = "closeData";
//				tError.errorMessage = "����Ѿ��ر��ˣ�";
//				this.mErrors.addOneError(tError);
//				flag = false;
//			}
//			else
//			{
//				mStatement.close();
//				mStatement = null;
//			}
//		}
//		catch (Exception ex3)
//		{
//			CError tError = new CError();
//			tError.moduleName = "RSWrapper";
//			tError.functionName = "closeData";
//			tError.errorMessage = ex3.toString();
//			this.mErrors.addOneError(tError);
//			flag = false;
//		}
//
//		System.out.println("use out connection:" + useOutterConnection);
//
//		if (!useOutterConnection)
//		{
//			try
//			{
//				con.close();
//				System.out.println("inner connection closed");
//			}
//			catch (Exception ex)
//			{
//				ex.printStackTrace();
//			}
//		}
//		return flag;
//	}
//
//	public static void main(String[] args)
//	{
//		RSWrapper rswrapper = new RSWrapper();
//		LMRiskSet lmRiskSet = new LMRiskSet();
//		rswrapper.prepareData(lmRiskSet, "select * from lmrisk");
//		do
//		{
//			rswrapper.getData();
//			System.out.println("get data count:" + lmRiskSet.size());
//		}
//		while (lmRiskSet.size() > 0);
//		rswrapper.close();
//		System.out.println("commplete");
//	}
//}