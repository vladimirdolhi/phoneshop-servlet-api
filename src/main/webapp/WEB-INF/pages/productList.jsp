<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
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
        <%--<td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}/priceHistory">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
        </td>--%>
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
      </tr>
    </c:forEach>
  </table>
</tags:master>