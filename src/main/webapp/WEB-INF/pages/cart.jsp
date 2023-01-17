<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <p>
    Cart ${cart}
  </p>
  <table>
    <thead>
      <tr>
        <td>Image</td>

        <td>
          Description
        </td>
        <td class="price">
          Price
        </td>
      </tr>
    </thead>
    <c:forEach var="item" items="${cart.items}">
      <tr>
        <td>
          <img src="${item.product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
          ${item.product.description}
        </td>
        <td class="price">
          <div>
            <a href="#popup${item.product.id}">
              <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </a>
          </div>
          <div id="popup${item.product.id}" class="overlay">
            <div class="popup">
              <h2>Price history</h2>
              <h1>${item.product.description}</h1>
              <a class="close" href="#">&times;</a>
              <div class="content">
                <c:forEach var="history" items="${item.product.priceHistoryList}">
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