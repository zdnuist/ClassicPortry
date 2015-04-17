package org.zdnuist.classicportry.adapter;

import java.util.ArrayList;
import java.util.List;

import org.zdnuist.classicportry.R;
import org.zdnuist.classicportry.entities.Portry;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyPageAdapter extends PagerAdapter{
	
	private List<Portry> portries;
	
	private List<View> portryViewGroup ;
	
	private LayoutInflater layoutInflater;
	
	public MyPageAdapter(Context context){
		layoutInflater = LayoutInflater.from(context);
		portryViewGroup = new ArrayList<View>();
	}

	@Override
	public int getCount() {
		return portryViewGroup.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(portryViewGroup.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View child = portryViewGroup.get(position);
		container.addView(child);
		TextView content = (TextView) child.findViewById(R.id.content);
		content.setText(portries.get(position).getContent());
		
		return child;
	}

	public void setPortries(List<Portry> portries) {
		this.portries = portries;
		for(int i = 0 ; i < portries.size(); i++){
			View v = layoutInflater.inflate(R.layout.portry_item, null);
			portryViewGroup.add(v);
		}
	}
	
	

}
