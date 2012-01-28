package jp.dip.sugarhouse.lockscreenoverlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 設定画面。というよりなんのことは無くボタンがあるだけ。startを押せばserviceが立ち上がってViewを表示し、stopを押せばserviceを殺してViewには消えて頂く。
 * @author YukiAsu
 */
public class Setting extends Activity {
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
			}
		});

		((Button)findViewById(R.id.stop)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(overlayService);
			}
		});
	}
}