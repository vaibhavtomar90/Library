
<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<body>
    <h1>Authors</h1>
    <form>
        <button id="create-button" type="submit" onclick="showAuthors()">Show Authors</button>
    </form>



    <form id="create-author" action="/authors/create" method="post">
        <input id="authorid" name="authorid" type="number"/>
        <input id="firstname" name="firstname" type="text"/>
        <input id="lastname" name="lastname" type="text"/>
        <button id="create-button" type="submit">Create Author</button>
    </form>
    <form id="update-author" action="/authors/update" method="post">
        <input id="authorid" name="authorid" type="number"/>
        <input id="firstname" name="firstname" type="text"/>
        <input id="lastname" name="lastname" type="text"/>
        <button id="create-button" type="submit">Update Author</button>
    </form>
    <form id="delete-author" action="/authors/delete" method="post">
        <input id="authorid" name="authorid" type="number"/>
        <button id="create-button" type="submit">Delete Author</button>
    </form>
</body>
<script>
    function showAuthors()
    {
        document.getElementById("abc").innerHTML ="reached end";
    //alert("i came here");
        $.getJSON( "http://localhost:8080/authors/list", function( data ) {
            window.alert(data.length);
//            for(var i=0;i<data.length;i++) {
//
//                var items = [];
//                $.each(data[i], function (key, val) {
//                    items.push("<li id='" + key + "'>" + val + "</li>");
//                });
//            }

        });

//        $( "</ul>", {
//            "class": "my-new-list",
//            html: items.join( "" )
//        }).appendTo( "#selectQuery" );
//        //document.getElementById("abc").innerHTML ="reached end";
    }

</script>
</html>