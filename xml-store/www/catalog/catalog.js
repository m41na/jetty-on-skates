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
    let target = document.querySelector("#content");
    let xodes = dom.documentElement.querySelectorAll("expr");
    for (let i = 0; i < xodes.length; i++) {
        var content = xodes[i].innerHTML.trim();
        let matches = content.match(/((<\!\[CDATA\[)|(&lt;\!\[CDATA\[))/);
        if (matches.length > 1) {
            content = content.substring(matches[1].length);
            //check end of content
            matches = content.match(/((\]\]>$)|(\]\]&gt;$))/);
            if (matches.length > 1) {
                content = content.substring(0, content.length - matches[1].length);
            }
        }

        //create element from markup
        var el = parser.parseFromString(he.decode(content), "text/html");
        let child = document.createElement("div");
        child.dataset.editable = "";
        child.dataset.name = "/".concat(xodes[i].tagName).concat("[" + i + "]");
        child.appendChild(el.body.firstChild);
        //child.append(el.body.childNodes);
        target.appendChild(child);
    }
}

let pageToXML = function() {
    var doc = document.implementation.createDocument("", "", null);
    var catElem = doc.createElement("catalog");
    var exprsElem = doc.createElement("expressions");
    let nodes = document.querySelectorAll("[data-name]");
    for (let i = 0; i < nodes.length; i++) {
        var content = nodes[i].innerHTML;
        content = he.encode(content);
        var expr = doc.createElement("expr");
        expr.setAttribute("id", i + 1);
        let cdata = doc.createCDATASection(content);
        expr.append(cdata);
        exprsElem.appendChild(expr);
    }

    catElem.appendChild(exprsElem);
    doc.appendChild(catElem);
    console.log(doc);
    return doc;
}

let sendXMLData = function(url) {
    let doc = pageToXML();
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

loadXMLFile('catalog.xml');