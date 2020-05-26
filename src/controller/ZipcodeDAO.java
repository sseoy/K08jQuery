package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

public class ZipcodeDAO {
	Connection con;
	PreparedStatement psmt;
	ResultSet rs;
	
	public ZipcodeDAO(ServletContext ctx) {
        try {
           Class.forName(ctx.getInitParameter("JDBCDriver"));
           String id = "kosmo";
           String pw = "1234";
           con = DriverManager.getConnection(
              ctx.getInitParameter("ConnectionURL"),id,pw);
           System.out.println("DB연결성공");
        }
        catch(Exception e) {
           System.out.println("DB 연결실패ㅜㅜ;");
            e.printStackTrace();
         }
   }
	public ZipcodeDAO() {
		try {
			Context ctx = new InitialContext();
			
			DataSource source = 
					(DataSource)ctx.lookup("java:comp/env/jdbc/myoracle");
			
			con = source.getConnection();
			System.out.println("DBCP연결성공");
		}
		catch(Exception e) {
			System.out.println("DBCP연결실패");
			e.printStackTrace();
		}
		
	}
	
	
	//우편번호 테이블에서 시/도 가져오기
	public ArrayList<String> getSido(){
		ArrayList<String> sidoAddr = 
				new ArrayList<String>();
		String sql = "SELECT sido FROM zipcode "
				+ " WHERE 1=1 "
				+ " GROUP by sido"
				+ " ORDER BY sido ASC";
		try {
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()) {
				sidoAddr.add(rs.getString(1));
			}
		}catch(Exception e) {}
		
		return sidoAddr;
				
				
	}
	
	//우편번호 테이블에서 각 시/도에 해당하는 구/군 가져오기
	public ArrayList<String> getGugun(String sido){
		ArrayList<String> gugunAddr = 
				new ArrayList<String>();
		
		String sql = "SELECT distinct gugun FROM zipcode "
				+ "  WHERE sido=?"
				+ "  ORDER BY gugun desc";
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, sido);
			rs = psmt.executeQuery();
			while(rs.next()) {
				gugunAddr.add(rs.getString(1));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return gugunAddr;
				
				
	}
	public void close() {
		try {
			if(rs!=null)rs.close();
			if(psmt!=null)psmt.close();
			if(con!=null)con.close();
		}
		catch(Exception e) {
			System.out.println("자원반남시 예외발생");
			e.printStackTrace();
		}
	}
	
}
