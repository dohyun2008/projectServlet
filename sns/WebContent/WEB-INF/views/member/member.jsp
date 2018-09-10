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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
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
//ID 공백+형식+중복체크
function chkUserId(){
	// 입력란에서 받아온 ID 값
	var id = $("#id").val();
	var url="<%=cp%>/mem/chkId.do";
	var query = "id="+id;
	// 아이디 구성 포맷 선언
	var format = /^[a-z][a-z0-9_]{4,9}$/i;
	// 받아온 ID값이 없는 경우
	if(! id){
		var s = "<span style='color:red; font-weight:bold;'>아이디를 입력하세요.</span>";
		$("#spanId2").html(s);
		$("#id").focus();
	}
	// 받아온 ID의 포맷이 올바르지 않은 경우 
	else if(! format.test($("#id").val())) {
		var s = "<span style='color:red; font-weight:bold;'>올바른 형식으로 입력하세요.</span>";
		$("#spanId2").html(s);
		$("#id").val("");
		$("#id").focus();
	} 
	// 사용할 수 있는 포맷의 ID인 경우
	else {
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data){
				var p = data.passed;
				if(p=="true") {
					var s = "<span style='color:blue; font-weight:bold;'>"+id+"</span> 아이디는 사용 가능 합니다.";
					$("#spanId2").html(s);
				} else{
					var s = "<span style='color:red; font-weight:bold;'>"+id+"</span> 아이디는 다른 사용자가 사용 중입니다.";
					$("#spanId2").html(s);
					$("#id").val("");
					$("#id").focus();
				}
			}
		});	
	}
}

// 닉네임 공백+형식+중복체크
function chkUserNick(){
	var nick = $("#nick").val();
	var url="<%=cp%>/mem/chkNick.do";
	var query = "nick="+nick;
	var format = /^[가-힣0-9]{3,10}$/;
	if(! nick){
		var s = "<span style='color:red; font-weight:bold;'>닉네임을 입력하세요.</span>";
		$("#regNick").html(s);
		$("#nick").focus();
	} else if(! format.test($("#nick").val())){
		var s = "<span style='color:red; font-weight:bold;'>올바른 형식으로 입력하세요.</span>";
		$("#regNick").html(s);
		$("#nick").val("");
		$("#nick").focus();
	} else {
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data){
				var p = data.passed;
				if(p=="true") {
					var s = "<span style='color:blue; font-weight:bold;'>"+nick+"</span> 닉네임은 사용 가능 합니다.";
					$("#regNick").html(s);
				} else{
					var s = "<span style='color:red; font-weight:bold;'>"+nick+"</span> 닉네임은 다른 사용자가 사용 중입니다.";
					$("#regNick").html(s);
					$("#nick").val("");
					$("#nick").focus();
				}
			}
		});	
	}
}

$(function() {
	$("#birth").datepicker({
		dateFormat: 'yy-mm-dd',
		changeYear: true,
		maxDate: "+0m+0w"
		});
});
	
