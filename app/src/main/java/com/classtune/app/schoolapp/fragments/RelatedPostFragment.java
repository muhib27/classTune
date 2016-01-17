/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.classtune.app.schoolapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.FreeVersionPost;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class RelatedPostFragment extends Fragment {

	private static final String ARG_POSITION = "post_id";
	private String postId;
	private View view;
	private LinearLayout relatedPostScrollView;
	private ProgressBar pb;
	private List<FreeVersionPost> posts;

	public static RelatedPostFragment newInstance(String id) {
		RelatedPostFragment f = new RelatedPostFragment();
		Bundle b = new Bundle();
		b.putString(ARG_POSITION, id);
		f.setArguments(b);
		return f;
	}

	private void updateUi()
	{
		if(posts.size()==0)
		{
			view.setVisibility(View.GONE);
			return;
		}
		else
		{
			view.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < posts.size(); i++) {
			FreeVersionPost post=posts.get(i);
			View child = getActivity().getLayoutInflater().inflate(
					R.layout.cell_grid_related_post, null);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					AppUtility.convertDipToPixels(getActivity(), 200f),
					LayoutParams.MATCH_PARENT);
			params.setMargins(0, 0, 30, 0);
			
			child.setTag(post.getId());

			LinearLayout textLayout = (LinearLayout) child
					.findViewById(R.id.text_layout);
			textLayout.getBackground().setAlpha(170);
			
			TextView title=(TextView)child.findViewById(R.id.title);
			title.setText(post.getTitle());
			
			final ProgressBar pbImage=(ProgressBar)child.findViewById(R.id.pb_cell);
			if(post.getImages().size()>0)
			{
				ImageView image=(ImageView)child.findViewById(R.id.icon);
				ImageLoader.getInstance().displayImage(post.getImages().get(0),
						image, AppUtility.getOption(),
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri, View view) {
								pbImage.setVisibility(View.VISIBLE);

							}

							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								// spinner.setVisibility(View.GONE);
								pbImage.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								pbImage.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {

							}
						});
			}
			
			child.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(getActivity(), "Related child clicked id: "+((String)v.getTag()), Toast.LENGTH_SHORT).show();
					/*((SingleItemShowActivity) getActivity()).initApiCall(((String)v.getTag()), null);
					getActivity().getSupportFragmentManager().beginTransaction().remove(RelatedPostFragment.this).commit();*/
				
					
				}
			});
			
			relatedPostScrollView.addView(child, params);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(AppUtility.isInternetConnected())
		{
			fetchRelatedPost();
		}
		
	}

	private void fetchRelatedPost()
	{
		RequestParams params=new RequestParams();
		params.put("id",postId);
		Log.e("ID", postId);
		AppRestClient.post(URLHelper.URL_FREE_VERSION_RELATED_NEWS, params, relatedNewsHandler);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		postId = getArguments().getString(ARG_POSITION);
		posts=new ArrayList<FreeVersionPost>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.related_post_layout, container, false);

		relatedPostScrollView = (LinearLayout) view
				.findViewById(R.id.related_scroll_view);
		pb = (ProgressBar) view.findViewById(R.id.pb);

		return view;
	}

	AsyncHttpResponseHandler relatedNewsHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			pb.setVisibility(View.GONE);
		};

		@Override
		public void onStart() {
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		public void onSuccess(int arg0, String responseString) {
			pb.setVisibility(View.GONE);
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
				posts.clear();
				posts.addAll(GsonParser.getInstance()
						.parseFreeVersionPost(
								modelContainer.getData().getAsJsonArray("post")
										.toString()));
				if(getActivity()==null)
					return;
				updateUi();

			}

		}

	};

}