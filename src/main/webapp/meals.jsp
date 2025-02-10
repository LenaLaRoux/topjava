<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        body {
            font-family: Arial, serif;
        }

        table {
            border-collapse: collapse;
            width: 100%;
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
    <%
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<MealTo> meals = (List<MealTo>) request.getAttribute("meals");
        if (meals != null) {
            for (MealTo meal : meals) {
                String textColor = meal.isExcess() ? "green" : "red";
    %>
    <tr class="<%= textColor%>">
        <td><%= meal.getDateTime().format(formatter) %></td>
        <td><%= meal.getDescription() %></td>
        <td><%= meal.getCalories() %></td>
        <td>Update</td>
        <td>Delete</td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>