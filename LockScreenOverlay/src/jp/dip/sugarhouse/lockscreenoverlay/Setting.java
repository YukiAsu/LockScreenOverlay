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
 * �ݒ��ʁB�Ƃ������Ȃ�̂��Ƃ͖����{�^�������邾���Bstart��������service�������オ����View��\�����Astop��������service���E����View�ɂ͏����Ē����B
 * @author YukiAsu
 */
public class Setting extends Activity {
	
	/** �\�����Ǘ����Ă���Service�B{@link ServiceConnection}�ɂ���ăT�[�r�X����public���\�b�h�����育��Ăяo�� */
	private OverlayService overlayService;
	/** ����{@link Service}��bind����Ă�H���Ă����t���O�Btrue�Ȃ炳��Ă�B */
	private boolean mBound = false;

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
	 * {@link ServiceConnection}���g������Service�ɂ���public�̃��\�b�h��Activity����ł��Ăяo���܂��B<br>
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		/**
		 * @param className �ڑ����ꂽService�̖��O�Ƃ�������B
		 * @param service �ڑ����ꂽService��{@link Binder}�B�R�����g���ăT�[�r�X�̃C���X�^���X���Ђ��ς��Ă��Ă�������
		 */
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			OverlayServiceBinder binder = (OverlayServiceBinder)service;
			overlayService = binder.getService();
			mBound = true;
		}

		/**
		 * Service����؂藣���ꂽ���ɌĂ΂��B�������A�����{@link Setting#unbindService(ServiceConnection)}���ꂽ�ꍇ�͌Ă΂�Ȃ��B<br>
		 * ���ꂪ�Ă΂��̂̓N���b�V�������苭���I�����ꂽ�ꍇ�B
		 */
		@Override
		public void onServiceDisconnected(ComponentName className) {
			mBound = false;
		}
	};
}