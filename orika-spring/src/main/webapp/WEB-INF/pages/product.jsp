<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Product</h2>

<c:if test="${not empty product}">

    <p>${product.id}: ${product.name}</p>

</c:if>
</body>
</html>