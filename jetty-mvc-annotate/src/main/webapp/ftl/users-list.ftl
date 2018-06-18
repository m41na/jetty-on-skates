<!DOCTYPE html>
<html>
    <head>
        <title>Users List</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div id="header">
            <h2>Users List - freemarker</h2>
        </div>
        
        <div id="content">
            <table class="datatable">
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Email Addr</th>
                </tr>
                </thead>
                <tbody>
                    <#list model['users'] as user>
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </body>
</html>
