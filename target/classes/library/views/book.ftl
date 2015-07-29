<html">
<#--<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>-->
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css">
<body>
<!--login modal-->
<div>

<centre>
</br></br></br></br></br>
    <form id="create_book">
        <div>
            <input id= "isbn" name="isbn" placeholder="ISBN" type="number">
        </div>
        <div class="form-group">
            <input id="title" name="title" placeholder="Title" type="text">
        </div>
        <div>
            <button id="submit_button" onclick="submitfunc()">Create Book</button>

        </div>
    </form>
</centre>
</div>
<h3>Update</h3>
        <form id="update_form" action="/books/update" method="post">
            <input id="isbn" name="isbn" type="number"/>
            <input id="title" name="title" type="text"/>
            <button id="submit_button" type="submit" onclick="submitfunc()">Update</button>
        </form>
</div>
<div>
<h3>Delete</h3>
    <form id="delete_form" action="/books/delete" method="post">
        <input id="isbn" name="isbn" type="number"/>
        <button id="submit_button" type="submit">Delete</button>
    </form>

</div>
</div>
<#--<form id="login-form" method="post">-->
    <#--<input id="isbn" name="isbn" type="number" />-->
    <#--<input id=isbn" name="title" type="text"></input>-->
    <#--<button id="submit" type="submit" value="submit" onclick="submitfunc()">Submit</button>-->
<#--</form>-->
</body>

<script>
    function submitfunc()
    {
        var isbn=document.getElementById("isbn").value;
        var title=document.getElementById("title").value;
        alert("i got success in response");

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/books",
            contentType: "application/json",
            data: JSON.stringify({"isbn":isbn,"title":title})

        });

    }
</script>
</html>