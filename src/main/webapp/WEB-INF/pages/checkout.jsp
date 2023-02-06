<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
  <c:if test="${not empty param.message && empty errors}">
    <div class="success">
        ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty errors}">
    <div class="error">
      There was an error placing order
    </div>
  </c:if>
  <p>
    Cart ${cart}, total quantity ${order.totalQuantity}
  </p>
  <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
        </td>
        <td  class="quantity">
          Quantity
        </td>
        <td class="price">
          Price
        </td>
      </tr>
    </thead>
    <c:forEach var="item" items="${order.items}" varStatus="status">
      <tr>
        <td>
          <img src="${item.product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
          ${item.product.description}
        </td>
        <td class="quantity">
          <fmt:formatNumber value="${item.quantity}" var="quantity"/>
         ${item.quantity}
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
    <tr>
      <td></td>
      <td></td>
      <td class="quantity">Subtotal:</td>
      <td class="price">
          <p>
            <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${order.currency}"/>
          </p>
      </td>
    </tr>
    <tr>
      <td></td>
      <td></td>
      <td class="quantity">Delivery cost:</td>
      <td class="price">
        <p>
          <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${order.currency}"/>
        </p>
      </td>
    </tr>
    <tr>
      <td></td>
      <td></td>
      <td class="quantity">Total cost:</td>
      <td class="price">
        <p>
          <fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="${order.currency}"/>
        </p>
      </td>
    </tr>

  </table>
<h2>Your details</h2>
  <table>
    <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}"></tags:orderFormRow>
    <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}"></tags:orderFormRow>
    <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"></tags:orderFormRow>
    <tags:orderFormRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}"></tags:orderFormRow>
    <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"></tags:orderFormRow>
    <tr>
      <td>Payment method <span style="color: red"></span> </td>
      <td>
        <select name="paymentMethod">
          <option></option>
          <c:forEach var="paymentMethod" items="${paymentMethods}">
            <option value="${paymentMethod}" ${param.paymentMethod eq paymentMethod ? 'selected' : '' }>
                ${paymentMethod}</option>
          </c:forEach>
        </select>
        <c:set var="error" value="${errors['paymentMethod']}"/>
        <c:if test="${not empty error}">
          <div class="error">
              ${error}
          </div>
        </c:if>
      </td>
    </tr>


  </table>
    <p>
      <button>Place order</button>
    </p>
  </form>


  </form>
</tags:master>