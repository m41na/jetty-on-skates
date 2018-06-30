var ws;
var content = document.getElementById("content");
var users = {
    sender: '',
    sendto: ''
};

function startSession() {
    if (!ws) {
        clearContent();

        users.sender = document.querySelector("input[name=sender]").value;
        users.sendto = document.querySelector("input[name=sendto]").value;

        if (users.sender && users.sendto) {
            ws = new WebSocket(`ws://localhost:8080/events/${users.sender}/to/${users.sendto}`);

            //add event handlers
            ws.onopen = function () {
                content.append(createMessage("incoming", "server", dateTime(), "WebSocket opened"));
            };

            ws.onmessage = function (evt) {
                var msg = JSON.parse(evt.data);
                content.append(createMessage(msg.from === users.sender? "outgoing" : "incoming", msg.from, msg.time, msg.message));
            };

            ws.onclose = function () {
                var message = "WebSocket closed";
                content.append(createMessage("incoming", "server", dateTime(), message));
                ws = null;
            };

            ws.onerror = function (err) {
                content.append(createMessage("incoming", "server", dateTime(), err));
            };
        }
        else{
            alert('sender to recipient are required fields');
        }
    }
}

function endSession() {
    if (ws) {
        ws.close();
        ws = null;
    }
}

function sendContent() {
    var message = {
        from: users.sender,
        to: users.sendto,
        message: document.querySelector("textarea[name=message]").value
    };
    ws.send(JSON.stringify(message));
}

function clearContent() {
    var children = content.childNodes;
    for (var i = children.length - 1; i >= 0; i--) {
        children[i].remove();
    }
}

function createMessage(dir, from, time, msg) {
    var message = document.createElement('div');
    message.classList.add(dir);
    message.innerHTML = `
            <p class='title'>from ${from} at ${time}</p>
            <p class='content'>${msg}</p>
    `;
    return message;
}

function dateTime() {
    var now = new Date();
    var date = now.toLocaleDateString('en-GB', {
        day: 'numeric',
        month: 'short',
        year: 'numeric'
    });

    var time = now.toLocaleTimeString(undefined, {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });

    return `${date} at ${time}`;
}
