package com.activity.test;

import com.activity.MyFirstActivity;
import static android.test.ViewAsserts.assertOnScreen;
import static android.test.ViewAsserts.assertRightAligned;
import static android.test.MoreAsserts.assertNotContainsRegex;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ��������׿����demo
 * @author PJY
 * ʱ��:2017.3.15
 *
 */
public class MyFirstActivityText extends ActivityInstrumentationTestCase2<MyFirstActivity> {
	//��������
	MyFirstActivity mActivity;
	private EditText mMessage;
	private Button mOK;
	private TextView mLink;
	private Instrumentation mInstrumentation;
	
	
	/**
	 * �޲������췽��
	 * �ڲ�������ʱ���в����Ĺ��췽�����ã�ͨ��Ҳ������Ӧ�������л�
	 */
	public MyFirstActivityText() {
		this("MyFirstActivityText");
	}
	/**
	 * �в������췽��
	 * @param name
	 * ����������ڲ��Ա����У�����������ʶ��ʧ�ܵĲ�������
	 * 
	 * ��һЩ��չTestCase���࣬���ṩһ���������Ĺ��췽������ʱ��ʹ��setName()��������
	 */
	public MyFirstActivityText(String name) {
		super(MyFirstActivity.class);
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		/**
		 * ����Ϊfalseʱ��ָ������ʹ��sengding key events��
		 * setActivityInitialTouchMode()����������getActivity()��������֮ǰ������
		 */
		setActivityInitialTouchMode(false);
		mActivity=getActivity();
		//Instrumentation�������ڼ��ϵͳ��Ӧ�ó����Activity֮��Ľ�����
		mInstrumentation=getInstrumentation();
		//��ȡ�����findViewById()�����в���ӦΪ�����������
		mLink=(TextView)mActivity.findViewById(com.activity.R.id.link);
		mMessage=(EditText)mActivity.findViewById(com.activity.R.id.message);
		mOK=(Button)mActivity.findViewById(com.activity.R.id.capitalize);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * ��ȡ��������һ������ȡ��������
	 * mActivity.getWindow().getDecorView();
	 * 
	 * �ж�mMessage��mOK��ť�Ƿ���������
	 */
	public void testUserInterfaceLayout(){
		final int margin=0;
		//Ѱ��mActivity�ĸ�����
		final View origin=mActivity.getWindow().getDecorView();
		//�ж�mMessage�����Ƿ���������
		assertOnScreen(origin,mMessage);
		//�ж�mOK��ť�Ƿ���������
		assertOnScreen(origin, mOK);
		//�ж�mMessage��mOK��ť�Ƿ��Ҷ���
		assertRightAligned(mMessage, mOK,margin);
	}
	/**
	 * ��ȡ����������������ȡ��������
	 * mMessage.getRootView();
	 * 
	 * mMessage�ڸ������ϣ������ֻ�ȡ�����Ľ����һ�µ�
	 * 
	 * �ж�mMessage��mOK��ť�Ƿ���������
	 */
	public void testUserInterfaceLayoutWithOtherOrigin(){
		final int margin=0;
		//Ѱ��mMessage�ĸ�����
		View origin=mMessage.getRootView();
		assertOnScreen(origin, mMessage);
		origin=mOK.getRootView();
		assertOnScreen(origin, mOK);
		assertRightAligned(mMessage, mOK, margin);
	}
	/**
	 * @UiThreadTest ͨ���̸߳ı����
	 * 
	 * ���������
	 */
	@UiThreadTest
	public void testNoErrorInCapitalization(){
		final String msg="this is a sample";
		//���ı��������ַ���
		mMessage.setText(msg);
		//�Զ������ť
		mOK.performClick();
		final String actual=mMessage.getText().toString();
		//��Сд
		final String notExpectedRegexp="(?i:ERROR)";
		//�ж�notExpectedRegexp�Ƿ���actual���Ӽ�
		assertNotContainsRegex("OK found error:", notExpectedRegexp,actual);
	}
	public void requestMessageFocus(){
		try {
			//�����߳�
			runTestOnUiThread(new Runnable() {
				public void run() {
					//�����ı����ȡ����
					mMessage.requestFocus();
				}
			});
		} catch (Throwable e) {
			fail("Couldn't set focus");
		}
		//waitForIdleSync()������ʾͬ���ȴ�Ӧ�ó�������
		mInstrumentation.waitForIdleSync();	
	}
	/**
	 * ���Լ��̣��Լ��̵Ĳ���������һ��
	 * sendkeys
	 */
	public void testSendKeys(){
		requestMessageFocus();
		//�����̷�������ʹ�����
		sendKeys(KeyEvent.KEYCODE_H,
				KeyEvent.KEYCODE_E,
				KeyEvent.KEYCODE_E,
				KeyEvent.KEYCODE_E,
				KeyEvent.KEYCODE_Y,
				KeyEvent.KEYCODE_ALT_LEFT,
				KeyEvent.KEYCODE_1,
				KeyEvent.KEYCODE_DPAD_DOWN,
				KeyEvent.KEYCODE_ENTER
				);
		//��������ֵ
		final String expected="HEEEY1";
		//��ȡʵ��ֵ
		final String actual=mMessage.getText().toString();
		//�ж�����ֵ��ʵ��ֵ�Ƿ����
		assertEquals(expected, actual);
	}
	/**
	 * ���Լ��̣��Լ��̵Ĳ�������������
	 * sendkeys
	 */
	public void testSendKeyString(){
		requestMessageFocus();
		//�����̷�������ʹ�����
		sendKeys("H 3*E Y ALT_LEFT 1 DPAD_DOWN ENTER");
		//��������ֵ
		final String expected="HEEEY1";
		//��ȡʵ��ֵ
		final String actual=mMessage.getText().toString();
		//�ж�����ֵ��ʵ��ֵ�Ƿ����
		assertEquals(expected, actual);
	}
	/**
	 * ���Լ��̣��Լ��̵Ĳ�������������
	 * sendRepeatedkeys
	 */
	public void testSendRepeatedKeys(){
		requestMessageFocus();
		//�����̷�������ʹ�����
		sendRepeatedKeys(1,KeyEvent.KEYCODE_H,
				3,KeyEvent.KEYCODE_E,
				1,KeyEvent.KEYCODE_Y,
				1,KeyEvent.KEYCODE_ALT_LEFT,
				1,KeyEvent.KEYCODE_1,
				1,KeyEvent.KEYCODE_DPAD_DOWN,
				1,KeyEvent.KEYCODE_ENTER
				);
		//��������ֵ
		final String expected="HEEEY1";
		//��ȡʵ��ֵ
		final String actual=mMessage.getText().toString();
		//�ж�����ֵ��ʵ��ֵ�Ƿ����
		assertEquals(expected, actual);
	}
	/**
	 * �����ӽ��в���
	 * ���裺
	 * 1�����Instrumentation����
	 * 2�����һ��IntentFilter�����
	 * 3���ȴ�activity��Ӧ
	 * 4��ȷ�ϱ����ĵ���������
	 * 5���Ƴ����
	 */
	public void testFollowLink(){
		//���һ��Instrumentation����
		//������������Ӧ�ó����activity�Ͱ�׿ϵͳ���������й���
		final Instrumentation inst=getInstrumentation();
		//���һ��IntentFilter�������Intent.ACTION_VIEW������ʾ�û�����
		IntentFilter intentFilter=new IntentFilter(Intent.ACTION_VIEW);
		//��ӡ�http��Э��
		intentFilter.addDataScheme("http");
		//ָ�����������
		intentFilter.addCategory(Intent.CATEGORY_BROWSABLE);
		//ʵ��ActivityMonitor
		//ActivityMonitor��������Ӧ���е����������������ָ������ͼ
		/**
		 * ͨ��Instrumentation.addMonitor�����ʵ��
		 * ���������ϵͳ��ƥ��Instrumentation�е�ActivityMonitorʵ���б�
		 * ���ƥ�䣬�ͻ��ۼӼ�����
		 */
		ActivityMonitor monitor=inst.addMonitor(intentFilter, null, false);
		//��ȡ����Ĵ���
		assertEquals(0, monitor.getHits());
		//TouchUtils���ṩ�˴���ģ��������˴�Ϊģ����
		TouchUtils.clickView(this, mLink);
		//���õȴ�5��
		monitor.waitForActivityWithTimeout(5000);
		//�ٴλ�ȡ�������
		assertEquals(1, monitor.getHits());
		//�������
		inst.removeMonitor(monitor);
	}

}
