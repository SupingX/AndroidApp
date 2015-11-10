package com.mycj.beasun;

import com.mycj.beasun.service.MusicService;
import com.mycj.beasun.service.XplBluetoothService;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment{
//	public AbstractSimpleBlueService getSimpleBlueService(){
//		BaseApp app = (BaseApp) getActivity().getApplication();
//		return app.getSimpleBlueService();
//	}
	
	public MusicService getMusicService(){
		BaseApp app = (BaseApp) getActivity().getApplication();
		return app.getMusicService();
	}
	public XplBluetoothService getxplBluetoothService(){
		BaseApp app = (BaseApp) getActivity().getApplication();
		return app.getXplBluetoothService();
	}
}
