<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="printersItems" scope="request" type="java.util.List" />
<jsp:useBean id="jobHeadItems" scope="request" type="java.util.List" />
<jsp:useBean id="jobItems" scope="request" type="java.util.List" />
<jsp:useBean id="messageWarning" scope="request" type="java.lang.String" />
<jsp:useBean id="messageWarningType" scope="request" type="java.lang.String" />
<jsp:useBean id="messageWarningTitle" scope="request" type="java.lang.String" />
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="resources/ico/favicon.ico">

    <title>PrintPort Main</title>

    <!-- Bootstrap core CSS -->
    <link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<!-- Bootstrap theme -->
    <link href="resources/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="resources/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="resources/css/main.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="resources/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
   
    <!-- Fixed navbar -->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">PrintPort</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
           <!--  <li class="active"><a href="#">Home</a></li>--> 
           <% 
           		String jobRefStatus = " class=\"disabled\"";
			     if (printersItems.size() > 0)
			     {
			    	 
			    	 jobRefStatus = "";
			     }
			%>
            <li<%=jobRefStatus%>><a href="#contact" data-toggle="modal" data-target="#addJobModal" >Add job</a></li>
            <li><a href="#contact" data-toggle="modal" data-target="#addDeviceModal">Add Device</a></li>
            <!--
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li role="separator" class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href="#">Separated link</a></li>
                <li><a href="#">One more separated link</a></li>
              </ul>
            </li>
            --> 
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
    
    <% 	String messageWarningTypeTemp = messageWarningType;
	     if (messageWarningTypeTemp.length() == 0)
	     {
	    	 messageWarningTypeTemp = "alert";
	     }
	%>
	<div class="<%=messageWarningTypeTemp%>" id="validateWarning"  role="alert" style="display: none"> <a class="close" onclick="$('.alert').hide()">×</a>
    	<strong><%=messageWarningTitle%></strong><%=messageWarning%>  
	</div>
	<div class="table-responsive">
		<table class="table table-hover">
 			<thead>
      			<tr>
      				<% Iterator it = jobHeadItems.iterator();
	     	 		while (it.hasNext()){ String newsItem = (String) it.next();%>
	   				<th><%=newsItem%></th><% } %>
      		</tr>
    		</thead>
    		<tbody>
		     
		      <% 	Iterator it1 = jobItems.iterator();
	     	 		while (it1.hasNext())
	     	 		{ %>
	     	 			 <tr>
	     	 		<% 	List<String> newsItem = (List<String>) it1.next();
	     	 			Iterator it2 = newsItem.iterator();
	     	 			while (it2.hasNext())
		     	 		{
	     	 				String strItem = (String) it2.next();
	     	 			%>
	   						<th><%=strItem%></th>
	   			<% }%>
	     	 			</tr> 
	     	 	<%	} %>
   	 		</tbody>
		</table>
	</div>
     <!-- Add job -->
	<div class="modal fade" id="addJobModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    	<form id="formAddJob" method="post" action="AddJob" enctype="multipart/form-data">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">Add Job</h4>
		      </div>
		      <div class="modal-body">
		     	 <div class="form-group">
			      	<label class="btn btn-primary" for="file">
				   	 	<input id="file" name="file" type="file" style="display:none;" onchange="$('#upload-file-info').html($(this).val());">
				    	Browse file
					</label>
					<span class='label label-info' id="upload-file-info"></span>
			     </div>
		      </div>
		      <div class="form-group">
		      	<label for="selDevice">Printer</label>
		      	<select class="form-control" id="selDevice" name="selDevice">
			    <% 	Iterator it3 = printersItems.iterator();
				      while (it3.hasNext())
				      {
				         String newsItem = (String) it3.next();
				   %>
			        <option><%=newsItem%></option>
			        <% } %>
			     </select>
		       </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary" >Add</button>
		      </div>
	      </form>
	    </div>
	  </div>
	</div>
    
    <!-- Add devcie -->
	<div class="modal fade" id="addDeviceModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    
	    <form id="formAddDevice" method="post" onsubmit="return validateDevForm()" action="AddDevice" >
	    
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Add Device</h4>
	      </div>
	      <div class="modal-body">
		      <div class="form-group">
		      	<label for="selBrand">Brand</label>
		      	<select class="form-control" id="selBrand" name="selBrand">
			        <option>Epson</option>
			        <option>Canon</option>
			     </select>
			     <br>
			     <label for="selDevice">Model</label>
		      	<select class="form-control" id="selDevice" name="selDevice">
			        <option>SureColor SC-S50600</option>
			        <option>SureColor SC-S30600</option>
			     </select>
			     <br>
		       </div>
		       <!--  
		       <label for="inputName" class="sr-only">Device Name</label>
      			<input type="text" id="inputName" name="inputName" class="form-control" placeholder="Device Name" required autofocus>
      			<div class="alert" id="validateDevNameWarning"  role="alert" style="display: none"> 
   						<a class="close" onclick="$('.alert').hide()">×</a>
    					<strong>Warning!</strong> Device Name field must be not empty.  
				</div>
				-->
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="submit" class="btn btn-primary" name="btmType" value="addDevice" >Add</button>
	      </div>
	      
	      </form>
	    </div>
	  </div>
	</div>
    
    
     <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="resources/js/vendor/jquery.min.js"><\/script>')</script>
    <script src="resources/bootstrap/js/bootstrap.min.js"></script>
    <script src="resources/js/docs.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="resources/js/ie10-viewport-bug-workaround.js"></script>
    <script >  
    function validateDevForm() {
    	
    	
    	return true;
	}
    
    <% if (messageWarning.length() > 0){%>
		$('#validateWarning').show();
		<% }else{%>
    	$('#validateWarning').hide();
    	<%}%>
	</script>
  </body>
</html>
