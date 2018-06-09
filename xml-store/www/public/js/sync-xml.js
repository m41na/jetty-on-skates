$(document).ready(function() {

    //import xml to transform
    var xmlReq = new XMLHttpRequest();
    //xmlReq.addEventListener('load', onLoadXml);
    xmlReq.open('GET', "xml/blogpost.xml", false); //false to indicate synchronous request
    xmlReq.send(null);

    var xmlRef = xmlReq.responseXML;

    //now load xsl required to transform
    var xslReq = new XMLHttpRequest();
    //xslReq.addEventListener('load', onLoadXsl);
    xslReq.open('GET', "xsl/blogpost.xsl", false); //false to indicate synchronous request
    xslReq.send(null);

    var xslRef = xslReq.responseXML;

    //finally import the xsl
    var xsltProcessor = new XSLTProcessor();
    xsltProcessor.importStylesheet(xslRef);

    //now transform the xml
    var doc = xsltProcessor.transformToDocument(xmlRef);
    var content = doc.querySelector("#blogpost");
    console.log(content);
    //document.querySelector("#blogpost").innerHTML = content.innerHTML;

    //define an API for external calls
    let AjaxApi = {
        sendXML: function(config) {
            $.ajax({
                url: 'http://localhost:3010/ws/basex/blogs',
                type: 'post',
                dataType: 'xml',
                contentType: 'application/xml',
                data: config.xml,
                success: config.success,
                error: config.error
            });
        }
    }

    //handle submit action
    $("#search-btn").on('click', function submitUpdate() {
        var nodes = document.querySelectorAll("[data-name]");
        var updates = {};
        for (var i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            updates[node.dataset.name] = node.innerHTML;
        }
        console.log(updates);
    });

    //declare init callbacks
    let onSync = (markup) => {

        let onSyncOk = (data) => {
            console.log(data);
        }

        let onSyncFail = (xhr) => {
            console.log(xhr.responseText);
        }

        AjaxApi.sendXML({
            xml: markup,
            success: onSyncOk,
            error: onSyncFail
        })
    }

    let onClose = (markup) => {
        console.log(markup);
    }

    //initialize editor
    let Blogs = new PageDoc({
        appname: 'blogger',
        appmode: 'activate',
        content: '[data-id=blogpost]',
        source: {},
        element: 'blogpost',
        basetag: 'blogpost'
    });

    //attach 'close' listener
    Blogs.listener('close', onClose);

    //attach 'sync' listener
    Blogs.listener('sync', onSync);
});