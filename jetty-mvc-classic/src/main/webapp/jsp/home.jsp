<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
    </head>
    <body>
        <h3>Server date/time is : ${date}</h3>
        
        <table>
            <tr>
                <td>
                    <a href="login">Login</a>
                </td>
                <td>
                    <a href="register">Register</a>
                </td>
                <td>
                    <a href="users?start=0&size=5">User's List</a>
                </td>
            </tr>
        </table>
    </body>
</html>
