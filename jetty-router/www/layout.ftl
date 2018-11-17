<#macro basic>
<!DOCTYPE html>
<html>
    <head>
        <title>${title}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link type="text/css" href="/app/css/todos.css" rel="stylesheet">
    </head>
    <body>
        <div>
            <#nested/>
        </div>
        
        <div id="footer">
		    &copy; Copyright 2018 by <a href="http://practicaldime.com/">PracticalDime</a>.
		</div>
    </body>
</html>
</#macro>
