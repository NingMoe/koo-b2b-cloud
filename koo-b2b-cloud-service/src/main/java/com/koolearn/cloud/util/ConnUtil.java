package com.koolearn.cloud.util;
import java.sql.Connection;
import java.sql.SQLException;
import com.koolearn.framework.aries.util.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ConnUtil
{
	private static Logger logger = LoggerFactory.getLogger(ConnUtil.class);
	/**
	 * 获取连接
	 * Connection conn = ConnUtil.getTransactionConnection(GlobalConstant.MYSQL_DATASOURCE);
	 * @return
	 */
	public static Connection getTransactionConnection(){
		Connection conn = null;
		try 
		{
			conn = ConnectionUtils.getTransactionConnection(GlobalConstant.MYSQL_DATASOURCE);
			conn.setAutoCommit(false);
		} catch (SQLException ex)
		{
			ex.printStackTrace();
			logger.error("======================getTransactionConnection===========", ex);
		}
		return conn;
	}
	
	//回滚
	public static void rollbackConnection(Connection conn)
	{
		try 
		{
			if(conn != null)
			{	
				conn.rollback();
			}
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			logger.error("======================rollbackConnection===========", ex);
		}
	}
	
	//关闭Connection
	public static void closeConnection(Connection conn)
	{
		if (conn != null) 
		{
			try 
			{
				conn.close();
			} catch (SQLException ex)
			{
				ex.printStackTrace();
				logger.error("======================closeConnection===========", ex);
			}
		}
	}
}
