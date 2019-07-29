// IWebAidlCallback.aidl
package com.plumcookingwine.webview;

// Declare any non-default types here with import statements

interface IWebAidlCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onResult(int responseCode, String actionName, String response);
}
