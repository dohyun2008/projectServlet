<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>가입 화면</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
	
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	
<script type="text/javascript">
$(function() {
	$("#birth").datepicker({
		dateFormat: 'yy-mm-dd',
		changeYear: true,
		maxDate: "+0m+0w"
		});
});
	
function send() { // 버튼 눌렀을때 alert는 여기서 처리
	var f = document.memberForm;

	// 비밀번호 형식 체크
	var pwFormat = /^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i;
	
	if(! $("#pw").val()){
		var s = "<span style='color:red; font-weight:bold;'>비밀번호를 입력하세요.</span>";
		$("#chkPw").html(s);	
		$("#pw").focus();
		return false;
	}
	
	f.action = "<%=cp%>/mem/pwd_ok.do";
	f.submit();
}

</script>

</head>
<body>
<article class="container">

	<div class="col-md-12" style="margin-top: 150px;">
		<div class="page-header">
    		<h1>${title } <small> </small></h1>
        </div>
	<form class="form-horizontal" method="post" name="memberForm">

	  	<div class="form-group">
        	<label class="col-sm-3 control-label">아이디</label>
           	<div class="col-sm-6">
            	<div class="input-group">
                 	<input type="text" class="form-control" name="id" id="id" 
                 	value="${sessionScope.member.userId}" style="width: 500px;" readonly="readonly">
                 	<span class="input-group-btn">
                   		
                 	</span>
                </div>
                <p class="help-block" id="spanId1"></p>
                <p class="help-block" id="spanId2"></p>
       		</div>
        </div>
		
        <div class="form-group">
        	<label class="col-sm-3 control-label">비밀번호</label>
        	<div class="col-sm-6">
          		<input class="form-control" name="pw" id="pw" type="password" style="width: 500px;">
       			<p class="help-block" id="chkPw"></p>
       		</div>
        </div>		
		
	  	<div class="form-group">
	     	<div class="col-sm-12 text-center">
	          	<button class="btn btn-warning " type="button" onclick="send();">확 인</button>
	          	<input type="hidden" name="mode" value="${mode}">
	          
	          	<button class="btn btn-danger " type="button" onclick="javascript:location.href='<%=cp%>/board/profile.do?id=${sessionScope.member.userId}';">돌 아 가 기</button>

	     	</div>
	 	</div>
        
     	</form>
        <hr>
   	</div>
</article>

</body>
</html>