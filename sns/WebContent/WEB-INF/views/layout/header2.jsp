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
    <a class="navbar-brand" href="#">돼 지 들</a>
    	</div>
    <div class="collapse navbar-collapse" id="myNavbar">

      <ul class="nav navbar-nav navbar-right">
     	<li><span class="glyphicon glyphicon-log-in"></span></li>
     	<li><span class="glyphicon glyphicon-log-in"></span></li>
        <li><span class="glyphicon glyphicon-log-in"></span></li>
      </ul>
    </div>
  </div>
</nav>