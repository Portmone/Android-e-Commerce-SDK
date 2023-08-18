package com.portmone.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
		TextView tvVersion = findViewById(R.id.tv_version);
		tvVersion.setText("Version SDK: " + PortmoneSDK.getVersionSDK());
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
			case R.id.preauth_pay_bills:
				startActivity(new Intent(this, PreauthPayBillsPreActivity.class));
				break;
			case R.id.pay_bills:
				startActivity(new Intent(this, PayBillsActivity.class));
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
