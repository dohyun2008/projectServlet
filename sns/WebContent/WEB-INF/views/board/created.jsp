<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>게시물 작성 화면</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>

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
    .row.content {height: 1300px;}
    
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
</style>

<script type="text/javascript">
// 등록(수정) 버튼
function sendOk(){
	var f = document.boardForm;
	var mode = "${mode}";
	
	if(! $("#content").val()){
		alert("내용을 입력하세요.");
		return;
	}
	if(mode=="created"){
		if(! $("input[type=file]").val()){
			alert("사진을 등록하세요.");
			return;
		}	
	}
	
	if(mode == "update"){
		f.action = "<%=cp%>/board/update_ok.do";
	} else if (mode == "created"){
		f.action = "<%=cp%>/board/created_ok.do";
	}
	
	f.submit();
}

<c:if test="${mode=='update'}">
function viewerImage(){
	var url = "<%=cp%>/uploads/photo/${dto.imgName}";
	var img = "<img src='"+url+"' width='570' height='450'>";
	$("#photoLayout").html(img);
	$("#photoDialog").dialog({
		title : "사진"
		,width : 600
		,height : 520
		,modal : true
		,resizable : false
	});
}
</c:if>

$(function(){
	$(".placeBtn").click(function(){
		window.open("<%=cp%>/mapTest.jsp", "width=800,height=600");
	});
});

</script>
</head>
<body>

<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-2 sidenav">
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
    </div>
    <div class="col-sm-8 text-left">
            <div>
            <div class="body-title">
            	<h1><span style="font-family: Webdings">2</span> PHOTO-UPLOAD </h1>
        	</div>
			<form name="boardForm" method="post" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">

			  <tr align="left" height="50" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">닉 네 임</td>
			      <td style="padding-left:10px;"> 
			      	${sessionScope.member.userNick}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			          <textarea id="content" name="content" rows="15" class="boxTA" style="width: 95%; resize: none;">${dto.content}</textarea>
			      </td>
			  </tr>
			  
			  <tr align="left" height="50" style="border-bottom: 1px solid #cccccc;">
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">이&nbsp;&nbsp;미&nbsp;&nbsp;지</td>
			      <td style="padding-left:10px;">
			          <input type="file" name="upload" accept="image/*" class="boxTF" size="53" style="height: 25px;">
			       </td>
			  </tr>
			  
			  <tr align="left" height="50" style="border-bottom: 1px solid #cccccc;">
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">위&nbsp;&nbsp;치</td>
			      <td style="padding-left:10px;">
			          <button type="button" id="placeBtn" class="placeBtn btn btn-default btn-sm" data-toggle="modal" data-target="#myModal">위치 검색</button>
			          <input type="text" readonly="readonly" id="p_input" name="p_input">
			       </td>
			  </tr>
			  
			  <c:if test="${mode=='update'}">
			  	 <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
				     <td width="100" bgcolor="#eeeeee" style="text-align: center;">등 록 이 미 지</td>
				     <td style="padding-left:10px;">
				     	<img src="<%=cp%>/uploads/photo/${dto.imgName}"
				     	width="30" height="30" border="0"
				     	onclick="viewerImage();"
				     	style="cursor: pointer;">
				     </td>
			  	 </tr>
			  </c:if>
			  
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			      
			      	<input type="hidden" name="screen" value="${screen}">
			      	
					<c:if test="${mode=='update'}">
			      		<input type="hidden" name="num" value="${dto.num}">
			      		<input type="hidden" name="imgName" value="${dto.imgName}">
			      	</c:if>

			        <button type="button" class="btn btn-success" onclick="sendOk();">${mode=='update'?'수정하기':'등록하기'}</button>
			        <button type="reset" class="btn btn-info">다시입력</button>
			        <c:if test="${screen == 'list'}">
			        	<button type="button" class="btn btn-warning" onclick="javascript:location.href='<%=cp%>/board/main.do';">${mode=='update'?'수정취소':'등록취소'}</button>
					</c:if>
					
					<c:if test="${screen == 'profile'}">
			        	<button type="button" class="btn btn-warning" onclick="javascript:location.href='<%=cp%>/board/profile.do?id=${sessionScope.member.userId}';">${mode=='update'?'수정취소':'등록취소'}</button>
					</c:if>
				  </td>
			    </tr>
			  </table>
			</form>
        </div>
    </div>
    <div class="col-sm-2 sidenav">
    	 <p>&nbsp;</p>
    	 <p>&nbsp;</p>
    	 <p>&nbsp;</p>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
<div id="photoDialog">
	<div id="photoLayout"></div>
</div>

 <!-- Modal -->
  <div class="modal fade" id="myModal2" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">PHOTO</h4>
        </div>
        <div id="place-modal" class="modal-body place-modal">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
</body>
</html>