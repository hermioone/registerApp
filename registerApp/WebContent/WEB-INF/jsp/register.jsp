<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>

<script type="text/javascript">
	function validate() {
		var name = $("#username").val();
		var params = {"username":name};
		var flag = false;
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath }/validate.action",
			data: params,
		    async:false,
		    success:function(data) {
		    	flag = data.flag;
				var msg = data.message;
				var msgSpan = $("#msg");
				if(flag == false) {
					msgSpan.attr("class", "alert alert-danger");
					msgSpan.attr("role", "alert");
					msgSpan.text(msg);
				}else {
					msgSpan.removeAttr("class");
					msgSpan.removeAttr("role");
					msgSpan.text("");
				}	
		    }
		});
		return flag;
	}
	
	function validatePass() {
		var password = $("#password").val();
		var params = {"password":password};
		var flag = false;
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath }/validatePass.action",
			data: params,
		    async:false,
		    success:function(data) {
		    	flag = data.flag;
				var msg = data.message;
				var msgSpan = $("#msgPass");
				if(flag == false) {
					msgSpan.attr("class", "alert alert-danger");
					msgSpan.attr("role", "alert");
					msgSpan.text(msg);
				}else {
					msgSpan.removeAttr("class");
					msgSpan.removeAttr("role");
					msgSpan.text("");
				}	
		    }
		});
		return flag;
	}
	
	function submitForm() {
		var username_valid = validate();
		var password_valid = validatePass();
		if((username_valid == true) && (password_valid == true)) {
			document.getElementById("form").submit();    
		}
	}
</script>
</head>
<body>
	<div class="container">
		<h1 align="center">Please input username and password to register</h1>	
		<form id="form" action="${pageContext.request.contextPath }/register.action" method="post">
			<div class="form-group">
				<label for="username">Username</label> 
				<input type="text" class="form-control" id="username" name="username" placeholder="Username" onblur="validate()">
			</div>
			<h4 id="msg"></h4>
			<div class="form-group">
				<label for="password">Password</label> 
				<input type="password" class="form-control" id="password" name="password" placeholder="Password">
			</div>
			<h4 id="msgPass"></h4>
			<input type="button" class="btn btn-default" onclick="submitForm()" value="Submit">
		</form>
	</div>
</body>

</html>