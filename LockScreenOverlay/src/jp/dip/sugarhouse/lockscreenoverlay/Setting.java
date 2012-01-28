package jp.dip.sugarhouse.lockscreenoverlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * �ݒ��ʁB�Ƃ������Ȃ�̂��Ƃ͖����{�^�������邾���Bstart��������service�������オ����View��\�����Astop��������service���E����View�ɂ͏����Ē����B
 * @author YukiAsu
 */
public class Setting extends Activity {
	/** Activity����������鎞�ɌĂ΂�� */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Service���Ăяo���ׂ�Intent�𐶐�
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