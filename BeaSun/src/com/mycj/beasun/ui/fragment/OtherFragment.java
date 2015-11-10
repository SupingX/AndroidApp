package com.mycj.beasun.ui.fragment;

import com.mycj.beasun.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 更多Fragment
 * @author Administrator
 *
 */
public class OtherFragment extends Fragment implements OnClickListener{
	public static final String ARGUMENT = "argument";
	private RelativeLayout rlUserInfo;
	private RelativeLayout rlFaq;
	private RelativeLayout rlFeedBack;  
	 
	    /** 
	     * 传入需要的参数，设置给arguments 
	     * @param argument 
	     * @return 
	     */  
	    public static OtherFragment newInstance(String argument) {  
	        Bundle bundle = new Bundle();  
	        bundle.putString(ARGUMENT, argument);  
	        OtherFragment otherFragment = new OtherFragment();  
	        //setArguments方法必须在fragment创建以后，添加给Activity前完成
	        otherFragment.setArguments(bundle);  
	        return otherFragment;  
	    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bea_other,container,false);
		rlUserInfo = (RelativeLayout) view.findViewById(R.id.rl_user_info);
		rlFaq = (RelativeLayout) view.findViewById(R.id.rl_faq);
		rlFeedBack = (RelativeLayout) view.findViewById(R.id.rl_feedback);
		rlFeedBack.setOnClickListener(this);
		rlFaq.setOnClickListener(this);
		rlUserInfo.setOnClickListener(this);
		return  view;
	}
	@Override
	public void onClick(View v) {
		if (mOnOtherFragmentViewClickListener!=null) {
			mOnOtherFragmentViewClickListener.onClick(v);
		}
	}
	
	/**
	 * 其他页面中控件点击事件
	 * @author Administrator
	 *
	 */
	public interface OnOtherFragmentViewClickListener{
		public void onClick(View v);
	}
	private OnOtherFragmentViewClickListener mOnOtherFragmentViewClickListener;
	public void setOnOtherFragmentViewClickListener(OnOtherFragmentViewClickListener l){
		this.mOnOtherFragmentViewClickListener= l;
	}
	
	
}