function send() { // 버튼 눌렀을때 alert는 여기서 처리
	var f = document.memberForm;
	
	<c:if test="${mode != 'update'}">
	// 아이디 체크
	var id = $("#id").val();
	var url="<%=cp%>/mem/chkId.do";
	var query = "id="+id;
	var format = /^[a-z][a-z0-9_]{4,9}$/i;
	if(! id){
		var s = "<span style='color:red; font-weight:bold;'>아이디를 입력하세요.</span>";
		$("#spanId2").html(s);
		$("#id").focus();
		return false;
		
	} else if(! format.test($("#id").val())) {
		var s = "<span style='color:red; font-weight:bold;'>올바른 형식으로 입력하세요.</span>";
		$("#spanId2").html(s);
		$("#id").val("");
		$("#id").focus();
		return false;
	} else {
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data){
				var p = data.passed;
				if(p=="true") {
					var s = "<span style='color:blue; font-weight:bold;'>"+id+"</span> 아이디는 사용 가능 합니다.";
					$("#spanId2").html(s);
				} else{
					var s = "<span style='color:red; font-weight:bold;'>"+id+"</span> 아이디는 다른 사용자가 사용 중입니다.";
					$("#spanId2").html(s);
					$("#id").val("");
					$("#id").focus();
					return false;
				}
			}
		});	
	}
	</c:if>
	// 비밀번호 형식 체크
	var pwFormat = /^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i;
	
	if(! $("#pw").val()){
		var s = "<span style='color:red; font-weight:bold;'>비밀번호를 입력하세요.</span>";
		$("#chkPw").html(s);	
		$("#pw").focus();
		return false;
	}
		
	if(! pwFormat.test($("#pw").val())){
		var s = "<span style='color:red; font-weight:bold;'>영문자 + 숫자 or 특문 포함, 5~10자이내</span>";
		$("#chkPw").html(s);
		$("#pw").val("");
		$("#pw").focus();
		return false;
		
	} else {
		var s = "<span style='color:red; font-weight:bold;'></span>";
		$("#chkPw").html(s);
	}
	
	// 비밀번호 일치 체크
	if($("#pw").val() != $("#pw2").val()){
		var s = "<span style='color:red; font-weight:bold;'>비밀번호가 일치하지 않습니다.</span>";
		$("#equalPw").html(s);
		$("#pw2").val("");
		$("#pw2").focus();
		return false;
	} else {
		var s = "<span style='color:red; font-weight:bold;'></span>";
		$("#equalPw").html(s);
	}
	
	<c:if test="${mode != 'update'}">
	// 이름 형식 체크
	var nameFormat = /^[가-힣][가-힣\s]{1,4}$/;
	if(! $("#name").val()){
		var s = "<span style='color:red; font-weight:bold;'>이름을 입력하세요.</span>";
		$("#chkName").html(s);		
		$("#name").focus();
		return false;
	}
	
	if(! nameFormat.test($("#name").val())){
		var s = "<span style='color:red; font-weight:bold;'>이름은 한글 2~5글자</span>";
		$("#chkName").html(s);
		$("#name").val("");
		$("#name").focus();
		return false;
	} else {
		var s = "<span style='color:red; font-weight:bold;'></span>";
		$("#chkName").html(s);
	}
	</c:if>
	
	// 닉네임 체크
	var nick = $("#nick").val();
	var url="<%=cp%>/mem/chkNick.do";
	var query = "nick="+nick;
	var format = /^[가-힣0-9]{3,10}$/;
	if(! nick){
		var s = "<span style='color:red; font-weight:bold;'>닉네임을 입력하세요.</span>";
		$("#regNick").html(s);
		$("#nick").focus();
		return false;
		
	} else if(! format.test($("#nick").val())){
		var s = "<span style='color:red; font-weight:bold;'>올바른 형식으로 입력하세요.</span>";
		$("#regNick").html(s);
		$("#nick").val("");
		$("#nick").focus();
		return false;
		
	} else {
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data){
				var p = data.passed;
				if(p=="true") {
					var s = "<span style='color:blue; font-weight:bold;'>"+nick+"</span> 닉네임은 사용 가능 합니다.";
					$("#regNick").html(s);
				} else{
					var s = "<span style='color:red; font-weight:bold;'>"+nick+"</span> 닉네임은 다른 사용자가 사용 중입니다.";
					$("#regNick").html(s);
					$("#nick").val("");
					$("#nick").focus();
					return false;
				}
			}
		});	
	}	
	
	// 이메일 형식 체크
	var eFormat = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
	if(! $("#email").val()){
		var s = "<span style='color:red; font-weight:bold;'>E-mail을 입력하세요.</span>";
		$("#chkEmail").html(s);		
		$("#email").focus();
		return false;
	}
	
	if(! eFormat.test($("#email").val())){
		var s = "<span style='color:red; font-weight:bold;'>올바른 이메일을 입력하세요.</span>";
		$("#chkEmail").html(s);
		$("#email").val("");
		$("#email").focus();
		return false;
	} else {
		var s = "<span style='color:red; font-weight:bold;'></span>";
		$("#chkEmail").html(s);
	}
	
	// 휴대폰 형식 체크
	var telFormat=/^(01[016789])-[1-9]{1}[0-9]{2,3}-[0-9]{4}$/g;
	if(! $("#tel").val()){
		var s = "<span style='color:red; font-weight:bold;'>휴대폰 번호를 입력하세요.</span>";
		$("#chkTel").html(s);		
		$("#tel").focus();
		return false;
	}
	if(! telFormat.test($("#tel").val())){
		var s = "<span style='color:red; font-weight:bold;'>올바른 번호 형식으로 입력하세요.</span>";
		$("#chkTel").html(s);
		$("#tel").val("");
		$("#tel").focus();
		return false;
	} else {
		var s = "<span style='color:red; font-weight:bold;'></span>";
		$("#chkTel").html(s);
	}
	
	
	// 생일 체크
	if(! $("#birth").val()){
		var s = "<span style='color:red; font-weight:bold;'>생일을 선택하세요.</span>";
		$("#chkBirth").html(s);		
		$("#birth").focus();
		return false;
	} else{
		var s = "<span style='color:red; font-weight:bold;'></span>";
		$("#chkBirth").html(s);
	}
	
	var mode = "${mode}";
	if(mode == "created"){
		f.action = "<%=cp%>/mem/member_ok.do";
		alert("돼지들의 회원이 되셨습니다. 환영합니다.");
	} else if (mode == "update"){
		f.action = "<%=cp%>/mem/update_ok.do";
	}
	f.submit();
}
</script>

