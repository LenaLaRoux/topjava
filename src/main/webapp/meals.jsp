<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Meals</title>
    <style>
        body {
            font-family: Arial, serif;
        }

        table {
            border-collapse: collapse;
            width: 700px;
            margin-top: 20px;
        }

        th, td {
            border: 2px solid #808080;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            cursor: pointer;
            width: 125px;
        }

        .green {
            color: green;
        }

        .red {
            color: red;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>

<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
        <c:forEach var="meal" items="${meals}">
            <tr class="${meal.excess ? 'red' : 'green'}">
                <td>${meal.getDateTime().format(formatter)}</td>
                <td>${meal.getDescription()}</td>
                <td>${meal.getCalories()}</td>
                <td><p><a href="meals?action=edit&id=${meal.id}">Update</a></p></td>
                <td><p><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>

</table>
<p><a href="meals?action=add">
    <button>Add Meal</button>
</a></p>
</body>
</html>