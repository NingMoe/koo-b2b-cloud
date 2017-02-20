package com.koolearn.cloud.util;
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;  
import java.util.StringTokenizer;  

import org.apache.commons.lang3.StringUtils;

public class TableToJavaBean {  
	private static final String DATABASE_NAME = "cloud";  
	private static final String JDBC_URL="jdbc:mysql://192.168.100.65:3301/"+DATABASE_NAME;
	private static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
	private static final String USER_NAME = "ent";  
	private static final String USER_PASSWORD = "ent";  
    private static final String LINE = "\r\n";  
    private static final String TAB = "\t";  
    
    /**转换类型
     * {"TABLE","VIEW","SYSTEM TABLE","GLOBAL TEMPORARY","LOCAL TEMPORARY","ALIAS" , "SYNONYM"}
     */
    private static final String[] TABLE_TYPE = {"TABLE", "VIEW" };
    /**表名模糊查询：过滤转换表  % 表示全部*/
    private static final String FILTER_CONVER_TABLE="%";
    //工具所在包com.koolearn.k12.util;
    String packages = this.getClass().getPackage().getName().replace("util", "school.entity");
    private static Map<String, String> map;  

    static {  
        map = new HashMap<String, String>();  
        //数据库数据类型，java数据类型
        map.put("VARCHAR", "String");  
        map.put("INTEGER", "Integer");  
        map.put("FLOAT", "float");  
        map.put("TIMESTAMP", "Date");  
        map.put("CHAR", "String");  
        map.put("DATETIME", "Date");  
        map.put("INT", "Integer");  
        map.put("DOUBLE", "Double");
        map.put("decimal", "BigDecimal");
        //属性所需导入类包
        map.put("TIMESTAMP_IMPORT", "import java.util.Date");  
        map.put("DATETIME_IMPORT","import java.util.Date");  
    }  
  /**
   * 获取java属性类型
   * @param dataType 数据库字段类型
   * @return
   */
    public static String getPojoType(String dataType) {  
        StringTokenizer st = new StringTokenizer(dataType);  
        return map.get(st.nextToken());  
    }  
      
    public static String getImport(String dataType) {  
        if (map.get(dataType)==null||"".equals(map.get(dataType))) {  
           return null;   
        }else{  
           return map.get(dataType);  
        }  
    }  
  
   /**
    * 根据数据库表名 创建对应java实体对象
    * @param connection
    * @param tableName
    * @param tableComment  表注释
    * @throws SQLException
    */
    public void tableToBean(Connection connection, String tableName,String tableComment) throws SQLException {
    	String sql = "select * from " + tableName + " where 1 <> 1";  
        PreparedStatement ps = null;  
        ResultSet rs = null;  
        ps = connection.prepareStatement(sql);  
        rs = ps.executeQuery();  
        ResultSetMetaData md = rs.getMetaData();  
        int columnCount = md.getColumnCount();  
        StringBuffer sb = new StringBuffer();  
        tableName = tableName.substring(0, 1).toUpperCase() + tableName.subSequence(1, tableName.length());  
        String oldName=tableName;
        tableName = this.dealLine(tableName);  
        tableName=tableName.replace("Xx", "").replace("Yy", "").replace("Sys", "");
        sb.append("package " + this.packages + " ;");  
        sb.append(LINE);  
        importPackage(md, columnCount, sb);  
        sb.append(LINE);  
        sb.append(LINE); 
        if(StringUtils.isNotBlank(tableComment)){
        	sb.append("/**"+LINE+tableComment).append(LINE+"*/"+LINE);
        }
        sb.append("@Entity").append(LINE);//拼接表实体注解
        sb.append("@Table(name = \""+oldName.toLowerCase()+"\")").append(LINE);
        sb.append("public class " + tableName + " implements Serializable {");  
        sb.append(LINE);  
        sb.append(TAB).
          append("private static final long serialVersionUID = 1L;").append(LINE).append(LINE);
        //获取字段注释Map集合
        Map<String,String> commentMap= getColumnCommentTable( connection, oldName);
        defProperty(md, columnCount, sb,commentMap);  
        genSetGet(md, columnCount, sb);  
        sb.append("}");  
        String paths = System.getProperty("user.dir");  
        String endPath = paths + "\\src\\main\\java\\" + (packages.replace("/", "\\")).replace(".", "\\");  
        System.out.println("endPath=="+endPath);
        buildJavaFile("D:\\tol\\data\\cloudJavaBean" + "\\" + tableName + ".java", sb.toString());
    }  
    /**
     * 属性生成get、 set 方法  
     * @param md
     * @param columnCount
     * @param sb
     * @throws SQLException
     */
    private void genSetGet(ResultSetMetaData md, int columnCount, StringBuffer sb) throws SQLException {  
        for (int i = 1; i <= columnCount; i++) {  
            sb.append(TAB);  
            String pojoType = getPojoType(md.getColumnTypeName(i));  
            String columnName = dealLine(md, i);  
            String getName = null;  
            String setName = null;  
            if (columnName.length() > 1) {  
                getName = "public " + pojoType + " get" + columnName.substring(0, 1).toUpperCase()  
                        + columnName.substring(1, columnName.length()) + "() {";  
                setName = "public void set" + columnName.substring(0, 1).toUpperCase()  
                        + columnName.substring(1, columnName.length()) + "(" + pojoType + " " + columnName + ") {";  
            } else {  
                getName = "public get" + columnName.toUpperCase() + "() {";  
                setName = "public set" + columnName.toUpperCase() + "(" + pojoType + " " + columnName + ") {";  
            }  
            sb.append(LINE).append(TAB).append(getName);  
            sb.append(LINE).append(TAB).append(TAB);  
            sb.append("return " + columnName + ";");  
            sb.append(LINE).append(TAB).append("}");  
            sb.append(LINE);  
            sb.append(LINE).append(TAB).append(setName);  
            sb.append(LINE).append(TAB).append(TAB);  
            sb.append("this." + columnName + " = " + columnName + ";");  
            sb.append(LINE).append(TAB).append("}");  
            sb.append(LINE);  
  
        }  
    }  
      
