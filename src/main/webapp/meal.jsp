<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Add meal</title>
    <style>
        body {
            font-family: Arial, serif;
        }

        form {
            width: 300px;
            margin: 20px;
        }

        input[type="text"],
        input[type="number"],
        input[type="datetime-local"] {
            width: 100%;
            margin-bottom: 10px;
            padding: 10px;
        }

        button,
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            cursor: pointer;
            width: 125px;
        }

        input[type="submit"]:hover {
            background-color: #3e8e41;
        }
    </style>
</head>
<body>
<h2>Add Meal</h2>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden"  name="id"
           value="<c:out value="${meal.id}" />"/>
    Date : <input
        type="datetime-local" name="dateTime"
        value="<c:out value="${meal.getDateTime().format(formatter)}"/>"/> <br/>
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Save"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>

</body>
</html>