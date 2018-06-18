<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <form id="loginForm" modelAttribute="login" action="login" method="post">
            <table align="center">
                <tr>
                    <td>
                        <label path="username">Username: </label>
                    </td>
                    <td>
                        <input path="username" name="username" id="username" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label path="password">Password:</label>
                    </td>
                    <td>
                        <input path="password" name="password" id="password" />
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td align="left">
                        <button id="login" name="login">Login</button>
                    </td>
                </tr>
                <tr></tr>
                <tr>
                    <td></td>
                    <td><a href="/">Home</a>
                    </td>
                </tr>
            </table>
        </form>
        <table align="center">
            <tr>
                <td style="font-style: italic; color: red;">${message!}</td>
            </tr>
        </table>
    </body>

</html>
