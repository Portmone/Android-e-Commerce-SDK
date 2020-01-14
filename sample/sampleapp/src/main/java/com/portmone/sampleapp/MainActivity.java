//  Created on 2/18/19.
//  Copyright © 2019 Portmone. All rights reserved.
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
package com.portmone.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;

public class MainActivity extends AppCompatActivity implements PortmoneSDK.PaymentCallback {

	private final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PortmoneSDK.setPaymentCallback(this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.card:
				startActivity(new Intent(this, CardActivity.class));
				break;
			case R.id.preauth:
				startActivity(new Intent(this, PreauthCardPreActivity.class));
				break;
			case R.id.token:
				startActivity(new Intent(this, TokenActivity.class));
				break;
			case R.id.token_transfer:
				startActivity(new Intent(this, TokenTransferActivity.class));
				break;
			case R.id.btn_open_theme_screen:
				startActivity(new Intent(this, ThemeConfigureActivity.class));
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PortmoneSDK.setPaymentCallback(null);
	}

	@Override
	public void paymentSuccess(Bill bill, boolean paidThroughGooglePay) {
		Log.d(TAG, "paymentSuccess() called with: bill = [" + bill + "], paidThroughGooglePay: " + paidThroughGooglePay);
	}

	@Override
	public void paymentError(int code, String message) {
		Log.d(TAG, "paymentError() called with: code = [" + code + "], message = [" + message + "]");
	}
}