</head>
<body>
<article class="container">
        
	<div class="col-md-12">
		<div class="page-header">
    		<h1>${title} <small>Register</small></h1>
        </div>
	<form class="form-horizontal" method="post" name="memberForm" enctype="multipart/form-data">

	  	<div class="form-group">
        	<label class="col-sm-3 control-label">아이디</label>
           	<div class="col-sm-6">
            	<div class="input-group">
                 	<input type="text" class="form-control" name="id" id="id" 
                 	placeholder="5~10자 영숫자,첫글자는 영어만" value="${sessionScope.member.userId}"
                 	${mode=="update" ? "readonly='readonly' ":""}>

                 	<span class="input-group-btn">
                   		<button class="btn btn-warning" type="button" id="chkId" onclick="chkUserId();"
                   		 ${mode=="update" ? "disabled='disabled' ":""}>중복확인</button>
                 	</span>
                </div>
                <p class="help-block" id="spanId1"></p>
                <p class="help-block" id="spanId2"></p>
       		</div>
        </div>
        
        <div class="form-group">
        	<label class="col-sm-3 control-label">프로필 사진</label>
        	<div class="col-sm-6">
          		<input class="form-control" name="pic" id="pic" type="file">
       		</div>
        </div>
		
        <div class="form-group">
        	<label class="col-sm-3 control-label">비밀번호</label>
        	<div class="col-sm-6">
          		<input class="form-control" name="pw" id="pw" type="password" placeholder="영어 + 숫자 or 특수문자 포함 8자 이상 15자 이하">
       			<p class="help-block" id="chkPw"></p>
       		</div>
        </div>		
		
        <div class="form-group">
        	<label class="col-sm-3 control-label">비밀번호 확인</label>
        	<div class="col-sm-6">
          		<input class="form-control" name="pw2" id="pw2" type="password" placeholder="비밀번호 확인">
            	<p class="help-block" id="equalPw"></p>
            </div>
       	</div>
		
        <div class="form-group">
        		<label class="col-sm-3 control-label">이름</label>
        	<div class="col-sm-6">
            	<input class="form-control" name="name" id="name" type="text" value="${dto.name}"
            	${mode=="update" ? "readonly='readonly' ":""}>
     			<p class="help-block" id="chkName"></p>
     		</div>
        </div>
        <br>
        
        <div class="form-group">
        		<label class="col-sm-3 control-label">닉네임</label>
        	<div class="col-sm-6">
            	<div class="input-group">
                 	<input type="text" class="form-control" name="nick" id="nick" placeholder="한글 닉네임 3~10글자">
                 	<span class="input-group-btn">
                   		<button class="btn btn-warning" type="button" id="chkNick" onclick="chkUserNick();">중복확인</button>
                 	</span>
                </div>
                <p class="help-block" id="regNick"></p>
     		</div>
        </div>
        <br>
		<div class="form-group">
		<label class="col-sm-3 control-label">이메일</label>
        	<div class="col-sm-6">
        		<input class="form-control" name="email" id="email" type="email" placeholder="이메일을 입력하세요.">
        		<p class="help-block" id="chkEmail"></p>
        	</div>
        </div>
        <br>
		
		<div class="form-group">
		<label class="col-sm-3 control-label">휴대폰</label>
        	<div class="col-sm-6">
        		<input class="form-control" name="tel" id="tel" type="text" placeholder="01X-XXXX-XXXX">
	        	<p class="help-block" id="chkTel"></p>
        	</div>
        </div>
        <br>

	        <div class="form-group">
			<label class="col-sm-3 control-label" for="inputEmail">생 일</label>
	        	<div class="col-sm-6">
	        		<input class="form-control" name="birth" id="birth" type="text" value="${dto.birth }"
	        		${mode=="update" ? "readonly='readonly' disabled='disabled'":""}>
	        		<p class="help-block" id="chkBirth"></p>
	        	</div>
	        </div>

	  	<div class="form-group">
	     	<div class="col-sm-12 text-center">
	          	<button class="btn btn-warning " type="button" onclick="send();">${mode=="update"?"정 보 수 정":"회 원 가 입"}</button>
	          	<button class="btn btn-danger " type="button" onclick="javascript:history.go(-1);">${mode=="update"?"수 정 취 소":"가 입 취 소"}</button>
	     	</div>
	 	</div>
        
     	</form>
        <hr>
   	</div>
</article>

</body>
</html>