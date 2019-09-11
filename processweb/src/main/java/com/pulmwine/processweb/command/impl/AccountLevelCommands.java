package com.pulmwine.processweb.command.impl;

import android.content.Context;
import android.text.TextUtils;
import com.limpoxe.support.servicemanager.ServiceManager;
import com.plumcookingwine.common.interfaces.LoginListener;
import com.plumcookingwine.common.provider.LoginProvider;
import com.plumcookingwine.common.provider.LoginProviderImpl;
import com.plumcookingwine.network.utils.ACache;
import com.pulmwine.processweb.WebInitSdk;
import com.pulmwine.processweb.aidl.error.AidlError;
import com.pulmwine.processweb.bridge.call.ResultCallback;
import com.pulmwine.processweb.command.Command;
import com.pulmwine.processweb.command.Commands;
import com.pulmwine.processweb.common.WebConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xud on 2017/12/16.
 */

public class AccountLevelCommands extends Commands {

    public AccountLevelCommands() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand(appDataProviderCommand);
        registerCommand(appDataProviderCommand2);
    }

    @Override
    public int getCommandLevel() {
        return WebConstants.LEVEL_ACCOUNT;
    }

    // 获取native data
    private final Command appDataProviderCommand2 = new Command() {
        @Override
        public String name() {
            return "appDataProvider";
        }

        @Override
        public void exec(Context context, Map params, ResultCallback resultBack) {
            try {
                String callbackName = "";
                if (params.get("type") == null) {
                    AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                    resultBack.onResult(WebConstants.FAILED, this.name(), aidlError);
                    return;
                }
                if (params.get(WebConstants.WEB2NATIVE_CALLBACk) != null) {
                    callbackName = params.get(WebConstants.WEB2NATIVE_CALLBACk).toString();
                }
                String type = params.get("type").toString();
                HashMap map = new HashMap();
                switch (type) {
                    case "account":
                        map.put("accountId", "test123456");
                        map.put("accountName", "xud");
                        break;
                }
                if (!TextUtils.isEmpty(callbackName)) {
                    map.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName);
                }
                resultBack.onResult(WebConstants.SUCCESS, this.name(), map);
            } catch (Exception e) {
                e.printStackTrace();
                AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                resultBack.onResult(WebConstants.FAILED, this.name(), aidlError);
            }
        }
    };

    // 获取native data
    private final Command appDataProviderCommand = new Command() {

        private Map params;
        private ResultCallback resultCallback;
        private LoginListener mLoginListener = new LoginListener() {

            @Override
            public void onLogin() {
                try {
                    String callbackName = "";
                    if (params.get("type") == null) {
                        AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                        resultCallback.onResult(WebConstants.FAILED, name(), aidlError);
                        return;
                    }
                    if (params.get(WebConstants.WEB2NATIVE_CALLBACk) != null) {
                        callbackName = params.get(WebConstants.WEB2NATIVE_CALLBACk).toString();
                    }
                    String type = params.get("type").toString();
                    HashMap map = new HashMap();
                    switch (type) {
                        case "account":
                            // 本地获取value
                            map.put("accountName", ACache.get(WebInitSdk.Companion.getInstance().getContext())
                                    .getAsString("username"));
                            break;
                    }
                    if (!TextUtils.isEmpty(callbackName)) {
                        map.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName);
                    }
                    resultCallback.onResult(WebConstants.SUCCESS, name(), map);
                } catch (Exception e) {
                    e.printStackTrace();
                    AidlError aidlError = new AidlError(WebConstants.ERRORCODE.ERROR_PARAM, WebConstants.ERRORMESSAGE.ERROR_PARAM);
                    resultCallback.onResult(WebConstants.FAILED, name(), aidlError);
                }
            }

            @Override
            public void logout() {

            }

            @Override
            public void register() {

            }


        };

        @Override
        public String name() {
            return "appLogin";
        }

        @Override
        public void exec(Context context, Map params, ResultCallback resultBack) {
            LoginProvider provider = (LoginProviderImpl) ServiceManager.getService(LoginProvider.LOGIN_SERVICE);
            if (!provider.isLogin()) {
                provider.registerLoginListener(mLoginListener);
                provider.login();
            }
            this.params = params;
            this.resultCallback = resultBack;
        }
    };
}
