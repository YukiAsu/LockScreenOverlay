package jp.dip.sugarhouse.lockscreenoverlay;

import jp.dip.sugarhouse.lockscreenoverlay.OverlayService.OverlayServiceBinder;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * 設定画面。というよりなんのことは無くボタンがあるだけ。startを押せばserviceが立ち上がってViewを表示し、stopを押せばserviceを殺してViewには消えて頂く。
 * @author YukiAsu
 */
public class Setting extends Activity {
	
	/** 表示を管理しているService。{@link ServiceConnection}によってサービス内のpublicメソッドをごりごり呼び出す */
	private OverlayService overlayService;
	/** いま{@link Service}はbindされてる？っていうフラグ。trueならされてる。 */
	private boolean mBound = false;

	/** Activityが生成される時に呼ばれる */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Serviceを呼び出す為のIntentを生成
		final Intent overlayService = new Intent();
		overlayService.setClass(this, OverlayService.class);

		((Button)findViewById(R.id.start)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startService(overlayService);
				bindService(overlayService, mConnection, Context.BIND_AUTO_CREATE);
			}
		});

		((Button)findViewById(R.id.stop)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(overlayService);
				if (mBound) {
		            unbindService(mConnection);
		            mBound = false;
		        }
			}
		});
		
		((Button)findViewById(R.id.changeView1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Setting.this.overlayService.setView(new Button(getApplicationContext()));
			}
		});
		
		((Button)findViewById(R.id.changeView2)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Setting.this.overlayService.setView(new RadioButton(getApplicationContext()));
			}
		});
		
		((Button)findViewById(R.id.changeView3)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Setting.this.overlayService.setView(new EditText(getApplicationContext()));
			}
		});
	}

	/**
	 * {@link ServiceConnection}を使う事でServiceにあるpublicのメソッドをActivityからでも呼び出せます。<br>
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		/**
		 * @param className 接続されたServiceの名前とかそこら。
		 * @param service 接続されたServiceの{@link Binder}。コレを使ってサービスのインスタンスをひっぱってきていじくる
		 */
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			OverlayServiceBinder binder = (OverlayServiceBinder)service;
			overlayService = binder.getService();
			mBound = true;
		}

		/**
		 * Serviceから切り離された時に呼ばれる。ただし、正常に{@link Setting#unbindService(ServiceConnection)}された場合は呼ばれない。<br>
		 * これが呼ばれるのはクラッシュしたり強制終了された場合。
		 */
		@Override
		public void onServiceDisconnected(ComponentName className) {
			mBound = false;
		}
	};
}