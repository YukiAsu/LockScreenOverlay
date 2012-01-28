package jp.dip.sugarhouse.lockscreenoverlay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Activity�Ɋ֌W����View��\���������邽�߂̃T�[�r�X�B<br>
 * �T�[�r�X���X�^�[�g�����WindowManager���g����View���قڍőO�ʂɕ\��������B�T�[�r�X���I�����鎞��View��������ƌ�n�����邱�Ƃ��Y��Ȃ��B
 * Activity�Ƃ��č���Ă��܂��ƁAActivity�̃��C�t�T�C�N���ɏ]��Ȃ���΂Ȃ�Ȃ��ׂɊ��Ғʂ�̓����������̂��ʓ|�������B���Ԃ�ł��Ȃ��B
 * @author YukiAsu
 */
public class OverlayService extends Service {
	
	/** View��Activity�Ɋ֌W�Ȃ���ʂɕ��荞�ނ��߂�WindowManager */
	private WindowManager wm;
	/** ���������ɉ�ʂɕ\�������View */
	private View view;
	/** View�̃p�����[�^�B����ɂ���ă��b�N��ʂ̏�ɒ��������� */
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate(){
		// WindowManager���擾���āAxml����ȑf��View�𐶐����āAView�̑������������Ă���
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        view = layoutInflater.inflate(R.layout.overlay, null);
        params = new WindowManager.LayoutParams(
        	    WindowManager.LayoutParams.WRAP_CONTENT,
        	    WindowManager.LayoutParams.WRAP_CONTENT,
        	    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,			// ���b�N��ʂ���ɂ���
        	    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |	// View�̊O�̃^�b�`�C�x���g�ɂ���������H
        	    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |		// �^�b�`�C�x���g���E��Ȃ��B���b�N��ʂ��ז����Ȃ��B
        	    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,			// �t�H�[�J�X����Ȃ��B�n�[�h�L�[�őI�ڂ��Ƃ����Ă��A�����B
        	    PixelFormat.TRANSLUCENT); 						// �E�B���h�E�̓�����
	}
	
	/**
	 * �T�[�r�X�̋N�����ɌĂяo�����BView��\�����邾���B
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		Log.d("overlay","OverlayService start!");
    	wm.addView(view, params);    
	}
	
	/**
	 * �T�[�r�X�̏I�����ɌĂяo�����BView��������Ə����Ď��ʁB
	 */
	@Override
	public void onDestroy() {
		wm.removeView(view);
	}
}
