package com.jarredweb.zesty.setup;

import com.jarredweb.zesty.http.app.ZestyBuilder;

public interface ZestyProvider<T extends ZestyBuilder> {
        
    T provide(OnStartAction action);
}
