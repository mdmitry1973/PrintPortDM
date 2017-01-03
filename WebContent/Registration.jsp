<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="resources/bootstrap/ico/favicon.ico">

    <title>Signin Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="resources/bootstrap/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="resources/css/signin.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="resources/bootstrap/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    
  </head>

  <body>

    <div class="container">

      <form class="form-signin" id="formRegistration" onsubmit="return validateForm()" method="post" action="Registration" >
        <h2 class="form-signin-heading">Registration</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" name="inputEmail" class="form-control" placeholder="Email address" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
	    <input type="text" id="inputPassword" name="inputPassword" class="form-control" placeholder="Password" required>
	    <input type="text" id="inputPasswordConfirm" name="inputPasswordConfirm" class="form-control" onkeyup="checkPass(); return false;" placeholder="Confirm Password" required>
		<div class="alert" id="passWarning"  role="alert" style="display: none"> 
   			<!--   <a class="close" onclick="$('.alert').hide()">×</a>  -->
    		<strong>Warning!</strong> Passwords Do Not Match!  
		</div>
		<div class="alert" id="validateWarning"  role="alert" style="display: none"> 
   			<a class="close" onclick="$('.alert').hide()">×</a>
    		<strong>Warning!</strong> Passwords field must be not empty.  
		</div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Registration</button>
        
        
      </form>
    </div> <!-- /container -->
    


    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="resources/bootstrap/js/ie10-viewport-bug-workaround.js"></script>
 	<script >  
    function validateForm() {
    	var pass1, pass2 = "";
    	
    	pass1 = document.getElementById("inputPassword").value;
    	pass2 = document.getElementById("inputPasswordConfirm").value;
    	
    	if (pass1.length == 0 || pass2.length == 0)
    		{
    			$('#validateWarning').show();
    			return false;
    		}
    	
    	if (pass1 != pass2)
		{
			$('#validateWarning').show();
			return false;
		}
    	
    	return true;
	}
    
    function checkPass()
    {
    	var pass1, pass2 = "";
    	//alert('tt');
    	//$('#passWarning').show();
    	
    	pass1 = document.getElementById("inputPassword").value;
    	pass2 = document.getElementById("inputPasswordConfirm").value;
    	
    	if (pass2.length > 0 && pass1.startsWith(pass2) == false)
    		{
    			$('#passWarning').show();
    		}
    	else
    		{
    			$('#passWarning').hide();
    		}
    	
    }
	</script>
	
  </body>
</html>
