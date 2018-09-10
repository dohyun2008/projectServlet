<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>개인 프로필 화면</title>

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
	
  <style>
    /* Remove the navbar's default margin-bottom and rounded borders */ 
    .navbar {
      margin-bottom: 0;
      border-radius: 0;
      height: 57px;
    }
    
    /* Add a gray background color and some padding to the footer */
    footer {
      background-color: #f2f2f2;
      color: white;
      padding: 25px;
    }
  </style>
  
<script type="text/javascript">
$(function(){
	var userId = "${dto.id}";
	var sessionId = "${sessionScope.member.userId}";
	
	if(userId == sessionId){
		$("#myNavbar2").hide();
	} else if(userId != sessionId){
		$("#myNavbar1").hide();
	}
});

$(function(){
	listPage(1);
});

// 사용자 게시물 페이징
function listPage(page){
	var userId = "${dto.id}";
	
	var url = "<%=cp%>/board/profileList.do";
	var query = "id="+userId+"&pageNo="+page;
	
	$.ajax({
		type : "post",
		url : url,
		data : query,
		success : function(data) {
			$("#photoLayout").html(data);
		}
	});	
}

$(function(){
	// 동적으로 생성되는 요소에는 on 활용
	$("body").on("click",".showImg", function(){
		var num = $(this).attr("data-num");
		// GET방식으로 게시물 번호 전달
		var url = "<%=cp%>/board/article.do?num="+num;
		// load함수 활용해 modal에 선택한 게시물의 정보를 띄운다.
		$("#photo-modal").load(url,function(){
			$("#myModal").modal('show');
			countBoardLike(num);
		});
	});
	
	// 게시물 삭제
	$("body").on("click",".deleteImg",function(){
		if(! confirm("게시물을 삭제하시겠습니까?"))
			return;
		
		var num = $(this).attr("data-num");
		var screen = $(this).attr("data-screen");
		
		var url = "<%=cp%>/board/delete.do";
		var query = "num="+num+"&screen="+screen;
		
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data){
				var state = data.state;
				var screen = data.screen;
				if(state == "true" && screen == "profile"){
					alert("게시물을 삭제했습니다.");
					location.href = "<%=cp%>/board/profile.do?id=${sessionScope.member.userId}";
				}
			},
			beforeSend : function(jqXHR){
				jqXHR.setRequestHeader("AJAX",true);
			},
			error : function(jqXHR){
				if(jqXHR.status == 403){
					location.href="<%=cp%>/mem/login.do";
					return;
				}
				console.log(jqXHR.responseText);
			}
		});
		
	});
	
	// 게시물 수정 화면으로 이동
	$("body").on("click",".updateImg", function(){
		var num = $(this).attr("data-num");
		var screen = $(this).attr("data-screen");
		location.href="<%=cp%>/board/update.do?num="+num+"&screen="+screen;
	});
});

// 좋아요 추가
function sendBoardLike(num){
	var url = "<%=cp%>/board/insertBoardLike.do";
	$.ajax({
		type : "post",
		url : url,
		data : {num:num},
		dataType : "json",
		success : function(data){
			var state = data.state;
			if(state == "true"){
				countBoardLike(num);
			} else if(state == "false"){
				countBoardLike(num);
			}
		},
		beforeSend :function(jqXHR) {
	    	jqXHR.setRequestHeader("AJAX", true);
	    }
	    ,error:function(jqXHR) {
	    	if(jqXHR.status==403) {
	    		login();
	    		return;
	    	}
	    	console.log(jqXHR.responseText);
	    }
	});
}

// 좋아요 개수
function countBoardLike(num) {
	var url="<%=cp%>/board/countBoardLike.do";
	
	$.ajax({
		type:"post"
		,url:url
		,data:{num:num}
		,dataType:"json"
		,success:function(data) {
			var count=data.countBoardLike;
			$("#countBoardLike").html(count);
		}
	    ,beforeSend :function(jqXHR) {
	    	jqXHR.setRequestHeader("AJAX", true);
	    }
	    ,error:function(jqXHR) {
	    	if(jqXHR.status==403) {
	    		login();
	    		return;
	    	}
	    	console.log(jqXHR.responseText);
	    }
	});
}

$(function(){
	$("body").on("click","#btnFollow",function(){
		var title = $(this).html();
		var star = $(this).attr("data-id");
		var fan = '${sessionScope.member.userId}';
		
		url = "<%=cp%>/mem/follow.do";
		query = "fan="+fan+"&star="+star;
		
		// DB엔 들어가는데 버튼 모양 안바뀜
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success : function(data){
				var state = data.state;
				if(state == 'true'){
					$("#btnFollow").html('팔로잉');
					$("#btnFollow").attr('class', 'btn btn-info');
				} else if(state == 'false') {
					$("#btnFollow").html('팔로우');
					$("#btnFollow").attr('class', 'btn btn-danger');
				}
			},
			beforeSend :function(jqXHR) {
		    	jqXHR.setRequestHeader("AJAX", true);
		    }
		    ,error:function(jqXHR) {
		    	if(jqXHR.status==403) {
		    		login();
		    		return;
		    	}
		    	console.log(jqXHR.responseText);
		    }
		});
	});
});

</script>

</head>

<body>
<jsp:include page="/WEB-INF/views/layout/header3.jsp"></jsp:include>

<div class="jumbotron">
  <div class="container text-center">
    <c:if test="${dto.pic != null}">
    <img src="<%=cp%>/uploads/photo/${dto.pic}" alt="Image" style="width: 100px; height: 100px; border-radius: 50px;">
    </c:if>
    &nbsp;
    <div style="width:210px; height:65px; font-size: 50px; display: inline-block;">${dto.nick}</div>&nbsp;
    
    <c:if test="${dto.id != sessionScope.member.userId}">
    <p align="right" style="display: inline">
    <c:if test="${following == 'true'}">
	    <button type="button" id="btnFollow" name="btnFollow" class="btn btn-info btnFollow" style="padding: 10px 25px; margin-left: 10px;" 
	    data-id="${dto.id}">팔로잉</button>
    </c:if>
    
    <c:if test="${following != 'true'}">
	    <button type="button" id="btnFollow" name="btnFollow" class="btn btn-danger btnFollow" style="padding: 10px 25px; margin-left: 10px;" 
    	data-id="${dto.id}">팔로우</button>
    </c:if>
    </p>
    </c:if>
    
  </div>
</div>
  
<div class="container-fluid bg-3 text-center" id="photoLayout"></div>
<br>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>

 <!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header" style="height: 50px;">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">PHOTO</h4>
        </div>
        <div id="photo-modal" class="modal-body">
        </div>
        <div class="modal-footer" style="height: 55px;">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>

</body>
</html>