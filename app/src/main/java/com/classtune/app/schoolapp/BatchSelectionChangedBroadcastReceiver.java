package com.classtune.app.schoolapp;

/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BatchSelectionChangedBroadcastReceiver extends
		WakefulBroadcastReceiver {

	public BatchSelectionChangedBroadcastReceiver(
			onBatchIdChangeListener mListener) {
		super();
		this.mListener = mListener;
	}

	public interface onBatchIdChangeListener {
		public void update(String batchId, String schoolId);
	}

	onBatchIdChangeListener mListener;

	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		String batchId = intent.getStringExtra("batch_id");
		String schoolId = "";
		schoolId = intent.getStringExtra("student_id");
		mListener.update(batchId, schoolId);
	}
}
