<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced search">
  <h2>
    Advanced search
  </h2>
  <%--<c:if test="${not empty param.message && empty param.errors}">
    <div class="success">
        ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty param.errors}">
    <div class="error">
      There was an error adding to cart: ${param.errors}
    </div>
  </c:if>--%>
  <form action="${pageContext.servletContext.contextPath}/search">
    <label>
      <input name="description" value="${param.query}">
      <%--<select name="searchOption">
        <option></option>
        <c:forEach var="searchOption" items="${searchOptions}">
          <option value="${searchOption}" ${param.searchOption eq searchOption ? 'selected' : '' }>
              ${searchOption}</option>
        </c:forEach>
      </select>--%>

      <select name="searchOption">
        <option name="ALL_WORDS">ALL_WORDS</option>
        <option name="ANY_WORDS">ANY_WORDS</option>
      </select>
    </label>
    <br>

    <label>
      Min price: <input type="text" name="minPrice" value="${param.minPrice}">
    </label>

    <br>
    <label>
      Max price: <input type="text" name="maxPrice" value="${param.maxPrice}">
    </label>

    <button>Search</button>
  </form>

  <c:if test="${not empty requestScope.products and empty requestScope.errors}">
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
      <c:forEach var="product" items="${products}" varStatus="status">
        <tr>
          <td>
            <img src="${product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                ${product.description}
          </td>
          <td class="price">
            <div>
              <a href="#popup${product.id}">
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
              </a>
            </div>
            <div id="popup${product.id}" class="overlay">
              <div class="popup">
                <h2>Price history</h2>
                <h1>${product.description}</h1>
                <a class="close" href="#">&times;</a>
                <div class="content">
                  <c:forEach var="history" items="${product.priceHistoryList}">
                    <p>${history.date} the price was <fmt:formatNumber value="${history.price}"
                                                                       type="currency"
                                                                       currencySymbol="&#36"/></p>
                  </c:forEach>
                </div>
              </div>
            </div>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:if>

</tags:master>