//package com.vinux.suannipurse;
//
//import android.content.pm.ActivityInfo;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.vinux.suannipurse.application.MyApplication;
//import com.vinux.suannipurse.myset.MySetFragment;
//import com.vinux.suannipurse.util.LogUtil;
//import com.vinux.suannipurse.util.SPHelper;
//import com.vinux.suannipurse.util.StatusBarUtils;
//import com.vinux.suannipurse.util.ToastUtil;
//import com.vinux.suannipurse.wallet.homeMain.WalletFragment;
//
//import java.util.Set;
//
//import cn.jpush.android.api.JPushInterface;
//import cn.jpush.android.api.TagAliasCallback;
//
//public class MainActivity extends FragmentActivity implements View.OnClickListener {
//
//	private FragmentManager fm;
//	private LinearLayout home_wallet, home_my;
//	private ImageView home_wallet_image, home_my_image;
//	private TextView home_wallet_text, home_my_text;
//
//	private long exitTime = 0;
//
//
//	private MyApplication application;//添加到appliction退出用
//
//
//	@Override
//	protected void onCreate(@Nullable Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// 不显示程序的标题
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		//竖屏
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		//软键盘设置
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		setContentView(R.layout.activity_main);
//
//		/**
//		 * 设置状态栏透明黑字体
//		 */
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			LogUtil.i("6.0以上");
//			StatusBarUtils.setTranslucentStatus(this);
//			StatusBarUtils.setImmersiveStatusBar(this,true);
//
//		}else {
//			LogUtil.i("6.0以下");
//			StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.transparentColor));
//		}
//
//		initFindViewById();//实例化布局
//		init();
//
//		if (application == null) {
//			// 得到Application对象
//			application = (MyApplication) getApplication();
//		}
//		addActivity();// 调用添加方法
//
//	}
//
//	// 添加Activity方法
//	public void addActivity() {
//		application.addActivity_(this);// 调用myApplication的添加Activity方法
//	}
//
//	private void initFindViewById() {
//		fm = getSupportFragmentManager();
//		home_wallet = findViewById(R.id.home_wallet);
//		home_my = findViewById(R.id.home_my);
//		home_wallet_image = findViewById(R.id.home_wallet_image);
//		home_my_image = findViewById(R.id.home_my_image);
//		home_wallet_text = findViewById(R.id.home_wallet_text);
//		home_my_text = findViewById(R.id.home_my_text);
//
//		home_wallet.setOnClickListener(this);
//		home_my.setOnClickListener(this);
//
//	}
//
//	private void init() {
//		SPHelper spHelper = new SPHelper(this, "suanni");
//		String userId = spHelper.getString("userId");
//
//		home_wallet_text.setTextColor(getResources().getColor(R.color.greenColor));
//		home_wallet_image.setImageDrawable(getResources().getDrawable(R.drawable.icon_wallet));
//
//		FragmentTransaction transaction1 = fm.beginTransaction();
//		WalletFragment walletFragment = new WalletFragment();
//		transaction1.replace(R.id.fragment, walletFragment);
//		transaction1.addToBackStack(null);
//		transaction1.commitAllowingStateLoss();
//
//
//		JPushInterface.init(getApplicationContext());
////		JPushInterface.setAlias(getApplicationContext(), 0, userId);
////		设置别名
//		JPushInterface.setAlias(this, //上下文对象
//				userId, //别名
//				new TagAliasCallback() {//回调接口,i=0表示成功,其它设置失败
//					@Override
//					public void gotResult(int i, String s, Set<String> set) {
////						Log.d("alias", "set alias result is" + i);
//						LogUtil.i("别名0成功   " + i);
//					}
//				});
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.home_wallet:
//				restartBotton();
//				home_wallet_text.setTextColor(getResources().getColor(R.color.greenColor));
//				home_wallet_image.setImageDrawable(getResources().getDrawable(R.drawable.icon_wallet));
//
//				FragmentTransaction transaction1 = fm.beginTransaction();
//				 WalletFragment walletFragment = new WalletFragment();
//				transaction1.replace(R.id.fragment, walletFragment);
//				transaction1.addToBackStack(null);
//				transaction1.commitAllowingStateLoss();
//				break;
//			case R.id.home_my:
//				restartBotton();
//				home_my_text.setTextColor(getResources().getColor(R.color.greenColor));
//				home_my_image.setImageDrawable(getResources().getDrawable(R.drawable.icon_my));
//
//				FragmentTransaction transaction2 = fm.beginTransaction();
//				MySetFragment mySetFragment = new MySetFragment();
//				transaction2.replace(R.id.fragment, mySetFragment);
//				transaction2.addToBackStack(null);
//				transaction2.commit();
//				break;
//
//			default:
//				break;
//		}
//	}
//
//	/**
//	 * 恢复默认字图
//	 */
//	private void restartBotton() {
//		home_wallet_text.setTextColor(getResources().getColor(R.color.hintColor));
//		home_my_text.setTextColor(getResources().getColor(R.color.hintColor));
//
//		home_wallet_image.setImageDrawable(getResources().getDrawable(R.drawable.icon_wallet_un));
//		home_my_image.setImageDrawable(getResources().getDrawable(R.drawable.icon_my_un));
//
//	}
//
//
//	// 退出程序
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//			if ((System.currentTimeMillis() - exitTime) > 2000) {
//				ToastUtil.showToast(this, "再按一次退出程序");
//				exitTime = System.currentTimeMillis();
//			} else {
//				finish();
//				System.exit(0);
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//}
