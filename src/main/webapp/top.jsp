<%@page import="java.text.SimpleDateFormat"%>
<%@page import=" java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    Date nowDate = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    String date = simpleDateFormat.format(nowDate); 
    %>
<nav>
<div class="menusize">
    <ul id="menus">
        <li class="topmenu1"><a href="./adminMain" class="atag">ADMINISTRATOR</a></li>
        <li class="topmenu2"><a href="./infoPage" class="atag">환경설정</a></li>
        <li class="topmenu2"><a href="./adminMember" class="atag">회원관리</a></li>
        <li class="topmenu2"><a href="./noticeConfig" class="atag">공지사항 관리</a></li>
        <li class="topmenu2"><a href="./faqPage" class="atag">FAQ</a></li>
        <li class="topmenu2"><a href="./config" class="atag">관리자현황</a></li>
        <li class="topmenu2"><a href="./reserveCheck" class="atag">예약현황</a></li>
        <li class="topmenu3">${loginAdmin.getAname()}님 환영합니다  <a href="./logout" class="atag">[로그아웃]</a></li>
    </ul>
 </div>
<div class="menuline"></div>
</nav>