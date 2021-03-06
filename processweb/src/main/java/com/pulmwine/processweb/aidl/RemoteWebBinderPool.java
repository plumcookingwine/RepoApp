package com.pulmwine.processweb.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.pulmwine.processweb.IBinderPool;
import com.pulmwine.processweb.aidl.mainpro.MainProAidlInterface;
import com.pulmwine.processweb.aidl.mainpro.MainProHandleRemoteService;

import java.util.concurrent.CountDownLatch;

/**
 * Created by xud on 2017/8/22.
 *
 * 用于remoteweb process 向 main process 获取binder
 */

public class RemoteWebBinderPool {

    public static final int BINDER_WEB_AIDL = 1;

    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile RemoteWebBinderPool sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private RemoteWebBinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static RemoteWebBinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RemoteWebBinderPool.class) {
                if (sInstance == null) {
                    sInstance = new RemoteWebBinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, MainProHandleRemoteService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {   // 5

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {    // 6
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub {

        private Context context;

        public BinderPoolImpl(Context context) {
            this.context = context;
        }

        @Override
        public IBinder queryBinder(int binderCode) {
            IBinder binder = null;
            if (binderCode == BINDER_WEB_AIDL) {
                binder = new MainProAidlInterface(context);
            }
            return binder;
        }
    }

}
