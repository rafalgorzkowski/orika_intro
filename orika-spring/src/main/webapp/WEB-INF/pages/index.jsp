<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Products</h2>

<c:if test="${not empty lists}">

    <ul>
        <c:forEach var="listValue" items="${lists}">
            <li>${listValue.id} - ${listValue.name}</li>
        </c:forEach>
    </ul>

</c:if>
</body>
</html>
