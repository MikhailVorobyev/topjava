<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:useBean id="mealToList" type="java.util.List<ru.javawebinar.topjava.model.MealTo>" scope="request"/>
    <link rel="stylesheet" href="css/style.css">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<section>
    <h1 align="center">Meals</h1>
    <table border="1" cellpadding="8" cellspacing="0" align= "center">
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${mealToList}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm" var="formatedDate"/>
            <tr style="color: <%=meal.isExcess() ? "red" : "green"%>">
                <td>${formatedDate}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?id=${meal.id}&action=edit">Update</a></td>
                <td><a href="meals?id=${meal.id}&action=delete">Delete</a></td>
            </tr>
        </c:forEach>
        <td colspan="5" ><a href="meals?action=edit">Create new meal</a></td>
    </table>
</section>
</body>
</html>