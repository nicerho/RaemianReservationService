<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="../css/page_default.css?v=45">
<link rel="stylesheet" href="../css/admin_css.css?v=45">
<link rel="stylesheet" href="../css/notice.css?v=45">
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap">
<title>관리자 페이지</title>
<script>
</script>
</head>
<body>
<nav>
<div class="menusize">
    <ul id="menus">
        <li class="topmenu1"><a href="../adminMain" class="atag">ADMINISTRATOR</a></li>
        <li class="topmenu2"><a href="../infoPage" class="atag">환경설정</a></li>
        <li class="topmenu2"><a href="../adminMember" class="atag">회원관리</a></li>
        <li class="topmenu2"><a href="../noticeConfig" class="atag">공지사항 관리</a></li>
        <li class="topmenu2"><a href="../faqPage" class="atag">FAQ</a></li>
        <li class="topmenu2"><a href="../config" class="atag">관리자현황</a></li>
        <li class="topmenu2"><a href="../reserveCheck" class="atag">예약현황</a></li>
        <li class="topmenu3">${loginAdmin.getAname()}님 환영합니다  <a href="./logout" class="atag">[로그아웃]</a></li>
    </ul>
 </div>
<div class="menuline"></div>
</nav>
<!-- 공지사항 등록 시작 -->
<main class="page_main">
<section class="page_section">
<div class="listbody">
    <div class="protitle">공지사항 확인</div>
    <div class="procho">
       <section class="data_listsview">
       <ol>
       <li>공지제목</li>
       <li class="text_in" >${notice.getCtitle()}</li>
       <li>글쓴이</li>
       <li class="text_in">${notice.getCwriter()}</li> 
       <li style="height:520px;">내용</li>
       <li style="height:520px; padding-top: 10px;">
       <div class="text_in2">${notice.getCsection()}</div>
       </li>       
       <li>첨부파일</li>
       <li class="text_in" ><span id="file" style="cursor:pointer;">${notice.getCfilename()}</span></li>   
       </ol>
       <span class="notice_btns">
       <input type="button" value="목록" class="meno_btn2" onclick="location.href='../noticeConfig'"></span>
       </section>
    </div>
</div> 
</section>
</main>
<footer>
<%@include file="./footer.jsp" %> 
</footer>
</body>
<script>
document.querySelector("#file").addEventListener("click",function(){
	location.href="./download/"+${notice.getCno()}
})
</script>
</html>