    //导入属性所需包  
    private void importPackage(ResultSetMetaData md, int columnCount, StringBuffer sb) throws SQLException {  
    	sb.append("import java.io.Serializable;").append(LINE);
    	sb.append("import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;").append(LINE);  
    	sb.append("import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;").append(LINE);  
    	sb.append("import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;").append(LINE);  
    	sb.append("import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;").append(LINE);  
    	sb.append("import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;").append(LINE);  
//    	sb.append("import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;").append(LINE);  
        for (int i = 1; i <= columnCount; i++) {  
            String im=getImport(md.getColumnTypeName(i)+"_IMPORT");  
            if (im!=null) {  
                sb.append(im+ ";");  
                sb.append(LINE);  
            }  
        }  
    }  
    /**
     * 属性定义  
     * @param md     ResultSetMetaData  
     * @param columnCount
     * @param sb
     * @param Map<String,String> commentMap  字段名，注释
     * @throws SQLException
     * @Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id; // 编号
	@Column(name = "name")
     */
    private void defProperty(ResultSetMetaData md, int columnCount, StringBuffer sb,Map<String,String> commentMap) throws SQLException {  
    	
        for (int i = 1; i <= columnCount; i++) {  
            sb.append(TAB);  
            //数据库字段名
            String databaseColumnName=md.getColumnName(i);
            //获取字段注释
            String comment=getColumnComment(commentMap,databaseColumnName);
            if(StringUtils.isNotBlank(comment)){
            	sb.append("/** "+comment+" */").append(LINE);
            	sb.append(TAB); 
            }
            if("id".equals(databaseColumnName)){
            	 sb.append("@Id(strategy = GenerationType.AUTO_INCREMENT)");
                 sb.append(LINE);  
            }else{
            	 sb.append("@Column(name = \""+databaseColumnName+"\")");
                 sb.append(LINE);  
            }
            sb.append(TAB);  
            String columnName = dealLine(md, i);  
            sb.append("private " + getPojoType(md.getColumnTypeName(i)) + " " + columnName + ";");  
            sb.append(LINE);  
        }  
    }  
   /**
    * 获取数据库字段注释
    * @param columnComment  字段名称注释 map
    * @param columnName     字段名称
    * @return
    */
    private String getColumnComment(Map<String,String> columnComment,String columnName) {
    	  StringTokenizer st = new StringTokenizer(columnName);  
          return columnComment.get(st.nextToken());  
	}
    private Map<String,String> getColumnCommentTable(Connection connection,String tableName) {
    	try {
			String sql="select COLUMN_COMMENT,COLUMN_NAME,COLUMN_TYPE from information_schema.columns "+
			    	  " where table_schema = '"+DATABASE_NAME+"' and table_name = '"+tableName+"' ";
			PreparedStatement ps = null;  
	        ResultSet rs = null;  
	        ps = connection.prepareStatement(sql);  
	        rs = ps.executeQuery();  
	        Map<String,String> nameCommentMap=new HashMap<String, String>();
	        while(rs.next()){
	        	String name=rs.getString("COLUMN_NAME");
	        	String comment=rs.getString("COLUMN_COMMENT");
	        	nameCommentMap.put(name, comment);
	        }
	       return nameCommentMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private String dealLine(ResultSetMetaData md, int i) throws SQLException {  
        String columnName = md.getColumnName(i);  
        // 处理下划线情况，把下划线后一位的字母变大写；  
        columnName = dealName(columnName);  
        return columnName;  
    }  
  
    private String dealLine(String tableName) {  
        // 处理下划线情况，把下划线后一位的字母变大写；  
        tableName = dealName(tableName);  
        return tableName;  
    }  
    //下划线后一位字母大写  
    private String dealName(String columnName) {  
        if (columnName.contains("_")) {  
            StringBuffer names = new StringBuffer();  
            String arrayName[] = columnName.split("_");  
            names.append(arrayName[0]);  
            for (int i = 1; i < arrayName.length; i++) {  
                String arri=arrayName[i];  
                String tmp=arri.substring(0, 1).toUpperCase()+ arri.substring(1, arri.length());  
                names.append(tmp);  
            }  
            columnName=names.toString();  
        }  
        return columnName;  
    }  
    //生成java文件  
    public void buildJavaFile(String filePath, String fileContent) {  
        try {  
            File file = new File(filePath);  
            FileOutputStream osw = new FileOutputStream(file);  
            PrintWriter pw = new PrintWriter(osw);  
            pw.println(fileContent);  
            pw.close();  
        } catch (Exception e) {  
            System.out.println("生成txt文件出错：" + e.getMessage());  
        }  
    }
    /**
     * 获得某表的建表语句
     * @param tableName
     * @return
     * @throws Exception
     */
    public static String getCommentByTableName(Connection conn,String tableName) throws Exception {
        String createDDL="";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
        if (rs != null && rs.next()) {
            createDDL = rs.getString(2);
            String comment = parseCommentFromcreateDDL(createDDL);//表注释
            System.out.println(createDDL);
        }
        rs.close();
        stmt.close();
        return createDDL;
    }
    /**
     * 返回表注释信息
     * @param createDDL  建表sql
     * @return
     */

    public static String parseCommentFromcreateDDL(String createDDL) {
        String comment = null;
        int index = createDDL.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = createDDL.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }
    public static void main(String[] args)  {
    	try {
    		
			tableToJabaByMysql();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    /**
     * Mysql数据库
     * @throws Exception 
     */
	public static void tableToJabaByMysql() throws Exception { 
        Class.forName(JDBC_DRIVER);  
        Connection conn = DriverManager.getConnection(JDBC_URL, USER_NAME, USER_PASSWORD);
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        String userName=databaseMetaData.getUserName().toString();//获取客户端的连接用户和ip：ent@10.155.38.226
        String connectionURL=databaseMetaData.getStringFunctions().toString();//获取数据库链接地址： jdbc:mysql://192.168.100.65:3301/cloud
        String productName=databaseMetaData.getDatabaseProductName().toString();//获取数据库产品名：MySQL
        String productVersion=databaseMetaData.getDatabaseProductVersion().toString();//获取数据库版本：5.5.18-log
        String driverName=databaseMetaData.getDriverName().toString();//获取数据库驱动程序的名称：MySQL-AB JDBC Driver
        String driverVersion=databaseMetaData.getDriverVersion().toString();//获取数据库驱动程序的版本：mysql-connector-java-5.1.21 ( Revision: ${bzr.revision-id} )
        String systemFunctions=databaseMetaData.getSystemFunctions().toString();//获取系统函数
        String timeDateFunctions=databaseMetaData.getTimeDateFunctions().toString();//获取时间和日期函数
        String stringFunctions=databaseMetaData.getStringFunctions().toString();//获取时间和日期函数
        //数据库名，登录名，表名，类型
        ResultSet rs = databaseMetaData.getTables(null, null, FILTER_CONVER_TABLE,TABLE_TYPE);  
        TableToJavaBean d = new TableToJavaBean();  
        long start=System.currentTimeMillis();
        while(rs.next()){  
        	// 3 TABLE_NAME 表名
            String tableName = rs.getString("TABLE_NAME");
        	// 5 REMARKS  表注释
        	String tableComment = parseCommentFromcreateDDL(getCommentByTableName(conn, tableName));
        	String tableType = rs.getString("TABLE_TYPE");
//        	String tableSchem = rs.getString("TYPE_SCHEM") ;
            if(tableName.toLowerCase().startsWith("tb_")){
            d.tableToBean(conn,tableName,tableComment);

            }
        }
        System.out.println("转换时长："+(System.currentTimeMillis()-start));
        /**
         * TABLE_CAT String => 表类别（可为 null）
			TABLE_SCHEM String => 表模式（可为 null）
			TABLE_NAME String => 表名称
			TABLE_TYPE String => 表类型。
			REMARKS String => 表的解释性注释
			TYPE_CAT String => 类型的类别（可为 null）
			TYPE_SCHEM String => 类型模式（可为 null）
			TYPE_NAME String => 类型名称（可为 null）
			SELF_REFERENCING_COL_NAME String => 有类型表的指定 "identifier" 列的名称（可为 null）
			REF_GENERATION String
         */
    }  
}  