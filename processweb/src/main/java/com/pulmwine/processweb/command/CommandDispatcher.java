package com.pulmwine.processweb.command;

import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.pulmwine.processweb.IWebAidlCallback;
import com.pulmwine.processweb.IWebAidlInterface;
import com.pulmwine.processweb.aidl.RemoteWebBinderPool;
import com.pulmwine.processweb.bridge.call.ResultCallback;
import com.pulmwine.processweb.common.WebConstants;
import com.pulmwine.processweb.interfaces.Action;
import com.pulmwine.processweb.utils.SystemInfoUtil;
import com.pulmwine.processweb.weiget.BaseWebView;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * WebView所有请求分发
 *
 * 规则：
 *
 * 1、先处理UI依赖
 * 2、再判断是否是跨进程通信，跨进程通信需要通过AIDL方式分发数据
 */
public class CommandDispatcher {

    private static CommandDispatcher instance;
    private Gson gson = new Gson();

    // 实现跨进程通信的接口
    private IWebAidlInterface webAidlInterface;

    private CommandDispatcher() {

    }

    public static CommandDispatcher getInstance() {
        if (instance == null) {
            synchronized (CommandDispatcher.class) {
                if (instance == null) {
                    instance = new CommandDispatcher();
                }
            }
        }
        return instance;
    }

    public IWebAidlInterface getWebAidlInterface(Context context) {
        if (webAidlInterface == null) {
            initAidlConnect(context, null);
        }
        return webAidlInterface;
    }

    public void initAidlConnect(final Context context, final Action action) {
        if (webAidlInterface != null) {
            if (action != null) {
                action.call(null);
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("AIDL", "Begin to connect with main process");
                RemoteWebBinderPool binderPool = RemoteWebBinderPool.getInstance(context);
                IBinder iBinder = binderPool.queryBinder(RemoteWebBinderPool.BINDER_WEB_AIDL);
                webAidlInterface = IWebAidlInterface.Stub.asInterface(iBinder);
                Log.i("AIDL", "Connect success with main process");
                if (action != null) {
                    action.call(null);
                }
            }
        }).start();
    }


    public void exec(Context context, int commandLevel, String cmd, String params, WebView webView,
                     DispatcherCallBack dispatcherCallBack) {
        Log.i("CommandDispatcher", "command: " + cmd + " params: " + params);
        try {
            if (CommandsManager.getInstance().checkHitUICommand(commandLevel, cmd)) {
                execUI(context, commandLevel, cmd, params, webView, dispatcherCallBack);
            } else {
                execNonUI(context, commandLevel, cmd, params, webView, dispatcherCallBack);
            }
        } catch (Exception e) {
            Log.e("CommandDispatcher", "Command exec error!!!!", e);
        }
    }

    private void execUI(final Context context, final  int commandLevel, final String cmd, final String params,
                        final WebView webView, final DispatcherCallBack dispatcherCallBack) {
        Map mapParams = gson.fromJson(params, Map.class);
        CommandsManager.getInstance().findAndExecUICommnad(context, commandLevel, cmd, mapParams, new ResultCallback() {
            @Override
            public void onResult(int status, String action, Object result) {
                try {
                    if (status == WebConstants.CONTINUE) {
                        execNonUI(context, commandLevel, action, gson.toJson(result), webView, dispatcherCallBack);
                    } else {
                        handleCallback(status, action, gson.toJson(result), webView, dispatcherCallBack);
                    }
                } catch (Exception e) {
                    Log.e("CommandDispatcher", "Command exec error!!!!", e);
                }
            }
        });
    }

    private void execNonUI(Context context, int commandLevel, String cmd, String params, final WebView webView, final DispatcherCallBack dispatcherCallBack) throws Exception {
        Map mapParams = gson.fromJson(params, Map.class);
        if (SystemInfoUtil.INSTANCE.inMainProcess(context, android.os.Process.myPid())) {
            CommandsManager.getInstance().findAndExecNonUICommand(context, commandLevel, cmd, mapParams, new ResultCallback() {
                @Override
                public void onResult(int responseCode, String actionName, Object result) {
                    handleCallback(responseCode, actionName, gson.toJson(result), webView, dispatcherCallBack);
                }
            });
        } else {
            if (webAidlInterface != null) {
                webAidlInterface.handleWebAction(commandLevel, cmd, params, new IWebAidlCallback.Stub() {
                    @Override
                    public void onResult(int responseCode, String actionName, String response) {
                        handleCallback(responseCode, actionName, response, webView, dispatcherCallBack);
                    }
                });
            }
        }
    }

    private void handleCallback(final int responseCode, final String actionName, final String response,
                                final WebView webView, final DispatcherCallBack dispatcherCallBack) {
        Log.d("CommandDispatcher", String.format("Callback result: action= %s, result= %s", actionName, response));
        MainLooper.Companion.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dispatcherCallBack != null &&
                        !dispatcherCallBack.preHandleBeforeCallback(responseCode, actionName, response)) {
                    Map params = new Gson().fromJson(response, Map.class);
                    if (params.get(WebConstants.NATIVE2WEB_CALLBACK) != null && !TextUtils.isEmpty(params.get(WebConstants.NATIVE2WEB_CALLBACK).toString())) {
                        if (webView instanceof BaseWebView) {
                            ((BaseWebView) webView).handleCallback(response);
                        }
                    }
                    return;
                }

                Map params = new Gson().fromJson(response, Map.class);
                if (params.get(WebConstants.NATIVE2WEB_CALLBACK) != null &&
                        !TextUtils.isEmpty(params.get(WebConstants.NATIVE2WEB_CALLBACK).toString())) {
                    if (webView instanceof BaseWebView) {
                        ((BaseWebView) webView).handleCallback(response);
                    }
                }
            }
        });
    }

    /**
     * Dispatcher 过程中的回调介入
     */
    public interface DispatcherCallBack {
        boolean preHandleBeforeCallback(int responseCode, String actionName, String response);
    }
}
