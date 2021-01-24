<#assign context="/web-spring-csrf">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<form action="${context}/login" method="post">
    <p>Email</p>
    <input type="email" name="email">
    <p>Password</p>
    <input type="password" name="password">
    <input type="submit" value="Sign in">
    <input type="hidden" value="${_csrf_token}" name="_csrf_token">
</form>
<a href="${context}/signup">Sign up</a>
</body>
</html>