package com.practicaldime.zesty.quickstart;

import com.practicaldime.zesty.setup.OnStartAction;
import com.practicaldime.zesty.setup.ZestyProvider;

public class AppRunnerProvider implements ZestyProvider<AppRunner>{

    @Override
    public AppRunner provide(OnStartAction func) {
        return new AppRunner(){
            @Override
            public void onServerStarted(int port, String host) {
                func.apply(port, host);
            }
        };
    }
}
