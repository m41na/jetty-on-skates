package com.jarredweb.jesty.todos;

import com.jarredweb.jesty.view.AbstractView;

public abstract class TodosView extends AbstractView {

    @Override
    public String getContentType() {
        return "text/html;charset=utf-8";
    }

    @Override
    public String getEngine() {
        return "jtwig";
    }

    @Override
    public String getTitle() {
        return "ToDo Tasks";
    }

    @Override
    public String getDestFile() {
        return "todos.html";
    }

    @Override
    public String[] getMetaTags() {
        return new String[]{
            "<link type=\"text/css\" href=\"/app/css/todos.css\" rel=\"stylesheet\">"
        };
    }

    @Override
    public String[] getStyles() {
        return new String[]{
            "<link type=\"text/css\" href=\"/app/css/todos.css\" rel=\"stylesheet\">"
        };
    }

    @Override
    public String[] getScripts(boolean top) {
        return (!top)
                ? new String[]{
                    "<script type=\"text/javascript\" src=\"/app/js/todos.js\"></script>"
                }
                : new String[]{};
    }
}
