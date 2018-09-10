<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>

<form class="form-horizontal" method="post" name="msgForm" action="<%=cp%>/board/msg_ok.do">

	<div class="form-group">
		<label class="col-sm-3 control-label">보내는 사람</label>
		<div class="col-sm-6">
			<div class="input-group">
			<input class="form-control" name="sendId" id="inputSendId" type="text" value="${sendId}" readonly="readonly" style="border-radius: 7px;">
			</div>
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label">받는 사람</label>
		<div class="col-sm-6">
		<div class="input-group">
		<input class="form-control" name="getId" id="inputGetId" type="text" value="${getId}" readonly="readonly" style="border-radius: 7px;">
		</div>
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label">제 목</label>
		<div class="col-sm-6">
			<input class="form-control" name="title" id="title" type="text">
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label"></label>
		<div class="col-sm-6">
			<textarea rows="15" cols="50" style="resize: none; border-radius: 7px;" id="message" name="message"></textarea>
		</div>
	</div>
	
	<div class="form-group">
	     <div class="col-sm-12 text-center">
	          <button class="btn btn-warning" type="button" id="btnSendMsg" style="padding: 10px 30px;">전 송</button>
	          <button class="btn btn-danger" type="button" id="btnCancelMsg" style="padding: 10px 30px;">취 소</button>
	     </div>
	</div>
</form>