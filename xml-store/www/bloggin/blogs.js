let config = {
    target: "#books-list",
    xmlroot: "catalog",
    xmltags: {
        catalog: {
            book: {
                author: '',
                title: '',
                genre: '',
                price: '',
                publish_date: '',
                description: ''
            }
        }
    }
};

var parser = new DOMParser();
let nodes = document.querySelectorAll("[data-editable]");
for (let i = 0; i < nodes.length; i++) {
    console.log(nodes[i].innerHTML);
}

let loadXMLString = function(xmlstr) {
    let example = "<catalog></catalog>";
    var oParser = new DOMParser();
    var oDOM = oParser.parseFromString(xmlstr, "application/xml");
    console.log(oDOM.documentElement.nodeName == "parseerror" ? "error while parsing" : oDOM.documentElement.nodeName);
}

let loadXMLFile = function(xmlfile) {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        xmlToPage(xhr.responseXML);
    }

    xhr.onerror = function() {
        console.log("Error while getting XML");
    }

    xhr.open("GET", xmlfile);
    xhr.responseType = "document";
    xhr.send();
}

let xmlToPage = function(dom) {
    let target = document.querySelector(config.target);
    let xodes = dom.querySelectorAll(config.xmlroot);
    for (let i = 0; i < xodes.length; i++) {
        let child = document.createElement("div");
        let xode = xodes[i];
        child.dataset.name = xode.tagName;
        target.appendChild(child);
        renderXodes(xode, "/" + xode.tagName, i, child);
    }
}

let renderXodes = function(xode, path, index, target) {
    if (xode.children && xode.children.length > 0) {
        for (let i = 0; i < xode.childElementCount; i++) {
            let child = document.createElement("div");
            child.dataset.name = xode.children[i].tagName;
            if (xode.attributes.id) {
                target.dataset[xode.tagName] = xode.attributes.id.value;
            }
            target.appendChild(child);
            renderXodes(xode.children[i], "/".concat(xode.tagName + "[" + index + "]"), i, child);
        }
    } else {
        var content = xode.innerHTML.trim();
        let matches = content.match(/((<\!\[CDATA\[)|(&lt;\!\[CDATA\[))/);
        if (matches && matches.length > 1) {
            content = content.substring(matches[1].length);
            //check end of content
            matches = content.match(/((\]\]>$)|(\]\]&gt;$))/);
            if (matches && matches.length > 1) {
                content = content.substring(0, content.length - matches[1].length);
            }
        }

        //create element from markup
        var el = parser.parseFromString(he.decode(content), "text/html");
        let child = document.createElement("div");
        child.dataset.name = path + "/".concat(xode.tagName);
        child.setAttribute("contenteditable", true);
        child.appendChild(el.body.firstChild);
        //child.append(el.body.childNodes);
        target.appendChild(child);
    }
}

let visitElements = function(tag) {
    let target = document.querySelector(`[data-name=${config.xmlroot}]`);
    let elements = target.querySelectorAll(`[data-name=${tag}]`);
    for (let i = 0; i < elements.length; i++) {
        let child = elements[i];
        let name = child.dataset.name
        nextProperty(name, config.xmltags.catalog[name], child);
    }
}

let nextProperty = function(key, value, el, func) {
    if (typeof value === 'object') {
        for (let prop in value) {
            console.log(key);
            var node = el.querySelector(`[data-name=${prop}]`);
            nextProperty(key.concat(`.${prop}`), value[prop], node, func);
        }
    } else {
        let name = key.substring(key.lastIndexOf(".") + 1);
        var node = el.querySelector(`[contenteditable]`);
        var path = node.dataset.name;
        console.log(`${name} path is ${path} and value is ${node.innerHTML}`)
        console.log(key);
        if (func) {
            func.apply(this, [name, path, node]);
        }
    }
}

let pageToXML = function(tag) {
    var doc = document.implementation.createDocument("", "", null);
    var rootElem = doc.createElement(config.xmlroot);

    //get dom root
    let domroot = document.querySelector(config.target);
    let elements = domroot.querySelectorAll(`[data-name=${tag}]`);

    var bookElem;
    for (let i = 0; i < elements.length; i++) {
        let child = elements[i];
        let name = child.dataset.name
        bookElem = doc.createElement(child.dataset.name);
        bookElem.setAttribute('id', child.dataset.book);
        nextProperty(name, config.xmltags.catalog[name], child, buildNode);
        rootElem.appendChild(bookElem);
    }

    function buildNode(name, path, node) {
        var content = node.innerHTML;
        content = he.encode(content);
        var editElem = doc.createElement(name);
        editElem.setAttribute("path", node.dataset.name);
        let cdata = doc.createCDATASection(content);
        editElem.append(cdata);
        bookElem.appendChild(editElem);
    }

    doc.appendChild(rootElem);
    return doc;
}

let sendXMLData = function(url, tag) {
    let doc = pageToXML(tag);
    var oSerializer = new XMLSerializer();
    var sXML = oSerializer.serializeToString(doc);
    console.log(sXML);
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        console.log("Sent");
    }

    xhr.onerror = function() {
        console.log("Error while sending XML");
    }

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/xml");
    xhr.send(sXML);
}

loadXMLFile('books.xml');