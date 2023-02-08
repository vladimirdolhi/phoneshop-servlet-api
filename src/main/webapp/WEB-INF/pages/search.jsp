<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced search">
  <h2>
    Advanced search
  </h2>
  <c:if test="${not empty requestScope.message && empty requestScope.errors}">
    <div class="success">
        ${requestScope.message}
    </div>
  </c:if>
  <c:if test="${not empty requestScope.errors}">
    <div class="error">
      There was an error searching: ${param.errors}
    </div>
  </c:if>
  <form action="${pageContext.servletContext.contextPath}/search">
    <label>
      <input name="description" value="${param.description}">

      <select name="searchOption">
        <c:forEach var="searchOption" items="${searchOptions}">
          <option name="${searchOption}"} ${param.searchOption eq searchOption ? 'selected' : '' }> ${searchOption}</option>
        </c:forEach>
      </select>
    </label>
    <br>

    <label>
      <c:set var="error" value="${errors['minPrice']}"/>
      Min price: <input type="text" name="minPrice" value="${param['minPrice']}">
      <c:if test="${not empty error}">
        <div class="error">
            ${error}
        </div>
      </c:if>
    </label>

    <br>
    <label>
      <c:set var="error" value="${errors['maxPrice']}"/>
      Max price: <input type="text" name="maxPrice" value="${param['maxPrice']}">
      <c:if test="${not empty error}">
        <div class="error">
            ${error}
        </div>
      </c:if>
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