<#import "/themes/basic/basic.ftl" as layout>
<@layout.basic>
    <#include "/themes/basic/header.ftl"/>
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
</@layout.basic>
