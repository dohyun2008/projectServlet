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
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
      	<li><input type="text" class="form-control" id="search" name="search" style="margin-top: 10px;" placeholder="#검 색 어"></li>
      	<li>&nbsp;<button type="button" id="btnSearch" class="btn btn-warning" style="margin-top: 10px; margin-right: 15px;" onclick="searchListPage(1);">검색</button></li>
     	<li><a href="<%=cp%>/board/profile.do?id=${sessionScope.member.userId}"><span class="glyphicon glyphicon-user">&nbsp;MY PROFILE</span></a></li>
     	<li><a href="<%=cp%>/board/created.do?screen=list"><span class="glyphicon glyphicon-pencil">&nbsp;POST</span></a></li>
        <li><a href="<%=cp%>/mem/logout.do"><span class="glyphicon glyphicon-off"></span> &nbsp;LOGOUT</a></li>
      </ul>
    </div>
  </div>
</nav>