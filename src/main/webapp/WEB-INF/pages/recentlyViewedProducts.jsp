<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:if test="${not empty viewedProducts}">
    <h2>Viewed Products</h2>
</c:if>
<table>
    <tr>
        <c:forEach var="product" items="${viewedProducts.recentlyViewedProducts}">
            <td>
                <img src="${product.imageUrl}">
                <br>
                <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a>
                <br>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </c:forEach>
    </tr>
</table>