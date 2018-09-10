<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
	<title>시작 화면</title>
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
	
	
	<style>
	.container{
		min-height: 900px;
	}
    /* Remove the navbar's default margin-bottom and rounded borders */ 
    .navbar {
      margin-bottom: 0;
      border-radius: 0;
      height: 57px;
    }
    /* Set gray background color and 100% height */
    .sidenav {
      padding-top: 20px;
      background-color: #f1f1f1;
      height: 100%;
    }
    
    /* Set black background color, white text and some padding */
    footer {
      background-color: #555;
      color: white;
      padding: 15px;
    }
    
    /* On small screens, set height to 'auto' for sidenav and grid */
    @media screen and (max-width: 767px) {
      .sidenav {
        height: auto;
        padding: 15px;
      }
      .row.content {height:auto;} 
    }
    
    .start{
    	margin-top: 90px;
    }
    
    /****** LOGIN MODAL ******/
	.loginmodal-container {
	  padding: 30px;
	  max-width: 350px;
	  width: 100% !important;
	  background-color: #F7F7F7;
	  margin: 0 auto;
	  border-radius: 2px;
	  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	  overflow: hidden;
	  font-family: roboto;
	}
	
	.loginmodal-container h1 {
	  text-align: center;
	  font-size: 1.8em;
	  font-family: roboto;
	}
	
	.loginmodal-container input[type=submit] {
	  width: 100%;
	  display: block;
	  margin-bottom: 10px;
	  position: relative;
	}
	
	.loginmodal-container input[type=text], input[type=password] {
	  height: 44px;
	  font-size: 16px;
	  width: 100%;
	  margin-bottom: 10px;
	  -webkit-appearance: none;
	  background: #fff;
	  border: 1px solid #d9d9d9;
	  border-top: 1px solid #c0c0c0;
	  /* border-radius: 2px; */
	  padding: 0 8px;
	  box-sizing: border-box;
	  -moz-box-sizing: border-box;
	}
	
	.loginmodal-container input[type=text]:hover, input[type=password]:hover {
	  border: 1px solid #b9b9b9;
	  border-top: 1px solid #a0a0a0;
	  -moz-box-shadow: inset 0 1px 2px rgba(0,0,0,0.1);
	  -webkit-box-shadow: inset 0 1px 2px rgba(0,0,0,0.1);
	  box-shadow: inset 0 1px 2px rgba(0,0,0,0.1);
	}
	
	.loginmodal {
	  text-align: center;
	  font-size: 14px;
	  font-family: 'Arial', sans-serif;
	  font-weight: 700;
	  height: 36px;
	  padding: 0 8px;
	/* border-radius: 3px; */
	/* -webkit-user-select: none;
	  user-select: none; */
	}
	
	.loginmodal-submit {
	  /* border: 1px solid #3079ed; */
	  border: 0px;
	  color: #fff;
	  text-shadow: 0 1px rgba(0,0,0,0.1); 
	  background-color: #4d90fe;
	  padding: 17px 0px;
	  font-family: roboto;
	  font-size: 14px;
	  /* background-image: -webkit-gradient(linear, 0 0, 0 100%,   from(#4d90fe), to(#4787ed)); */
	}
	
	.loginmodal-submit:hover {
	  /* border: 1px solid #2f5bb7; */
	  border: 0px;
	  text-shadow: 0 1px rgba(0,0,0,0.3);
	  background-color: #357ae8;
	  /* background-image: -webkit-gradient(linear, 0 0, 0 100%,   from(#4d90fe), to(#357ae8)); */
	}
	
	.loginmodal-container a {
	  text-decoration: none;
	  color: #666;
	  font-weight: 400;
	  text-align: center;
	  display: inline-block;
	  opacity: 0.6;
	  transition: opacity ease 0.5s;
	} 
	
	.login-help{
	  font-size: 12px;
	}
	</style>

<script type="text/javascript">
<%-- 	
function sendOk(){
	var f = document.loginForm;
	var userId = f.userId.value;
	var userPw = f.userPw.value;
	alert(userId);
	
	if(!userId || !userPw){
		alert("아이디와 패스워드 모두 입력하세요.")
		return false;
	} 
	else{
		var s = "${message}";
		alert("1번째:"+s+":");
		// 다시 체크해야함
		if(s != ""){
			alert("2번째"+s);
			return false;
		}	
	}

	f.action = "<%=cp%>/mem/login_ok.do";
	f.submit(); 
}
--%>
$(function(){
	$("#btnLogin").click(function(){
		var userId = $("#userId").val();
		var userPw = $("#userPw").val();
		
		var url = "<%=cp%>/mem/login_ok.do";
		var query = "userId="+userId+"&userPw="+userPw;
		
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data){
				var st = data.state;
				if(st == "true"){
					location.href="<%=cp%>/board/main.do";
				} else{
					alert("정보가 올바르지 않습니다. 다시 시도해주세요.");
					return;
				}
			},
			error : function(){
				console.log(e.responseText);
				alert("에러발생");
			}
		});
	});
});
</script>

</head>

<body>
<jsp:include page="/WEB-INF/views/layout/header2.jsp"></jsp:include>

<div class="container-fluid text-center">
    	<div class="container">
  			<h1>돼&nbsp;지&nbsp;들</h1><br>
  
  
			  <div id="myCarousel" class="carousel slide" data-ride="carousel">
			    <!-- Indicators -->
			    <ol class="carousel-indicators">
			    	<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			    	<li data-target="#myCarousel" data-slide-to="1"></li>
			    	<li data-target="#myCarousel" data-slide-to="2"></li>
			    </ol>
			
			    <!-- Wrapper for slides -->
			    <div class="carousel-inner">
			    	<div class="item active">
			        	<img src="<%=cp %>/resources/img/1.jpg" style="height:590px; width:100%;">
			      	</div>
			
				    <div class="item">
				        <img src="<%=cp %>/resources/img/2.jpg" style="height:590px; width:100%;"> 
				    </div>
			    
			        <div class="item">
			        	<img src="<%=cp %>/resources/img/3.jpg" style="height:590px; width:100%;">
			        </div>
			    </div>
			
			    <!-- Left and right controls -->
			    	<a class="left carousel-control" href="#myCarousel" data-slide="prev">
			      		<span class="glyphicon glyphicon-chevron-left"></span>
			      		<span class="sr-only">Previous</span>
			    	</a>
			    	<a class="right carousel-control" href="#myCarousel" data-slide="next">
			      		<span class="glyphicon glyphicon-chevron-right"></span>
			      		<span class="sr-only">Next</span>
			   		</a>
			  </div>
			  
			  <div class="start">
			  	<button type="button" class="btn btn-warning btn-lg" data-toggle="modal" 
			  		data-target="#login-modal" style="font-size: 33px; padding: 25px; border-radius: 5px; margin-top:-45px;">시 작 하 기</button>
			  </div>
		</div>
</div>
		
		<div class="modal fade" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
			<div class="modal-dialog">
				<div class="loginmodal-container">
					<h1>LOGIN</h1><br>
						<form id="loginForm" name="loginForm" method="post" action="">
							<input type="text" id="userId" name="userId" placeholder="ID">
							<input type="password" id="userPw" name="userPw" placeholder="Password">
							<button type="button" id="btnLogin" class="btn btn-warning btn-lg" name="login" style="width: 290px;">LOGIN</button>
						</form>
								
					<div class="login-help">
						<a href="<%=cp%>/mem/member.do" style="font-size: 23px; text-align: center; width: 99%; margin-top: 5px;">회 원 가 입 </a>
					</div>
				</div>
			</div>
		</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>

</body>
</html>