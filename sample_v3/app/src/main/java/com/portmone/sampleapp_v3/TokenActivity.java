package com.portmone.sampleapp_v3;


import static com.portmone.ecomsdk.util.Constant.BillCurrency.BYN;
import static com.portmone.ecomsdk.util.Constant.BillCurrency.EUR;
import static com.portmone.ecomsdk.util.Constant.BillCurrency.GBP;
import static com.portmone.ecomsdk.util.Constant.BillCurrency.KZT;
import static com.portmone.ecomsdk.util.Constant.BillCurrency.RUB;
import static com.portmone.ecomsdk.util.Constant.BillCurrency.UAH;
import static com.portmone.ecomsdk.util.Constant.BillCurrency.USD;
import static com.portmone.ecomsdk.util.Constant.Language.EN;
import static com.portmone.ecomsdk.util.Constant.Language.RU;
import static com.portmone.ecomsdk.util.Constant.Language.SYSTEM;
import static com.portmone.ecomsdk.util.Constant.Language.UK;
import static com.portmone.ecomsdk.util.Constant.Type.ACCOUNT;
import static com.portmone.ecomsdk.util.Constant.Type.DEFAULT;
import static com.portmone.ecomsdk.util.Constant.Type.PHONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.TokenPaymentParams;
import com.portmone.ecomsdk.data.contract_params.TokenPaymentContractParams;
import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.ui.token.payment.TokenPaymentContract;

