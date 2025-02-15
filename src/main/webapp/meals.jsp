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
    <c:if test="${not empty meals}">
        <c:forEach var="meal" items="${meals}">
            <tr class="<c:choose>
             <c:when test="${meal.excess}">red</c:when>
             <c:when test="${not meal.excess}">green</c:when>
        </c:choose>">
                <td><c:out value="${meal.getDateTime().format(formatter)}"/></td>
                <td><c:out value="${meal.getDescription()}"/></td>
                <td><c:out value="${meal.getCalories()}"/></td>
                <td><p><a href="mealsedit?action=edit&id=${meal.id}">Update</a></p></td>
                <td><p><a href="mealsedit?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </c:if>

</table>
<p><a href="mealsedit?action=insert">
    <button>Add Meal</button>
</a></p>
</body>
</html>