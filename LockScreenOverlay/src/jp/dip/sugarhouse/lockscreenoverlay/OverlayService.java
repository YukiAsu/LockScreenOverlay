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
 * Activity�Ɋ֌W����View��\���������邽�߂̃T�[�r�X�B<br>
 * �T�[�r�X���X�^�[�g�����WindowManager���g����View���قڍőO�ʂɕ\��������B�T�[�r�X���I�����鎞��View��������ƌ�n�����邱�Ƃ��Y��Ȃ��B
 * Activity�Ƃ��č���Ă��܂��ƁAActivity�̃��C�t�T�C�N���ɏ]��Ȃ���΂Ȃ�Ȃ��ׂɊ��Ғʂ�̓����������̂��ʓ|�������B���Ԃ�ł��Ȃ��B
 * @author YukiAsu
 */
public class OverlayService extends Service {

	/** {@link Binder}�B{@link #onBind(Intent)}�̖߂�l��{@link IBinder}�Ȃ̂�IBinder�ɂȂ��Ă�B���g��{@link Binder}��extends */
	public final IBinder mBinder = new OverlayServiceBinder();

	/** View��Activity�Ɋ֌W�Ȃ���ʂɕ��荞�ނ��߂�WindowManager */
	private WindowManager wm;
	/** ���������ɉ�ʂɕ\�������View */
	private FrameLayout view;
	/** View�̃p�����[�^�B����ɂ���ă��b�N��ʂ̏�ɒ��������� */
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate(){
		// WindowManager���擾���āAxml����ȑf��View�𐶐����āAView�̑������������Ă���
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		this.view = new FrameLayout(getApplicationContext());
		((FrameLayout)this.view).addView(layoutInflater.inflate(R.layout.overlay, null));
        params = new WindowManager.LayoutParams(
        	    WindowManager.LayoutParams.WRAP_CONTENT,
        	    WindowManager.LayoutParams.WRAP_CONTENT,
        	    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,			// ���b�N��ʂ���ɂ���
        	    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |		// �^�b�`�C�x���g���E��Ȃ��B���b�N��ʂ��ז����Ȃ��B
        	    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,			// �t�H�[�J�X����Ȃ��B�n�[�h�L�[�őI�ڂ��Ƃ����Ă��A�����B
        	    PixelFormat.TRANSLUCENT); 						// �E�B���h�E�̓�����

    	wm.addView(view, params);
	}
	
	/**
	 * �\������View��resourceID���w�肷��
	 * @param resource �\����������resource
	 */
	public void setView(int resource){
		view.removeAllViews();
		LayoutInflater layoutInflater = LayoutInflater.from(this);
        this.view.addView(layoutInflater.inflate(R.layout.overlay, null));
	}
	
	/**
	 * �\������View�𒼐ڎw�肷��
	 * @param view �\����������View
	 */
	public void setView(View view){
		this.view.removeAllViews();
		this.view.addView(view);
	}
	
	/**
	 * �T�[�r�X�̋N�����ɌĂяo�����BView��\�����邾���B
	 */
	@Override
	public void onStart(Intent intent, int startId) {
	}
	
	/**
	 * �T�[�r�X�̏I�����ɌĂяo�����BView��������Ə����Ď��ʁB
	 */
	@Override
	public void onDestroy() {
		wm.removeView(view);
	}

	/**
	 * bind���邽�߂̋��n���A{@link Binder}<br>
	 * �R������Ď������g��n���Ă����鎖�ŁAService��public���\�b�h���g�p�ł���悤�ɂȂ�܂��B
	 */
	public class OverlayServiceBinder extends Binder {
		OverlayService getService() {
			// �N���C�A���g��public���\�b�h���Ăяo����悤�ɁA�������g�̃C���X�^���X��Ԃ��B
			return OverlayService.this;
		}
	}
}
