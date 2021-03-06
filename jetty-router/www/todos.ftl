<#import "layout.ftl" as layout>
<@layout.basic>
    <div class="wrapper">
        <div id="myDIV" class="header">
            <h2>My To Do List</h2>
            <form id="tasks_form">
                <input type="text" name="task" placeholder="Title...">
                <span onclick="createTask()" class="addBtn">Add</span>
            </form>
        </div>

        <ul id="myUL">
            <#list tasks as task>
            <li data-name="${ task.name }" <#if task.completed>class="checked"</#if>>${ task.name }</li>
            </#list>
        </ul> 
        </ul> 
    </div>    
    
    <script type="text/javascript" src="/js/todos.js"></script>
</@layout.basic>
