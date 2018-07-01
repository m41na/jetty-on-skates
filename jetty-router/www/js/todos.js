let form  = document.getElementById("tasks_form");

//create new task
var createTask = function(){  
    let value = form.querySelector("input[name=task]").value;
    if(value){
        var xhr = new XMLHttpRequest();
        xhr.open("POST", '/app/todos', true);

        //Send the proper header information along with the request
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.onreadystatechange = function() {
            //Call a function when the state changes.
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                var task = JSON.parse(xhr.response);
                newElement(task);
            }
        };
        xhr.send("task="+value); 
    }
    else{
        alert("You need a value for the task name");
    }
};

var retrieveTasks = function(){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/app/todos/refresh', true);

    xhr.onload = function () {
      // Request finished. Do processing here.
    };

    xhr.send(null);
};

var updateName = function(onSuccess){
    let value = form.querySelector("input[name=task]").value;

    var xhr = new XMLHttpRequest();
    xhr.open("PUT", '/app/todos/name', true);

    //Send the proper header information along with the request
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function() {//Call a function when the state changes.
        if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            var task = JSON.parse(xhr.response);
            onSuccess(task);
        }
    };
    xhr.send(JSON.stringify("task="+value)); 
};

var updateDone = function(task, complete, onSuccess){
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", '/app/todos/done', true);

    //Send the proper header information along with the request
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function() {//Call a function when the state changes.
        if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            var task = JSON.parse(xhr.response);
            onSuccess(task);
        }
    };
    xhr.send("task="+task+"&complete="+complete); 
};

var deleteTask = function(name, onSuccess){
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", '/app/todos?name=' + name, true);

    xhr.onload = function () {
      // Request finished. Do processing here.
      onSuccess();
    };
    
    xhr.send(null); 
};

// Add a "checked" symbol when clicking on a list item
var list = document.querySelector('ul');
list.addEventListener('click', function (ev) {
    if (ev.target.tagName === 'LI') {
        let task = ev.target.dataset.name;
        updateDone(task, !ev.target.classList.contains('checked'), ()=> {
            ev.target.classList.toggle('checked');
        });
    }
}, false);

// Create a new list item when clicking on the "Add" button
function newElement(task) {
    var li = document.createElement("li");
    li.dataset.name = task.name;

    var t = document.createTextNode(task.name);
    li.appendChild(t);
    
    document.getElementById("myUL").appendChild(li);    
    document.querySelector('[name=task]').value = "";

    var span = document.createElement("SPAN");
    var txt = document.createTextNode("\u00D7");
    span.className = "close";
    span.appendChild(txt);
    li.appendChild(span);
    span.onclick = function () {
        var item = this.parentElement;
        deleteTask(item.dataset.name, ()=>{
            item.remove();
        });
    };
};

//on page load, initialize elements
(function(){
    // Create a "close" button and append it to each list item
    var myNodelist = document.getElementsByTagName("LI");
    for (let i = 0; i < myNodelist.length; i++) {
        var span = document.createElement("SPAN");
        var txt = document.createTextNode("\u00D7");
        span.className = "close";
        span.appendChild(txt);
        myNodelist[i].appendChild(span);
    }

    // Click on a close button to hide the current list item
    var close = document.getElementsByClassName("close");
    for (let i = 0; i < close.length; i++) {
        close[i].onclick = function () {
            var task = this.parentElement;
            deleteTask(task.dataset.name, ()=>{
                task.remove();
            });
        };
    }
})();
