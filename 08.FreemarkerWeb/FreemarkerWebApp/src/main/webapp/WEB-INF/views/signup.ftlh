<#include "/WEB-INF/views/prefixes.ftlh">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<form action="${context}/signup" method="post">
    <p>Username</p>
    <input type="text" name="username">
    <p>Email</p>
    <input type="email" name="email">
    <p>Password</p>
    <input type="password" name="password">
    <p>Confirm password</p>
    <input type="password" name="confPassword">
    <input type="submit" value="Sign up">
    <input type="hidden" value="${_csrf_token}" name="_csrf_token">
</form>
</body>
</html>