public class TokenActivity
		extends AppCompatActivity
		implements View.OnClickListener {


	private Spinner spCurrency;
	private Spinner spLanguage;
	private Spinner spTypes;
	private EditText etPayeeId;
	private EditText etUID;
	private EditText etDescription;
	private EditText etAttribute1;
	private EditText etAttribute2;
	private EditText etAttribute3;
	private EditText etAttribute4;
	private EditText etAttribute5;
	private EditText etAmount;
	private EditText etAmountWithoutCvv;
	private EditText etBillNumber;
	private EditText etShopBillId;
	private CheckBox cbReceipt;
	private CheckBox cbDisableReturnToDetails;
	private CheckBox cbGPayEnabled;
	private CheckBox cbPrivatPayEnabled;
	private CheckBox cbOnlyGooglePay;
	private CheckBox cbGoogleTest;
	private CheckBox cbPreauth;
	private CheckBox cbFingerprint;
	private TextView tvResult;

	//@Constant$BillCurrency
	private String[] currencies = new String[]{
			UAH, USD, EUR, GBP, RUB, KZT, BYN
	};

	//@Constant$Language
	private String[] languages = new String[] {
			SYSTEM, UK, EN, RU
	};

	//@Constant$Type
	private int[] types = new int[] {
			DEFAULT,
			PHONE,
			ACCOUNT
	};

	private String[] typesTxt = new String[] {
			"DEFAULT",
			"PHONE",
			"ACCOUNT"
	};

	private String card;
	private String token;
	private String id;

	private ActivityResultLauncher<TokenPaymentContractParams> resultLauncher = registerForActivityResult(
			new TokenPaymentContract(),
			result -> {
				result.handleResult(
						(successResult) -> {
							tvResult.setText("Payment result:\n" + successResult.getBill().toString());
							return null;
						},
						(failureResult) -> {
							tvResult.setText("Payment error:\n" + "Code" + failureResult.getCode() +
									"\n" + failureResult.getMessage());
							return null;
						},
						() -> {
							tvResult.setText("Payment has been cancelled");
							return null;
						});
			}
	);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token);

		SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
		id = prefs.getString("id", null);
		card = prefs.getString("card", null);
		token = prefs.getString("token", null);

		tvResult = findViewById(R.id.tv_result);
		etUID = findViewById(R.id.et_uid);
		etPayeeId = findViewById(R.id.et_payee_id);
		etBillNumber = findViewById(R.id.et_bill_number);
		etShopBillId = findViewById(R.id.et_shop_bill_id);
		etDescription = findViewById(R.id.et_description);
		etAttribute1 = findViewById(R.id.et_attribute_1);
		etAttribute2 = findViewById(R.id.et_attribute_2);
		etAttribute3 = findViewById(R.id.et_attribute_3);
		etAttribute4 = findViewById(R.id.et_attribute_4);
		etAttribute5 = findViewById(R.id.et_attribute_5);
		etAmount = findViewById(R.id.et_amount);
		etAmountWithoutCvv = findViewById(R.id.et_amount_without_cvv);
		cbReceipt = findViewById(R.id.cb_receipt);
		cbDisableReturnToDetails = findViewById(R.id.cb_return_to_details);
		cbGPayEnabled = findViewById(R.id.cb_google_pay);
		cbOnlyGooglePay = findViewById(R.id.cb_only_google_pay);
		cbGoogleTest = findViewById(R.id.cb_test_google_pay);
		cbPrivatPayEnabled = findViewById(R.id.cb_privat);
		spCurrency = findViewById(R.id.sp_currency);
		spLanguage = findViewById(R.id.sp_language);
		spTypes = findViewById(R.id.sp_types);
		cbPreauth = findViewById(R.id.cb_preauth);
		cbFingerprint = findViewById(R.id.cb_fingerprint);

		ArrayAdapter<String> currencies = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		currencies.addAll(this.currencies);

		ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		languages.add("System");
		languages.add(this.languages[1]);
		languages.add(this.languages[2]);
		languages.add(this.languages[3]);

		ArrayAdapter<String> types = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		types.addAll(this.typesTxt);

		spCurrency.setAdapter(currencies);
		spCurrency.setSelection(0);

		spLanguage.setAdapter(languages);
		spLanguage.setSelection(0);

		etPayeeId.setText(id);
		//etPayeeId.setEnabled(false);

		spTypes.setAdapter(types);
		spTypes.setSelection(0);


		cbGPayEnabled.setOnCheckedChangeListener((compoundButton, checked) -> {
			if (checked) {
				cbOnlyGooglePay.setEnabled(true);
				cbGoogleTest.setEnabled(true);
				cbFingerprint.setEnabled(false);
				cbFingerprint.setChecked(false);
				cbPrivatPayEnabled.setEnabled(true);
			} else {
				cbOnlyGooglePay.setChecked(false);
				cbGoogleTest.setChecked(false);
				cbOnlyGooglePay.setEnabled(false);
				cbGoogleTest.setEnabled(false);
				cbFingerprint.setEnabled(true);
				cbPrivatPayEnabled.setEnabled(false);
				cbPrivatPayEnabled.setChecked(false);
			}
		});

		cbPrivatPayEnabled.setOnCheckedChangeListener((compoundButton, checked) -> {
			if (checked) {
				cbOnlyGooglePay.setEnabled(false);
				cbOnlyGooglePay.setChecked(false);
			} else {
				cbOnlyGooglePay.setEnabled(true);
			}
		});

		cbOnlyGooglePay.setOnCheckedChangeListener((buttonView, isChecked) -> {
			cbFingerprint.setEnabled(!isChecked);
			if (isChecked) {
				cbFingerprint.setChecked(false);
				cbPrivatPayEnabled.setEnabled(false);
				cbPrivatPayEnabled.setChecked(false);
			}
			else {
				cbPrivatPayEnabled.setEnabled(true);
			}
		});
	}

	@Override
	public void onClick(final View v) {
		if (v.getId() == R.id.btn_open_payment_screen) {
			if (id == null&&!cbOnlyGooglePay.isChecked()) {
				Toast.makeText(this, "No saved card", Toast.LENGTH_SHORT).show();
				return;
			}
			String uid = etUID.getText().toString();
			if (TextUtils.isEmpty(uid)) uid = null;
			PortmoneSDK.setUid(uid);

			final int selectedLanguageId = spLanguage.getSelectedItemPosition();
			PortmoneSDK.setLanguage(languages[selectedLanguageId]);

			PortmoneSDK.setFingerprintPaymentEnable(cbFingerprint.isChecked());

			AppStyle appStyle = PortmoneSDK.getAppStyle();
			if (appStyle == null) {
				appStyle = new AppStyle();
			}
			appStyle.setType(types[spTypes.getSelectedItemPosition()]);
			PortmoneSDK.setAppStyle(appStyle);

			if (etAmount.getText().toString().equals("")) {
				Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
				return;
			}

			if (etAmount.getText().toString().equals("")) {
				Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(etAmountWithoutCvv.getText())) {
				PortmoneSDK.setBillAmountWithoutCvvConfirmation(0);
			} else {
				PortmoneSDK.setBillAmountWithoutCvvConfirmation(Double.parseDouble(etAmountWithoutCvv.getText().toString()));
			}
			final TokenPaymentParams bigParams;
			if (cbGPayEnabled.isChecked()) {
				bigParams = new TokenPaymentParams(
						etPayeeId.getText().toString(),
						etBillNumber.getText().toString(),
						etShopBillId.getText().toString(),
						cbPreauth.isChecked(),
						currencies[spCurrency.getSelectedItemPosition()],
						etAttribute1.getText().toString(),
						etAttribute2.getText().toString(),
						etAttribute3.getText().toString(),
						etAttribute4.getText().toString(),
						etAttribute5.getText().toString(),
						Double.parseDouble(etAmount.getText().toString()),
						card,
						token,
						etDescription.getText().toString(),
						cbOnlyGooglePay.isChecked(),
						cbGoogleTest.isChecked(),
						cbPrivatPayEnabled.isChecked()
				);
			} else {
				bigParams = new TokenPaymentParams(
						etPayeeId.getText().toString(),
						etBillNumber.getText().toString(),
						etShopBillId.getText().toString(),
						cbPreauth.isChecked(),
						currencies[spCurrency.getSelectedItemPosition()],
						etAttribute1.getText().toString(),
						etAttribute2.getText().toString(),
						etAttribute3.getText().toString(),
						etAttribute4.getText().toString(),
						etAttribute5.getText().toString(),
						Double.parseDouble(etAmount.getText().toString()),
						card,
						token,
						etDescription.getText().toString()
				);
			}

			resultLauncher.launch(new TokenPaymentContractParams(
					bigParams,
					cbReceipt.isChecked(),
					!cbDisableReturnToDetails.isChecked()
			));
		}
	}
}