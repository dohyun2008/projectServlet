<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();
%>


<div class="container-fluid bg-3 text-center">    
  <h3>Photo</h3><br>
	<c:forEach var="dto" items="${list}" varStatus="status">
		<c:if test="${status.index==0}"><c:out value="<div class='row'>" escapeXml="false"/></c:if>
		<c:if test="${status.index != 0 && status.index%4 == 0}">
			<c:out value="</div><div class='row'>" escapeXml="false"/>
		</c:if>
	
	<div class="col-sm-3" style="height: 100%;">
		<img src="<%=cp%>/uploads/photo/${dto.imgName}" id="showImg" class="img-responsive showImg" 
		style="width:100%; height: 100%;" alt="Image" data-num="${dto.num}" data-toggle="modal" data-target="#myModal">
   	</div>
 	</c:forEach>

 	
 	<c:set var="n" value="${list.size()}"/>
 		
			<c:forEach var="i" begin="${n+1}" end="8" step="1">
			    <c:if test="${(i-1)%4 == 0}">
			      <c:out value="</div><div class='row'>" escapeXml="false"/>
		        </c:if>
				<div class="col-sm-3" style="height: 100%;">
				      <img src="<%=cp%>/resources/img/no-image.png" class="img-responsive" style="width:100%; height: 100%;" alt="Image">
				</div>
			</c:forEach>
      <c:out value="</div>" escapeXml="false"/>
		
 		${paging}

</div><br>