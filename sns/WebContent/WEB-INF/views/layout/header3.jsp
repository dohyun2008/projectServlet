<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>

<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
      		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
	        	<span class="icon-bar"></span>
	        	<span class="icon-bar"></span>
	        	<span class="icon-bar"></span>                        
      		</button>
    <img src="<%=cp %>/resources/img/pig.png" style="height:55px; width:55px;">
    <a class="navbar-brand" href="<%=cp%>/board/main.do">돼 지 들</a>
    	</div>
    <div class="collapse navbar-collapse">

      <ul class="nav navbar-nav navbar-right" id="myNavbar1">
     	<li><a href="<%=cp%>/board/main.do"><span class="glyphicon glyphicon-th-list">&nbsp;LIST</span></a></li>
     	<li><a href="<%=cp%>/board/created.do?screen=profile"><span class="glyphicon glyphicon-pencil">&nbsp;POST</span></a></li>
     	<li><a href="<%=cp%>/mem/pwd.do?mode=update"><span class="glyphicon glyphicon-transfer">&nbsp;정보수정</span></a></li>
     	<li><a href="<%=cp%>/mem/pwd.do?mode=delete"><span class="glyphicon glyphicon-log-in">&nbsp;탈 퇴</span></a></li>
        <li><a href="<%=cp%>/mem/logout.do"><span class="glyphicon glyphicon-off"></span> &nbsp;LOGOUT</a></li>
      </ul>
      
     <ul class="nav navbar-nav navbar-right" id="myNavbar2">
     	<li><a href="<%=cp%>/board/main.do"><span class="glyphicon glyphicon-log-in">&nbsp;LIST</span></a></li>
     	<li><a href="<%=cp%>/board/created.do"><span class="glyphicon glyphicon-log-in">&nbsp;POST</span></a></li>
        <li><a href="<%=cp%>/mem/logout.do"><span class="glyphicon glyphicon-off"></span> &nbsp;LOGOUT</a></li>
      </ul>
    </div>
    
  </div>
</nav>