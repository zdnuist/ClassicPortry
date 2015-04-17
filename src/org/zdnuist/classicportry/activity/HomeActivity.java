package org.zdnuist.classicportry.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zdnuist.classicportry.R;
import org.zdnuist.classicportry.adapter.MyPageAdapter;
import org.zdnuist.classicportry.entities.Portry;
import org.zdnuist.classicportry.fragment.LoadingFragment;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class HomeActivity extends Activity {
	
	public static final String TAG = "HomeActivity";

	private ViewPager viewPager;

	private MyPageAdapter myPageAdapter;

	private List<Portry> contents;

	LoadingFragment loading;

	DbUtils db;

	Portry portry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acivity_home);

		loading = new LoadingFragment();

		db = DbUtils.create(this, "portries.db");

		viewPager = (ViewPager) findViewById(R.id.myPager);
		new ContentAsnycTask().execute();
	}

	private void initDatas() {
		myPageAdapter = new MyPageAdapter(this);
		myPageAdapter.setPortries(contents);
		viewPager.setAdapter(myPageAdapter);

	}

	private class ContentAsnycTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				Log.w(TAG, "begin findAll");
				contents = db.findAll(Portry.class);
				if(contents != null){
				Log.w(TAG, "end findAll size:" + contents.size());
				}
				if (contents == null || contents.size() <= 0) {
					contents = new ArrayList<Portry>();
					getAllLink();
					Log.w(TAG, "begin saveAll");
					db.saveAll(contents);
					Log.w(TAG, "end saveAll");
				}

				if (contents.size() > 0)
					return true;

			} catch (DbException e) {
				e.printStackTrace();
			}
			return false;
		}

		/**
		 * 获取到具体的内容
		 * 
		 * @param url
		 */
		public void parseXINQIJIFromNet(String url) {
			try {
				Document doc = Jsoup.connect(url).timeout(3 * 1000).get();
				Elements content = doc.select(".content");
				Element body = content.get(0);
				String content_s = body.text().replaceAll("\\s+", "\n");

				portry = new Portry();
				portry.setContent(content_s);
				contents.add(portry);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 取得所有链接
		 */
		public void getAllLink() {
			try {
				Document doc = Jsoup
						.connect("http://www.xigutang.com/songci/xinqiji/")
						.timeout(3 * 1000).get();
				Elements listbox = doc.select(".listbox");
				Element list = listbox.first();
				Elements links = list.select("a[href]");
				System.out.println(links.size());
				for (Element link : links) {
					parseXINQIJIFromNet(link.attr("abs:href"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				initDatas();
			}

			loading.dismissAllowingStateLoss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			loading.show(getFragmentManager(), "loading");
		}

	}
}
