package com.practicaldime.zesty.setup;

import com.practicaldime.zesty.http.app.ZestyBuilder;

public interface ZestyProvider<T extends ZestyBuilder> {
        
    T provide(OnStartAction action);
}
