<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>

<div class="container-fluid text-center">
	<div class="row content">
		<div class="col-sm-8 text-left">
			<div>
				<table
					style='width: 535px; margin: 10px auto 30px; border-spacing: 0px; border-collapse: collapse;'>
					<thead>
						<tr>
							<td colspan='2' align='center'
								style='border: 1px solid #cccccc; height: 35px; font-size: 19px; background: tomato;'>
								${dto.place }</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan='2' align='center'
								style='border: 1px solid #cccccc; border-bottom: none; height: 400px;'><img
								src="<%=cp%>/uploads/photo/${dto.imgName}" width='70%'
								height='85%' border='0'></td>
						</tr>

						<tr
							style="border-left: 1px solid #cccccc; border-right: 1px solid #cccccc;">
							<td colspan="2" align="center" height="40"
								style="padding-bottom: 15px;">
								<button type='button' class='btnLike btn btn-danger'
									onclick="sendBoardLike('${dto.num}')">
									&nbsp;&nbsp;<span class="glyphicon glyphicon-heart"></span>&nbsp;&nbsp;
									<span id="countBoardLike">${countBoardLike}</span>
								</button>
							</td>
						</tr>

						<tr height='50' bgcolor='#eeeeee' id='tr${dto.num }'>
							<td id='getId' width='20%'
								style='padding-left: 5px; border: 1px solid #cccccc; border-right: none; font-size: 19px;'>${dto.id}</td>
							<c:if test="${dto.id == sessionScope.member.userId}">
								<td width='50%' align='right'
									style='padding-right: 5px; border: 1px solid #cccccc; border-left: none;'>
									${dto.created} | <a class='updateImg' data-num="${dto.num }"
									data-screen='profile'>수정</a> | <a class='deleteImg'
									data-num="${dto.num }" data-screen='profile'>삭제</a>
								</td>
							</c:if>

							<c:if test="${dto.id != sessionScope.member.userId}">
								<td width='50%' align='right'
									style='padding-right: 5px; border: 1px solid #cccccc; border-left: none;'>
									${dto.created} | <a href='#'>신고</a>
								</td>
							</c:if>
						</tr>

						<tr style='height: 150px; border: 1px solid #cccccc;'>
							<td colspan='2' style='padding: 5px; border: 1px solid #CCCCCC;'
								valign='top'>${dto.content}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

	</div>
</div>