<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
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
            border: 2px solid #000;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
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
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:if test="${not empty meals}">
        <c:forEach var="meal" items="${meals}">
            <tr class="<c:choose>
             <c:when test="${meal.excess}">red</c:when>
             <c:when test="${not meal.excess}">green</c:when>
        </c:choose>">
                <td><c:out value="${meal.getDateTime().format(formatter)}"/></td>
                <td><c:out value="${meal.getDescription()}"/></td>
                <td><c:out value="${meal.getCalories()}"/></td>
                <td>Update</td>
                <td>Delete</td>
            </tr>
        </c:forEach>
    </c:if>
</table>
</body>
</html>