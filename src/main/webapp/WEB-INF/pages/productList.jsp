<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message && empty param.errors}">
    <div class="success">
        ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty param.errors}">
    <div class="error">
      There was an error adding to cart: ${param.errors}
    </div>
  </c:if>
  <%--<p>${param.error}</p>--%>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <form method="post">
  <table>
    <thead>
      <tr>
        <td>Image</td>

        <td>
          Description
          <tags:sort sort="description" order="asc"/>
          <tags:sort sort="description" order="desc"/>
        </td>
        <td class="price">
          Price
          <tags:sort sort="price" order="asc"/>
          <tags:sort sort="price" order="desc"/>
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img src="${product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
          ${product.description}
        </td>
        <td>
          <fmt:formatNumber value="${item.quantity}" var="quantity"/>
          <c:set var="error" value="${errors[product.id]}"/>
          <input name="quantity" value="${not empty error? param.quantity: 1}" class= "quantity">
          <input type="hidden" name="productId" value="${product.id}"/>
          <c:if test="${not empty param.errors && param.id eq product.id}">
            <div class="error">
                ${param.errors}
            </div>
          </c:if>
        </td>
        <td class="price">
          <div>
            <a href="#popup${product.id}">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </a>
          </div>
          <div id="popup${product.id}" class="overlay">
            <div class="popup">
              <h2>Price history</h2>
              <h1>${product.description}</h1>
              <a class="close" href="#">&times;</a>
              <div class="content">
                <c:forEach var="history" items="${product.priceHistoryList}">
                  <p>${history.date} the price was <fmt:formatNumber value="${history.price}" type="currency" currencySymbol="&#36"/></p>
                </c:forEach>
              </div>
            </div>
          </div>
        </td>
        <td>
          <button formaction="${pageContext.servletContext.contextPath}/cart/addToCart/${product.id}">Add</button>
        </td>
      </tr>
    <form>
    </c:forEach>
  </table>
</tags:master>