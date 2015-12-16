package kr.co.dementor.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import kr.co.dementor.jsonformat.JReceiveAuthData;
import kr.co.dementor.jsonformat.JReceiveRegisterSetting;
import kr.co.dementor.ui.AuthActivity;
import kr.co.dementor.ui.RegisterActivity;

public class Controller {
    private static Controller dementorController;

    private OnDementorListener statusListener;

    private ConnectInfo connectionInfo;

    private Controller() {
    }

    public static Controller getInstance() {
        if (dementorController == null) dementorController = new Controller();

        return dementorController;
    }

    /**
     * @return the statusListener
     */
    public OnDementorListener getStatusListener() {
        return statusListener;
    }

    /**
     * @param statusListener the statusListener to set
     */
    public void setStatusListener(OnDementorListener statusListener) {
        this.statusListener = statusListener;
    }

    /**
     * @return the connectionInfo
     */
    public ConnectInfo getConnectionInfo() {
        return connectionInfo;
    }

    /**
     * @param connectionInfo the connectionInfo to set
     */
    public void setConnectionInfo(ConnectInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public void register(Context ctx, OnDementorListener listener, ConnectInfo connectionInfo) {
        this.setStatusListener(listener);

        this.setConnectionInfo(connectionInfo);

        Intent intent = new Intent(ctx, RegisterActivity.class);

        ctx.startActivity(intent);

    }

    public void auth(Context ctx, OnDementorListener listener, ConnectInfo connectionInfo) {
        this.setStatusListener(listener);

        this.setConnectionInfo(connectionInfo);

        Intent intent = new Intent(ctx, AuthActivity.class);

        ctx.startActivity(intent);
    }

    public interface OnDementorListener {
    	/*
         public void onDementorCreate(Activity activity);
         public void onDementorResume(Activity activity);
         public void onDementorPause(Activity activity);
    	 public void onDementorAuthImageDownFinished(JReceiveAuthImage receiveData);
         public void onDementorRegImageDownFinished(JReceiveRegisterImage receiveData);
         * 쓸까말까 고민중...
         */

        public void onDementorAuthFinished(JReceiveAuthData receiveData);
        
        public void onDementorRegFinished(JReceiveRegisterSetting receiveData);

        public void onBack(Activity activity);
    }
}
