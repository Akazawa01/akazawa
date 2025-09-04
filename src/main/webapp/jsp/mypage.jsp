<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
isErrorPage="true" %>
<c:url val= "employeemenuUrl" valeu="/employee_menu" >
<h1>マイページ</h1>

${user.username}

<!--<a class="aaaaa" href="<c:url value='/attendance'/>">従業員メニュー</a>-->
<a class="aaaaa" var="employeemenuUrl" href="/employee_menu.java">従業員メニュー</a>




