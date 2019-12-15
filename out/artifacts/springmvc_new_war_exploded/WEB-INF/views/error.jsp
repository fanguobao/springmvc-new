<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" isELIgnored="false" %>
<html>
<head>
    <title>信息反馈</title>
</head>
<body>
<h1>错误Error</h1>
错误信息：<%= ((Exception)request.getAttribute("exception")).getMessage() %>
</body>
</html>