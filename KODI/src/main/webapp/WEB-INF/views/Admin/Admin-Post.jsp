<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/css/Admin.css">
<link rel="stylesheet" href="/css/AdminHeader.css">
<link
	href="https://hangeul.pstatic.net/hangeul_static/css/nanum-square-neo.css"
	rel="stylesheet">
<script src="/js/jquery-3.7.1.min.js"></script>
<title>KODI</title>
</head>

<script>
	$(document).ready(function(){
		let language = <%=session.getAttribute("adminLanguage")%>;

		if(language.value == "ko") {
			$("#selectLanguage").val("ko").prop("selected", true);
		} else {
			$("#selectLanguage").val("en").prop("selected", true);
			$("#adminbtn").text("Admin");
			$("#logoutbtn").text("Logout");
			$("#listallBtn").text("Post list");
			$("#memberlistBtn").text("Member list");
		}
		
		$("#selectLanguage").change(function() {
			$.ajax({
				url: "/api/admin/adminlanguage",
				data: {"language": $("#selectLanguage").val()},
				type: "post",
				success: function(response){
					location.reload();
				},
				error: function(request, e){
					alert("코드: " + request.status + "메시지: " + request.responseText + "오류: " + e);
				}
			});
	    });
	});
</script>

<body>

	<header>
		<div class="header-container">
			<div class="menu">
				<img src="/image/icon/menu.png" class="icon" id="menubtn">
			</div>

			<div class="end-btn">
				<button class="btn" id="adminbtn">관리자</button>
				<button class="btn" id="logoutbtn">로그아웃</button>
				<div class="language-selection">
					<select id="selectLanguage">
						<option id="ko" value="ko">한국어</option>
						<option id="en" value="en">English</option>
					</select>
				</div>
			</div>
		</div>
	</header>

	<div class="menu-content">
		<a href="/api/admin/allposts" id="listallBtn">전체글</a> 
		<a href="/api/admin/allmembers" id="memberlistBtn">회원목록</a>
	</div>

<div class="main">

	<div class="logo-container">
		<img src="/image/icon/logo.png" id="KoDi">
	</div>

	<div class="board-box">
		<div class="title">

			<div id="title">
				<span style="margin-left: 10px;">전체글</span>
				<form action="/api/adminsearch">
					<select id="searchselect" name="filter">
						<option value="게시글">게시글</option>
					</select> <input id="searchinput" name="question">
					<button type="submit"
						style="border: none; background: none; cursor: pointer;">
						<img src="/image/icon/search.png"
							style="margin-right: 10px; height: 20px; width: 20px;">
					</button>
				</form>
			</div>
		</div>

		<div id="board">
			<table>
				<thead>
					<tr>
						<th>
							<div class="tdDiv">작성자(이메일)</div>
						</th>
						<th>
							<div class="tdDiv">글제목</div>
						</th>
						<th>
							<div class="tdDiv">글내용</div>
						</th>
					</tr>
				</thead>

				<tbody id="postList">
					<c:forEach var="post" items="${posts}">
						<tr>
							<td>
								<div class="tdDiv">
									<c:forEach var="member" items="${members}">
										<c:if test="${post.memberIdx eq member.memberIdx}">
														${member.email}</c:if>
									</c:forEach>
								</div>
							</td>
							
							<td>
								<div class="tdDiv">${post.title}</div>
							</td>
							
							<td>
								<div class="tdDiv">${fn:substring(post.content, 0, 20)}${post.content.length() > 20 ? '...' :
												''}</div>
							</td>
							
							<td>
								<a class="viewBtn" data-post-idx="${post.postIdx}" href="/api/post/${post.postIdx}">보기</a>
							
								<a class="deleteBtn" data-post-idx="${post.postIdx}" href="/api/admin/deletepost/${post.postIdx}">삭제</a>
							</td>
						</tr>
						
					</c:forEach>
				</tbody>
			</table>
			
				
			<button id="topBtn">
				<img src="/image/icon/topicon.png"> 
			</button>
			
			<!--  <div id="pagination">
				    <c:if test="${currentPage > 1}">
				        <button class="pageBtn" onclick="loadPage(${currentPage - 1})">이전</button>
				    </c:if>
				
				    <c:forEach var="pageNum" begin="1" end="${totalPages}">
				        <button class="pageBtn ${pageNum == currentPage ? 'selected' : ''}" onclick="loadPage(${pageNum})">${pageNum}</button>
				    </c:forEach>
				
				    <c:if test="${currentPage < totalPages}">
				        <button class="pageBtn" onclick="loadPage(${currentPage + 1})">다음</button>
			    </c:if>
			</div> -->

		</div>


	</div>
</div>


<script src="/js/AdminScript.js"></script>



</body>
<%@ include file="/WEB-INF/views/Footer.jsp" %>
</html>