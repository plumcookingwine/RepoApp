// IWebAidlInterface.aidl
package com.plumcookingwine.webview;

// Declare any non-default types here with import statements
import com.plumcookingwine.webview.IWebAidlCallback;

interface IWebAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void handleWebAction(int level, String actionName, String jsonParams, in IWebAidlCallback callback);
}
