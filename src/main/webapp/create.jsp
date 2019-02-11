
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="css/style.css">
    <title>Create</title>
</head>
<body>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <h3>Добавление еды</h3>
        <hr>
        <dl>
            <dt>Дата/Время</dt>
            <dd><input type="datetime-local" name="date" value="${meal.dateTime}" required/></dd>
        </dl>
        <dl>
            <dt>Описание</dt>
            <dd><input type="text" placeholder="Название еды" name="description" size=24 value="${meal.description}"></dd>
        </dl>
        <dl>
            <dt>Калории</dt>
            <dd><input type="number" placeholder="Калорийность" name="calories" size=24 value="${meal.calories}"></dd>
        </dl>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>
