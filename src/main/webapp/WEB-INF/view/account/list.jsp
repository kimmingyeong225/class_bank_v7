<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- header.jsp  -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<!-- start of content.jsp(xxx.jsp)   -->
<div class="col-sm-8">
	<h2>계좌 목록(인증)</h2>
	<h5>Bank App에 오신걸 환영합니다</h5>
	
	<!-- 계좌가 없는 경우 있는 경우를 분리 -->
	<!-- 계좌가 있는 사용자 일 경우 반복문을 활용할 예정 -->
	
	<c:choose>
		<c:when test="${accountList != null}">
			<%-- 계좌 존재 : html 주석을 사용하면 오류 (jsp -> java) (jstl 태그 안에서) --%>
			<table class="table">
				<thead>
					<tr>
						<th>계좌 번호</th>
						<th>잔액</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="account" items="${accountList}">
						<tr>
							<th><a href="/account/detail/${account.id}?type=all">${account.number}</a></th>
							<th>${account.formatKoreanWon(account.balance)}</th>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<div class="jumbotron display-4">
				<h5>아직 생성된 계좌가 없습니다.</h5>
			</div>
		</c:otherwise>		
	</c:choose>

</div>
<!-- end of col-sm-8  -->
</div>
</div>
<!-- end of content.jsp(xxx.jsp)   -->

<!-- footer.jsp  -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>



