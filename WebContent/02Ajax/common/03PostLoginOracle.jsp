<%@page import="controller.OracleDAO"%>
<%@page import="org.json.simple.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
//폼값을 받음
String id = request.getParameter("user_id");
String pw = request.getParameter("user_pw");
OracleDAO dao = new OracleDAO(application);
boolean isMember = dao.isMember(id, pw);
JSONObject json = new JSONObject();

if(isMember == true) {
   json.put("result", 1);
   json.put("message", "로그인 성공입니다.");
   
   String html = "";
   html += "<table class='table table-bordered' style='width:300px;'>";
   html += "   <tr>";
   html += "      <td>회원님, 반갑습니다.</td>";
   html += "   </tr>";
   html += "</table>";
   
   json.put("html", html);
}
else {
   //아이디/패스워드가 일치하지 않는 경우 result를 0으로 반환한다.
   json.put("result", 0);
   json.put("message", "로그인 실패입니다 :(");
}

//JSON객체를 String타입으로 형변환 후 화면에 내용을 출력한다.
String jsonStr = json.toJSONString();
out.println(jsonStr);
%>