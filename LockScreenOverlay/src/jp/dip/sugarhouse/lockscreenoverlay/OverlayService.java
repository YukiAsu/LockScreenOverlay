package jp.dip.sugarhouse.lockscreenoverlay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Activityに関係無くViewを表示し続けるためのサービス。<br>
 * サービスがスタートするとWindowManagerを使ってViewをほぼ最前面に表示させる。サービスが終了する時にViewをきちんと後始末することも忘れない。
 * Activityとして作ってしまうと、Activityのライフサイクルに従わなければならない為に期待通りの動作をさせるのが面倒くさい。たぶんできない。
 * @author YukiAsu
 */
public class OverlayService extends Service {

	/** {@link Binder}。{@link #onBind(Intent)}の戻り値が{@link IBinder}なのでIBinderになってる。中身は{@link Binder}のextends */
	public final IBinder mBinder = new OverlayServiceBinder();

	/** ViewをActivityに関係なく画面に放り込むためのWindowManager */
	private WindowManager wm;
	/** じっさいに画面に表示されるView */
	private FrameLayout view;
	/** Viewのパラメータ。これによってロック画面の上に鎮座させる */
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate(){
		// WindowManagerを取得して、xmlから簡素なViewを生成して、Viewの属性も準備しておく
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		this.view = new FrameLayout(getApplicationContext());
		((FrameLayout)this.view).addView(layoutInflater.inflate(R.layout.overlay, null));
        params = new WindowManager.LayoutParams(
        	    WindowManager.LayoutParams.WRAP_CONTENT,
        	    WindowManager.LayoutParams.WRAP_CONTENT,
        	    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,			// ロック画面より上にくる
        	    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |		// タッチイベントを拾わない。ロック画面を邪魔しない。
        	    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,			// フォーカスされない。ハードキーで選ぼうとかしても、無理。
        	    PixelFormat.TRANSLUCENT); 						// ウィンドウの透明化

    	wm.addView(view, params);
	}
	
	/**
	 * 表示するViewのresourceIDを指定する
	 * @param resource 表示させたいresource
	 */
	public void setView(int resource){
		view.removeAllViews();
		LayoutInflater layoutInflater = LayoutInflater.from(this);
        this.view.addView(layoutInflater.inflate(R.layout.overlay, null));
	}
	
	/**
	 * 表示するViewを直接指定する
	 * @param view 表示させたいView
	 */
	public void setView(View view){
		this.view.removeAllViews();
		this.view.addView(view);
	}
	
	/**
	 * サービスの起動時に呼び出される。Viewを表示するだけ。
	 */
	@Override
	public void onStart(Intent intent, int startId) {
	}
	
	/**
	 * サービスの終了時に呼び出される。Viewをきちんと消して死ぬ。
	 */
	@Override
	public void onDestroy() {
		wm.removeView(view);
	}

	/**
	 * bindするための橋渡し、{@link Binder}<br>
	 * コレを介して自分自身を渡してあげる事で、Serviceのpublicメソッドが使用できるようになります。
	 */
	public class OverlayServiceBinder extends Binder {
		OverlayService getService() {
			// クライアントがpublicメソッドを呼び出せるように、自分自身のインスタンスを返す。
			return OverlayService.this;
		}
	}
}
