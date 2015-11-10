package com.mycj.beasun.ui.fragment;

import com.mycj.beasun.R;
import com.mycj.beasun.view.BeaMainLinearLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MainFragment extends Fragment implements OnClickListener{

	private ImageView imgZhineng;
	private ImageView imgRounie;
	private ImageView imgTuina;
	private ImageView imgGuasha;
	private ImageView imgShoushen;
	private ImageView imgQinfu;
	private ImageView imgZhenjiu;
	private ImageView imgChuiji;
	private ImageView imgZhiya;
	private ImageView imgJinzhui;
	private ImageView imgHuoguan;
	 public static final String ARGUMENT = "argument";  
	 
    /** 
     * 传入需要的参数，设置给arguments 
     * @param argument 
     * @return 
     */  
    public static MainFragment newInstance(String argument) {  
        Bundle bundle = new Bundle();  
        bundle.putString(ARGUMENT, argument);  
        MainFragment mainFragment = new MainFragment();  
        //setArguments方法必须在fragment创建以后，添加给Activity前完成
        mainFragment.setArguments(bundle);  
        return mainFragment;  
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View mainView = inflater.inflate(R.layout.fragment_bea_main,container,false);
		 BeaMainLinearLayout beaMain = (BeaMainLinearLayout) mainView.findViewById(R.id.bea_main);
//			imgZhineng = (ImageView) mainView.findViewById(R.id.img_main_zhineng);
//			imgRounie = (ImageView) mainView.findViewById(R.id.img_main_rounie);
//			imgTuina = (ImageView) mainView.findViewById(R.id.img_main_tuina);
//			imgGuasha = (ImageView) mainView.findViewById(R.id.img_main_guasha);
//			imgShoushen = (ImageView) mainView.findViewById(R.id.img_main_shoushen);
//			imgQinfu = (ImageView) mainView.findViewById(R.id.img_main_qinfu);
//			imgZhenjiu = (ImageView) mainView.findViewById(R.id.img_main_zhenjiu);
//			imgChuiji = (ImageView) mainView.findViewById(R.id.img_main_chuiji);
//			imgZhiya = (ImageView) mainView.findViewById(R.id.img_main_zhiya);
//			imgJinzhui = (ImageView) mainView.findViewById(R.id.img_main_jinzhui);
//			imgHuoguan = (ImageView) mainView.findViewById(R.id.img_main_huoguan);
//
		 
		 imgZhineng =  beaMain.getImageView(0);
		 imgRounie =  beaMain.getImageView(1);
		 imgTuina =  beaMain.getImageView(2);
		 imgGuasha =  beaMain.getImageView(3);
		 imgShoushen =  beaMain.getImageView(4);
		 imgQinfu =  beaMain.getImageView(5);
		 imgZhenjiu =  beaMain.getImageView(6);
		 imgChuiji =  beaMain.getImageView(7);
		 imgZhiya =  beaMain.getImageView(8);
		 imgJinzhui =  beaMain.getImageView(9);
		 imgHuoguan =  beaMain.getImageView(10);
			imgZhineng.setOnClickListener(this);
			imgRounie.setOnClickListener(this);
			imgTuina.setOnClickListener(this);
			imgGuasha.setOnClickListener(this);
			imgShoushen.setOnClickListener(this);
			imgQinfu.setOnClickListener(this);
			imgZhenjiu.setOnClickListener(this);
			imgChuiji.setOnClickListener(this);
			imgJinzhui.setOnClickListener(this);
			imgHuoguan.setOnClickListener(this);
			imgZhiya.setOnClickListener(this);
			ScrollView sc = (ScrollView) mainView.findViewById(R.id.sc);
			sc.requestDisallowInterceptTouchEvent(false);
			return mainView;
	}
	
	@Override
	public void onClick( final View v) {
		
		ObjectAnimator animator =  ObjectAnimator.ofFloat(v, "alpha", 1f,0.8f,0.6f,0.2f,0.6f,1f);
		animator.setDuration(200);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				if (mOnMainFragmentViewOnClickListener != null) {
					mOnMainFragmentViewOnClickListener.onClick(v);
				}
			}
		});
		animator.start();
	}
	
	public OnMainFragmentViewOnClickListener mOnMainFragmentViewOnClickListener;

	public void setOnMainFragmentViewOnClickListener(OnMainFragmentViewOnClickListener l) {
		this.mOnMainFragmentViewOnClickListener = l;
	}
	/**
	 * 主页面Fragment 控件点击回调
	 * @author Administrator
	 *
	 */
	public interface OnMainFragmentViewOnClickListener {
		public void onClick(View v);
	}
}
