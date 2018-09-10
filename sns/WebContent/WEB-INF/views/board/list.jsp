<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>전체 리스트 화면</title>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
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
/* Remove the navbar's default margin-bottom and rounded borders */
.navbar {
	margin-bottom: 0;
	border-radius: 0;
	height: 57px;
}

/* Set height of the grid so .sidenav can be 100% (adjust as needed) */
.row.content {
	height: 1300px;
}

/* Set gray background color and 100% height */
.sidenav {
	padding-top: 20px;
	background-color: white;
	height: 3000px;
}

ul {
	list-style: none;
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
	.row.content {
		height: auto;
	}
}

/****** MSG MODAL ******/
	.msgmodal-container {
	  padding: 30px;
	  max-width: 500px;
	  width: 100% !important;
	  background-color: #F7F7F7;
	  margin: 0 auto;
	  border-radius: 2px;
	  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	  overflow: hidden;
	  font-family: roboto;
	}
	
	.msgmodal-container h1 {
	  text-align: center;
	  font-size: 1.8em;
	  font-family: roboto;
	}
	
	.msgmodal-container input[type=submit] {
	  width: 100%;
	  display: block;
	  margin-bottom: 10px;
	  position: relative;
	}
	
	.msgmodal {
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
	
	.msgmodal-submit {
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
	
	.msgmodal-submit:hover {
	  /* border: 1px solid #2f5bb7; */
	  border: 0px;
	  text-shadow: 0 1px rgba(0,0,0,0.3);
	  background-color: #357ae8;
	  /* background-image: -webkit-gradient(linear, 0 0, 0 100%,   from(#4d90fe), to(#357ae8)); */
	}
	
	.msgmodal-container a {
	  text-decoration: none;
	  color: #666;
	  font-weight: 400;
	  text-align: center;
	  display: inline-block;
	  opacity: 0.6;
	  transition: opacity ease 0.5s;
	} 
	
</style>

<script type="text/javascript">
var pageNo=1;
var totalPage=1;

// 스크롤바 존재 여부 체크하는 function
function checkScrollBar() {
	var hContent=$("body").height();
	var hWindow=$(window).height();
	if(hContent>hWindow)
		return true;
	
	return false;
}

// 스크롤 사용해 페이징
$(function(){
	$(window).scroll(function() {
		if($(window).scrollTop()+100>=$(document).height()-$(window).height()) {
			if(pageNo<totalPage) {
				++pageNo;
				listPage(pageNo);
			}
		}
	});
});


$(function(){
	listPage(1);
});

function listPage(page) {
	var url="<%=cp%>/board/list.do";
	
	$.post(url, {pageNo:page}, function(data){
		printPage(data);
	}, "json");
}

// JSON으로 받아온 데이터 사용해 list 띄우기
function printPage(data) {
	var uid="${sessionScope.member.userId}";
	
	var dataCount=data.dataCount;
	var page=data.pageNo;
	totalPage=data.total_page;
	
	var out="";
	
	if(dataCount!=0) {
		for(var i=0; i<data.list.length; i++) {
			var num = data.list[i].num;
			var id = data.list[i].id;
			var nick = data.list[i].nick;
			var content = data.list[i].content;
			var imgName = data.list[i].imgName;
			var created = data.list[i].created;
			var place = data.list[i].place;
			
            out+="<table style='width: 95%; margin: 10px auto 30px; border-spacing: 0px; border-collapse: collapse;'>";
            out+="	<thead>";
            out+="		<tr height='35'>";
            out+="			<td width='50%'>";
            out+="				<span style='color: #EDA900; font-weight: 700; font-size: 15px;'>&nbsp;</span>";
            out+="			</td>";
            out+="			<td width='50%'>&nbsp;</td>";
            out+="		</tr>";
            out+="</thead>";
            out+="<tbody>";
            out+="<tr>"
            out+="<td colspan='2' align='center' style='border: 1px solid #cccccc; height: 35px; font-size: 19px; background: tomato;'>"+place;
            out+="</td>";
            out+="</tr>"
            
            out+="<tr>";
            out+="	<td colspan='2' align='center' style='border: 1px solid #cccccc; border-bottom:none; height: 550px;'>";
            out+="		<img src='<%=cp%>/uploads/photo/"+imgName+"' width='70%' height='85%' border='0'>";
            out+="</td>";
        	out+="</tr>";
        	
        	out+="<tr style='border-left: 1px solid #cccccc; border-right: 1px solid #cccccc;'>";
        	out+="<td colspan='2' align='center' height='40' style='padding-bottom: 15px;'>";
        	out+="<button type='button' class='btnLike btn btn-danger' onclick='sendBoardLike("+num+")'>";
        	out+="&nbsp;&nbsp;<span class='glyphicon glyphicon-heart'></span>&nbsp;&nbsp;";
        	out+="<span id='countBoardLike"+num+"'></span>";
        	out+="</button></td></tr>";
        	
			out+="    <tr height='50' bgcolor='#eeeeee' id='tr"+num+"'>";
			out+="      <td class='getId' width='50%' style='padding-left: 5px; border:1px solid #cccccc; border-right:none; font-size:19px;'>"+id+"</td>";
			out+="      <td width='50%' align='right' style='padding-right: 5px; border:1px solid #cccccc; border-left:none;'>" + created;
			if(uid==id) {
				out+=" | <a class='updateImg' data-num='"+num+"' data-page='"+page+"' data-screen='list'>수정</a>";
				out+=" | <a class='deleteImg' data-num='"+num+"' data-page='"+page+"' data-screen='list'>삭제</a></td>"
			} else if(uid=="admin"){
				out+=" | <a class='deleteImg' data-num='"+num+"' data-page='"+page+"' data-screen='list'>삭제</a></td>"
			} 
			else {
				out+=" | <a href='#'>신고</a></td>";
			}
			out+="    </tr>";
			
			out+="    <tr style='height: 100px; border: 1px solid #cccccc;'>";
			out+="      <td colspan='2' style='padding: 5px;' valign='top' border: 1px solid #cccccc;>"+content+"</td>";
			out+="    </tr>";

            out+="<tr>";
            out+="<td align='right' colspan='2'>";
            out+=" <button type='button' class='btn btnReply' style='padding:10px 15px; margin-top:10px;' data-num='"+num+"'>댓 글</button>";
            out+="</td>";
            out+="</tr>";

            out+="</tbody>";
        	out+="</table>";
        	// 좋아요 개수 띄우기
        	countBoardLike(num);
		}
	}
	// 지정된 영역에 화면 출력
	$("#listBody").empty();
	$("#listBody").append(out);
	
	if(! checkScrollBar() ) {
		if(pageNo < totalPage) {
			++pageNo;
			listPage(pageNo);
		}
	}
}

$(function(){
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
				if(state == "true" && screen == "list"){
					alert("게시물을 삭제했습니다.");
					$("#listBody").empty();
					listPage(1);
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

//좋아요 추가
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
			type : "post",
			url : url,
			data : {
				num : num
			},
			dataType : "json",
			success : function(data) {
				var count = data.countBoardLike;
				$("#countBoardLike"+num).html(count);
			},
			beforeSend : function(jqXHR) {
				jqXHR.setRequestHeader("AJAX", true);
			},
			error : function(jqXHR) {
				if (jqXHR.status == 403) {
					login();
					return;
				}
				console.log(jqXHR.responseText);
			}
		});
	}
	
// 댓글 대화상자
var num = 0;
$(function(){
	
	$("body").on("click",".btnReply",function(){
		num = $(this).attr("data-num");

		$("#replyDialog").dialog({
			title : "댓글",
			width : 700,
			height : 500,
			modal : true,
			resizeable : false
		});
		$("#replyDialog").trigger(listReplyPage(1));
	});
	
	// 게시물 댓글 등록
	$(".btnSendReply").click(function(){
		var content = $("#replyContent").val().trim();
		if(! content){
			$("#replyContent").focus();
			alert("댓글을 입력하세요!");
			return;
		}
		content = encodeURIComponent(content);
		
		var url = "<%=cp%>/board/insertReply.do";
		var query = "num="+num+"&content="+content;
		
		$.ajax({
			type:"post"
			,url:url
			,data:query
			,dataType:"json"
			,success:function(data) {
				$("#replyContent").val("");
				var state=data.state;
				if(state=="true") {
					listReplyPage(1);
				} 
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
	});
	
	// 댓글 삭제
	$("body").on("click",".deleteReply",function(){
		if(! confirm("게시물을 삭제하시겠습니까 ? "))
		    return;
		
		var replyNum = $(this).attr("data-replyNum");
		var page = $(this).attr("data-pageNo");
		
		var url = "<%=cp%>/board/deleteReply.do";
		var query = "replyNum="+replyNum+"&pageNo="+page;
		
		$.ajax({
			type : "post",
			url : url,
			data : query,
			dataType : "json",
			success:function(data) {
				listReplyPage(page);
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
		
	});
});

// 댓글 목록 띄우기
function listReplyPage(page){
	var url = "<%=cp%>/board/listReply.do";
	var query = "num="+num+"&pageNo="+page;
	
	$.ajax({
		type : "post",
		url : url,
		data : query,
		success:function(data) {
			$("#listReply").html(data);
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
// 검색(리스트)
function searchListPage(page){
	var searchValue = $("#search").val().trim();
	if(! searchValue){
		alert("검색어를 입력하세요!");
		return;
	}
	url = "<%=cp%>/board/searchList.do";
	page = 1;
	query = "searchValue="+searchValue+"&pageNo="+page;
	
	$.ajax({
		type : "post",
		url : url,
		data : query,
		dataType : "json",
		success : function(data){
			if(data.searchCount == 0){
				alert("검색 결과가 없습니다.");
				listPage(1);	
			}
			else if(data.searchCount > 0){
				alert(data.searchCount+"개의 게시물을 찾았습니다.");
				printSearchPage(data);	
			}
		}
	});	
}

function printSearchPage(data) {
	var uid="${sessionScope.member.userId}";
	
	var searchCount=data.searchCount;
	var page=data.pageNo;
	totalPage=data.total_page;
	
	var out="";
	
	if(searchCount != 0) {
		for(var j=0; j<data.searchList.length; j++) {
			var num = data.searchList[j].num;
			var id = data.searchList[j].id;
			var nick = data.searchList[j].nick;
			var content = data.searchList[j].content;
			var imgName = data.searchList[j].imgName;
			var created = data.searchList[j].created;
			var place = data.searchList[j].place;
			
            out+="<table style='width: 95%; margin: 10px auto 30px; border-spacing: 0px; border-collapse: collapse;'>";
            out+="	<thead>";
            out+="		<tr height='35'>";
            out+="			<td width='50%'>";
            out+="				<span style='color: #EDA900; font-weight: 700; font-size: 15px;'>&nbsp;</span>";
            out+="			</td>";
            out+="			<td width='50%'>&nbsp;</td>";
            out+="		</tr>";
            out+="</thead>";
            out+="<tbody>";
            out+="<tr>"
            out+="<td colspan='2' align='center' style='border: 1px solid #cccccc; height: 35px; font-size: 19px; background: tomato;'>"+place;
            out+="</td>";
            out+="</tr>"
            
            out+="<tr>";
            out+="	<td colspan='2' align='center' style='border: 1px solid #cccccc; border-bottom:none; height: 550px;'>";
            out+="		<img src='<%=cp%>/uploads/photo/"+imgName+"' width='70%' height='85%' border='0'>";
            out+="</td>";
        	out+="</tr>";
        	
        	out+="<tr style='border-left: 1px solid #cccccc; border-right: 1px solid #cccccc;'>";
        	out+="<td colspan='2' align='center' height='40' style='padding-bottom: 15px;'>";
        	out+="<button type='button' class='btnLike btn btn-danger' onclick='sendBoardLike("+num+")'>";
        	out+="&nbsp;&nbsp;<span class='glyphicon glyphicon-heart'></span>&nbsp;&nbsp;";
        	out+="<span id='countBoardLike"+num+"'></span>";
        	out+="</button></td></tr>";
        	
			out+="    <tr height='50' bgcolor='#eeeeee' id='tr"+num+"'>";
			out+="      <td class='getId' width='50%' style='padding-left: 5px; border:1px solid #cccccc; border-right:none; font-size:19px;'>"+id+"</td>";
			out+="      <td width='50%' align='right' style='padding-right: 5px; border:1px solid #cccccc; border-left:none;'>" + created;
			if(uid==id) {
				out+=" | <a class='updateImg' data-num='"+num+"' data-page='"+page+"' data-screen='list'>수정</a>";
				out+=" | <a class='deleteImg' data-num='"+num+"' data-page='"+page+"' data-screen='list'>삭제</a></td>"
			} else if(uid=="admin"){
				out+=" | <a class='deleteImg' data-num='"+num+"' data-page='"+page+"' data-screen='list'>삭제</a></td>"
			} 
			else {
				out+=" | <a href='#'>신고</a></td>";
			}
			out+="    </tr>";
			
			out+="    <tr style='height: 100px; border: 1px solid #cccccc;'>";
			out+="      <td colspan='2' style='padding: 5px;' valign='top' border: 1px solid #cccccc;>"+content+"</td>";
			out+="    </tr>";

            out+="<tr>";
            out+="<td align='right' colspan='2'>";
            out+=" <button type='button' class='btn btnReply' style='padding:10px 15px; margin-top:10px;' data-num='"+num+"'>댓 글</button>";
            out+="</td>";
            out+="</tr>";

            out+="</tbody>";
        	out+="</table>";
        	// 좋아요 개수 띄우기
        	countBoardLike(num);
		}
	}
	$("#listBody").empty();
	$("#listBody").append(out);
	
	if(! checkScrollBar() ) {
		if(pageNo < totalPage) {
			++pageNo;
			listPage(pageNo);
		}
	}	
}

// 아이디 클릭하면 메뉴 띄우기(프로필이동 or 메시지 보내기)
$(function(){
	$("body").on("click",".getId", function(event){
		var $x = event.pageX;
		var $y = event.pageY;
		var id = $(this).text();
		$("#menu").show();
		$("#goProfile").attr("data-id", id);
		$("#goSendMsg").attr("data-id", id);
		$("#menu").css({
			"top" : $y,
			"left" : $x,
			"position" : 'absolute'
		});
	});
	
	// 사용자 프로필로 이동
	$("#goProfile").click(function(){
		var id = $(this).attr("data-id");
		location.href="<%=cp%>/board/profile.do?id="+id;
	});
	// 메시지 모달 띄우기
	$("body").on("click","#goSendMsg", function(){
		var getMsgId = $(this).attr("data-id");
		var sendMsgId = "${sessionScope.member.userId}";
		url = "<%=cp%>/board/msg.do?getId="+getMsgId+"&sendId="+sendMsgId+"&tmp="+new Date().getTime();
		
		if(getMsgId == sendMsgId){
			alert("본인에게는 메시지를 보낼 수 없습니다!");
			return;
		}

		$("#myMsgModal .modal-body").load(url,function(){
			$("#myMsgModal").modal('show');
		});

	});
	
	$("body :not('#menu')").click(function(){
		$("#menu").hide();
	});
	
// 메시지 전송
$("body").on("click","#btnSendMsg", function(){
	var f = $("form[name=msgForm]");
	var title = $("#title").val();
	var message = $("#message").val();
	
	if(! title){
		alert("제목을 입력하세요!");
		return;
	}
	
	if(! message){
		alert("내용을 입력하세요!");
		return;
	}
	alert("메시지를 전송했습니다!")
	f.submit();
});

$("body").on("click","#btnCancelMsg", function(){
	$("#title").val('');
	$("#message").val('');
	$("#myMsgModal").modal('hide');
});
	
});


// 아이디 클릭하면 사용자 프로필로 이동
/*
$("body").on("click",".getId", function(){
	var id = $(this).text();
	location.href="<%=cp%>/board/profile.do?id="+id;
});
*/

</script>

</head>
<body>
	<form name="searchForm" method="post">
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</form>
	<div class="container-fluid text-center">
		<div class="row content">
			<div class="col-sm-2 sidenav">
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
			</div>

			<div class="col-sm-8 text-left">
				<div id="listGuest">
					<div id="listBody"></div>
				</div>
			</div>

			<div class="col-sm-2 sidenav">
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
			</div>
		</div>
	</div>
	<div id="replyDialog" style="display: none;">
		<div class="replyWrite">
                 <div style="clear: both;">
                 	<span style="font-weight: bold;">댓글 쓰기</span>
                 </div>
                 <div style="clear: both; padding-top: 10px;">
                       <textarea name="replyContent" id="replyContent" class="boxTF" rows="3" style="display:block; width: 100%; padding: 6px 12px; box-sizing:border-box; resize:none;" required="required"></textarea>
                  </div>
                  <div style="text-align: right; padding-top: 10px;">
                       <button type="button" id="btnSendReply" class="btn btn-info btnSendReply" style="padding:8px 25px;"> 등 록 </button>
                  </div>           
       	</div>
       	<div id="listReply"></div>
	</div>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	
	<div id="menu" style="display: none;">
		<ul>
			<li id="goProfile" style="background: #FFE400; width: 250px; height: 50px; border-bottom: 1px solid #FFBB00;"><p style="padding-top: 11px; font-size: 19px;">프로필 이동</p></li>
			<li id="goSendMsg" class="goSendMsg" style="background: #FFE400; width: 250px; height: 50px;" data-toggle="modal" data-target="#msg-modal">
			<p style="padding-top: 11px; font-size: 19px;">쪽지 보내기</p></li>
		</ul>
	</div>
	
	<!-- Modal -->
  	<div class="modal fade" id="myMsgModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  		<div class="modal-dialog">
   			<div class="modal-content">
      			<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
     			<h4 class="modal-title" id="myModalLabel" style="font-family: 나눔고딕, 맑은 고딕, sans-serif; font-weight: bold;">메 시 지</h4>
    		</div>
      	<div class="modal-body"></div>
	    </div>
	  </div>
	</div>
  
</body>
</html>