<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
    <h1>Users Home Page</h1>
    <table>
        <caption>Users</caption>

        <tr>
            <th>Userid</th>
            <th>First Name</th>
            <th>LastName</th>
        </tr>
        <#list Users as user>
        <tr>
            <th>${user.title}</th>
        </tr>
    </#list>
    </table>
</body>
<script>

</script>
</html>