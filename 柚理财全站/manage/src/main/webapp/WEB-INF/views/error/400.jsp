<%@page import="javax.validation.ConstraintViolation"%>
<%@page import="javax.validation.ConstraintViolationException"%>
<%@page import="org.springframework.validation.BindException"%>
<%@page import="org.springframework.validation.ObjectError"%>
<%@page import="org.springframework.validation.FieldError"%>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@page import="com.rd.ifaes.common.web.ServletUtils"%>
<%@page import="com.rd.ifaes.common.util.ExceptionUtils"%>
<%@page import="com.rd.ifaes.common.util.StringUtils"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%
response.setStatus(400);

// 获取异常类
Throwable ex = ExceptionUtils.getThrowable(request);

// 编译错误信息
StringBuilder sb = new StringBuilder("错误信息：\n");
if (ex != null) {
	if (ex instanceof BindException) {
		for (ObjectError e : ((BindException)ex).getGlobalErrors()){
			sb.append("☆" + e.getDefaultMessage() + "(" + e.getObjectName() + ")\n");
		}
		for (FieldError e : ((BindException)ex).getFieldErrors()){
			sb.append("☆" + e.getDefaultMessage() + "(" + e.getField() + ")\n");
		}
		LoggerFactory.getLogger("400.jsp").warn(ex.getMessage(), ex);
	}else if (ex instanceof ConstraintViolationException) {
		for (ConstraintViolation<?> v : ((ConstraintViolationException)ex).getConstraintViolations()) {
			sb.append("☆" + v.getMessage() + "(" + v.getPropertyPath() + ")\n");
		}
	} else {
		//sb.append(Exceptions.getStackTraceAsString(ex));
		sb.append("☆" + ex.getMessage());
	}
} else {
	sb.append("未知错误.\n\n");
}

// 如果是异步请求，则直接返回信息
if (ServletUtils.isAjaxRequest(request)) {
	out.print(sb);
}

// 输出异常信息页面
else {
%>

<!DOCTYPE html>
<html>
<head>
	<title>400 - 请求出错</title>
 
</head>
<body>
	<div class="container-fluid">
		<div class="page-header"><h1>参数有误,服务器无法解析.</h1></div>
		<div class="errorMessage">
			<%=StringUtils.toHtml(sb.toString())%> <br/>
		</div>
		<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a> &nbsp;
		<br/> <br/>
	</div>
</body>
</html>
<%
} out = pageContext.pushBody();
%>