$(document).ready(function() {


    let editbtn = `<i style="display:inline-block;font-size:2em;padding:10px;" class="add fa fa-plus-circle font-weight-bold bg-info rounded-circle" data-toggle="tooltip" data-placement="top" title="Add content">&nbsp;</i>`;
    let syncbtn = `<i style="display:inline-block;font-size:1.5em;padding:10px;margin-left:10px;" class="sync fa fa-save font-weight-bold bg-success rounded-circle" data-toggle="tooltip" data-placement="top" title="Sync Content">&nbsp;</i>`;

    let editops = `
    <span class="editops btn-group" role="group" aria-label="Edit Options" style="position:absolute;top:0;left:0;">
        <button type="button" class="btn btn-sm btn-secondary" title="edit"><i class="fa fa-edit">&nbsp;</i></button>
        <button type="button" class="btn btn-sm btn-warning" title="prompt"><i class="fa fa-arrow-circle-down">&nbsp;</i></button>
        <span class="taginput" style="display:none;"><input type="text" name="tagname"/>
        <button type="button" class="btn btn-sm btn-warning" title="insert"><i class="fa fa-plus">&nbsp;</i></button></span>
        <button type="button" class="btn btn-sm btn-danger" title="delete"><i class="fa fa-times-circle">&nbsp;</i></button>
    </span>`;

    let PageDoc = function(config) {
        this.callbacks = { load: undefined, close: undefined, sync: undefined, insert: undefined };
        this.editor = this.genId(); //selector in html page to attach editor controls
        this.content = config.content; //selector in html page to insert content
        this.source = config.source;
        this.element = config.element; //lowest parent tag in XML for target nodes
        this.basetag = config.basetag; //container tag if items are a collection
        this.xmldata = config.appname || 'xmldata';
        this.appmode = config.appmode;
        $('body').append($(`<div id='${this.editor}'></div>`));

        if (!config.source) {
            let xml = localStorage.getItem(this.xmldata) || `<?xml version="1.0" encoding="UTF-8"?>`
            var xParser = new DOMParser();
            this.xDOM = xParser.parseFromString(xml, "application/xml");
            this.render(this.content);
        } else {
            if (typeof config.source === 'object') {
                this.xDOM = config.source;
                this.render(this.content);
            } else {
                this.fetch(config.source);
                this.onload('render', this.content);
            }
        }
        this.init();
    }

    PageDoc.prototype.init = function(file) {
        let self = this;
        $("#" + this.editor)
            .css({ position: 'fixed', bottom: '10px', left: '10px' })
            .append($(editbtn))
            .on('click', '.add', function(ev) {
                let editable = $(`
                  <div data-editable data-name='${self.genId()}' class='mb-3'>Add content Here</div>`).insertAfter($("[data-editable]").last());
                self.attachHandler(editable);
            })
            .append($(syncbtn))
            .on('click', '.sync', function(ev) {
                let container = [`<?xml version="1.0" encoding="UTF-8"?>`];
                if (self.basetag) container.push(`<${self.basetag}>`);
                let nodes = $("[data-name]").each(function() {
                    let name = self.tagname($(this).data('name'));
                    container.push(`<${name} id="${this.dataset.id | ''}"><![CDATA[${this.innerHTML}]]></${name}>\n`);
                });
                if (self.basetag) container.push(`</${self.basetag}>`);
                let xml = container.join("");
                self.onsync(xml);
            });
    }

    PageDoc.prototype.genId = function(file) {
        // Math.random should be unique because of its seeding algorithm.
        // Convert it to base 36 (numbers + letters), and grab the first 9 characters
        // after the decimal.
        return '_' + Math.random().toString(36).substr(2, 9);
    }

    PageDoc.prototype.attachHandler = function(editable) {

        let self = this;
        let current = undefined;
        let bgcolor = undefined;
        let tipmenu = undefined;

        var closeBtn = function(context) {
            var ui = $.summernote.ui;

            // create button
            var button = ui.button({
                contents: '<i class="fa fa-window-close"/>',
                click: function() {
                    let markup = current.summernote('code');
                    self.onclose(markup);
                    current.summernote('destroy');
                }
            });

            return button.render(); // return button as jquery object
        };

        editable.hover(function(e) {
            bgcolor = editable.css('background-color');
            editable.css({
                'background-color': '#F0E68C',
                'position': 'relative'
            });
            tipmenu = $(editops).on('click', '[title=edit]', function(ev) {
                $(this).parent().remove();
                if (current) current.summernote('destroy');
                current = editable.summernote({
                    height: 300,
                    focus: true,
                    callbacks: {
                        onKeyup: function(e) {
                            if (e.keyCode === 27) { //esc
                                editable.summernote('reset');
                                editable.summernote('destroy');
                            }
                        }
                    },
                    toolbar: [
                        ['style', ['bold', 'italic', 'underline', 'clear']],
                        ['font', ['strikethrough', 'superscript', 'subscript']],
                        ['fontsize', ['fontsize']],
                        ['color', ['color']],
                        ['para', ['ul', 'ol', 'paragraph']],
                        ['height', ['height']],
                        ['insert', ['picture', 'link', 'table', 'hr']],
                        ['misc', ['fullscreen', 'codeview', 'undo', 'redo']],
                        ['terminal', ['close']]
                    ],
                    buttons: {
                        close: closeBtn
                    }
                });
                current.focus();
            }).on('click', '[title=prompt]', function(ev) {
                let taginput = $(this).siblings(".taginput");
                if (taginput.is(":visible")) {
                    self.callbacks['insert'] = undefined;
                    taginput.hide();
                } else {
                    self.callbacks['insert'] = function(btn) {
                        let tagname = $('input', taginput).val();
                        let new_editable = $(`
                        <div data-editable data-name='${tagname}' class='mb-3'>Add ${tagname || self.genId()} Here</div>`).insertAfter(btn.parents('[data-editable]'));
                        self.attachHandler(new_editable);
                    }
                    taginput.show();
                }

            }).on('click', '[title=insert]', function(ev) {
                self.callbacks['insert']($(this));
                self.callbacks['insert'] = undefined;
                $(this).siblings(".taginput").hide();
            }).on('click', '[title=delete]', function(ev) {
                $(this).parents('[data-editable]').remove();
            });
            //attach tip menu
            editable.append(tipmenu);
        }, function(e) {
            editable.css({
                'background-color': bgcolor
            });
            tipmenu.remove();
            bgcolor = undefined;
        });
    }

    PageDoc.prototype.fetch = function(file) {
        var xhr = new XMLHttpRequest();
        xhr.onload = () => {
            this.xDOM = xhr.responseXML.documentElement;
            this.callbacks['load']();
        }
        xhr.onerror = function() {
            console.log("Error while getting XML.");
        }
        xhr.open("GET", file, true); // `false` makes the request synchronous
        xhr.responseType = "document";
        xhr.send();
    }

    PageDoc.prototype.onload = function(action) {
        let args = Array.from(arguments).slice(1);
        this.callbacks['load'] = () => {
            this[action].apply(this, args);
        }
    }

    PageDoc.prototype.onclose = function(xmldata) {
        this.callbacks['close'].call(this, xmldata);
    }

    PageDoc.prototype.onsync = function(xmldata) {
        localStorage.setItem(this.xmldata, xmldata);
        this.callbacks['sync'].call(this, xmldata);
    }

    PageDoc.prototype.render = function(target) {
        let self = this;
        switch (this.appmode) {
            case "render":
                {
                    let elements = $(this.xDOM).find(this.element);

                    //1. if a tag exists with data-name={this.tagName}, then append the data
                    //2. else, create a new tag and append after the last editable tag
                    elements.each(function(i) {
                        let nodes = document.querySelectorAll(`[data-name=${this.tagName}]`);
                        if (nodes.length > i) {
                            console.log(`element with data-name=#${this.tagName} exists`);
                            for (var j = 0; j < nodes.length; j++) {
                                let parent = nodes[i];
                                $(this).children().each(function(j) {
                                    let editable = $(`[data-name=${this.tagName}]`, parent);
                                    editable.html(self.uncdata(this.innerHTML));
                                    self.attachHandler(editable);
                                });
                            }

                        } else {
                            let parent = $(`<div data-name="${this.tagName}" data-id="${this.getAttribute('id')}"></div>`);
                            $(this).children().each(function(j) {
                                let editable = $(`
                            <div data-editable data-name='${this.tagName}' class='mb-3'>${self.uncdata(this.innerHTML)}</div>`);
                                parent.append(editable).appendTo($(target).last());
                                self.attachHandler(editable);
                            });
                        }
                    });
                    break;
                }
            case "activate":
                {
                    console.log('activate elements on page');
                    $("[data-editable]").each(function(i) {
                        self.attachHandler($(this));
                    });
                    break;
                }
            default:
                {
                    throw Error('render mode is required.')
                }
        }
    }

    PageDoc.prototype.listener = function(event, handler) {
        this.callbacks[event] = handler;
    }

    PageDoc.prototype.serialize = function(doc) {
        var oSerializer = new XMLSerializer();
        var sXML = oSerializer.serializeToString(doc);
    }

    PageDoc.prototype.uncdata = function(val) {
        let match = val.match(/^<\!\[CDATA\[(.*?)\]\]>$/);
        return (match) ? match[1] : val;
    }

    PageDoc.prototype.tagname = function(expr) {
        let index = expr.lastIndexOf("/");
        return (index > -1) ? expr.substring(index + 1) : expr;
    }

    window.PageDoc = PageDoc;
});