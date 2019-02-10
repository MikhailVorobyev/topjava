<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:useBean id="mealsList" type="java.util.List<ru.javawebinar.topjava.model.MealTo>" scope="request"/>
    <title>Meals</title>
</head>
<body>
<section>
    <h1 align="center">Meals</h1>
    <table border="1" cellpadding="8" cellspacing="0" align="center">
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
        </tr>
        <c:forEach items="${mealsList}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm" var="formatedDate"/>
            <tr style="color: <%=meal.isExcess() ? "red" : "green"%>">
                <td>${formatedDate}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>