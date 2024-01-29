<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/Header.css">
<link
	href="https://hangeul.pstatic.net/hangeul_static/css/nanum-square-neo.css"
	rel="stylesheet">
<script src="/js/jquery-3.7.1.min.js"></script>
<title></title>
</head>

<body>
<header>
	<div class="header-container">
		<div class="menu">
			<img src="/image/icon/menu.png" class="icon" id="menubtn">
		</div>

		<div class="end-btn">
			
			<a href="#">
    			<img class="icon" id="notifyIcon" src="/image/icon/notify.png"> 
			</a>
			
			<a href="/api/chatlist/<%= session.getAttribute("memberIdx") %>">
    			<img class="icon" id="chatIcon" src="/image/icon/chat.png">
			</a>

			<button class="btn" id="mypagenbtn">마이페이지</button>
			<button class="btn" id="logoutbtn">로그아웃</button>
			<div class="language-selection">
				<select>
					<option value="ko">한국어</option>
					<option value="en">English</option>
				</select>
			</div>
		</div>
	</div>
</header>

	<div class="menu-content">
		<a href="/api/post">모든 게시글</a> 
		<a href="/api/map">지도 서비스</a> 
		<a href="/api/plan">여행 플래너</a> 
		<a href="/api/diningcost">지역별 외식비</a>
	</div>
	
	
	<button id="topBtn">
		<img src="/image/icon/topicon.png"> 
	</button>

<script>
$(document).ready(function () {
	function updateMenuContentPosition() { //메뉴위치
	    var menuOffset = $(".menu").offset(); 
	    $(".menu-content").css({'left': menuOffset.left }); 
	}
	
	$("#menubtn").on("click", function () { //메뉴열기
		updateMenuContentPosition();
	    $(".menu-content").slideToggle(); 
	});
	
	$(window).on('resize', function(){ //윈도우창 크기에 따라 변화
	    if($(".menu-content").is(":visible")) {
	        updateMenuContentPosition();
	    }

	});
	
	$("#mypagenbtn").on("click", function (event) {
	    window.location.href = "/api/mypage";
	});

    
    $("#logoutbtn").on("click", function () {
    	if (confirm("로그아웃 하시겠습니까?")){
    		window.location.href = "/";
    	}
    	else{}
      });
    

    let topBtn = document.getElementById("topBtn");

    function topFunction() {
        document.body.scrollTop = 0; // Safari 용
        document.documentElement.scrollTop = 0; // Chrome, Firefox, IE 및 Opera 용
    }

    topBtn.addEventListener("click", topFunction);

    window.onscroll = function() {
        if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
            topBtn.style.display = "block";
        } else {
            topBtn.style.display = "none";
        }
    };
    
  

    

    

    
    
    
  }); //ready

</script>





</body>
</html>