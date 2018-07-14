<#macro basic>
<!DOCTYPE html>
<html>
    <head>
        <title>${page.title}</title>
        <meta charset="${page.charset}">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <#list page.styles as style>
            ${style}
        </#list>
    </head>
    <body>
        <div>
            <#nested/>
        </div>
        
        <#include "footer.ftl"/>
        
        <#list page.bscripts as script>
            ${script}
        </#list>
    </body>
</html>
</#macro>
