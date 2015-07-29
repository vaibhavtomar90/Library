
<html>
<body background="/Assets/images/Home_Page_Pic.jpg">
<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css">
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Library</a>
        </div>
        <div>
            <ul class="nav navbar-nav">
                <li><a href="/home">Home</a></li>
                <li><a href="/books">Books</a></li>
                <li><a href="/users">Users</a></li>
                <li><a href="/authors">Authors</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
                <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            </ul>
        </div>
    </div>
</nav>



</div>


<!--login modal-->
<!--login modal-->

    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="text-center">Login</h1>
            </div>
            <div class="modal-body">
            </br></br></br>
                <form id="form-login" name="form-login" class="form col-md-12 center-block">
                    <div class="form-group">
                        <input id= "username" name="username" class="form-control input-lg" placeholder="Username" type="text">
                    </div>
                    <div class="form-group">
                        <input id="password" name="password" class="form-control input-lg" placeholder="Password" type="password">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" onclick="submitfunc()">Sign In</button>
                        <span class="pull-right"><a href="#">Register</a></span>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <div class="col-md-12">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    function submitfunc()
    {
        alert("ajx call");
        var username=document.getElementById("username").value;
        var password=document.getElementById("password").value;
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/users/login",
            contentType: "application/json",
            data: JSON.stringify({"username":username,"password":password})
        });

    }
</script>
</html>