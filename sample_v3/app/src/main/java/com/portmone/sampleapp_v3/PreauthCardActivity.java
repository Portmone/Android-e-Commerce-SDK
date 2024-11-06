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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;
import com.portmone.ecomsdk.data.SaveCardParams;
import com.portmone.ecomsdk.ui.savecard.PreauthCardContract;

import java.util.Calendar;

public class PreauthCardActivity
		extends AppCompatActivity
		implements View.OnClickListener {

	private Spinner spLanguage;
	private EditText etUID;
	private EditText etPayeeId;
	private EditText etDescription;
	private EditText etEmailAddress;
	private TextView tvResult;

	private String[] currencies = new String[]{
			UAH, USD, EUR, GBP, RUB, KZT, BYN
	};

	private String[] languages = new String[]{
			SYSTEM, UK, EN, RU
	};

	private ActivityResultLauncher<SaveCardParams> resultLauncher = registerForActivityResult(
			new PreauthCardContract(),
			result -> {
				result.handleResult(
						(successResult) -> {
							tvResult.setText("Payment result:\n" + successResult.getBill().toString());
							saveCard(successResult.getBill());
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
		setContentView(R.layout.preauth_main);
		tvResult = findViewById(R.id.tv_result);
		etUID = findViewById(R.id.et_uid);
		etPayeeId = findViewById(R.id.et_payee_id);

		etDescription = findViewById(R.id.et_description);
		etEmailAddress = findViewById(R.id.et_email);

		spLanguage = findViewById(R.id.sp_language);

		ArrayAdapter<String> currencies = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		currencies.addAll(this.currencies);

		ArrayAdapter<String> languages = new ArrayAdapter<>(this, R.layout.layout_spinner, R.id.txt_spinner);
		languages.add("System");
		languages.add(this.languages[1]);
		languages.add(this.languages[2]);
		languages.add(this.languages[3]);

		spLanguage.setAdapter(languages);
		spLanguage.setSelection(0);
	}


	@Override
	public void onClick(final View v) {
		if (v.getId() == R.id.btn_open_preauth) {
			String uid = etUID.getText().toString();
			if (TextUtils.isEmpty(uid)) uid = null;
			PortmoneSDK.setUid(uid);


			final int selectedLanguageId = spLanguage.getSelectedItemPosition();
			PortmoneSDK.setLanguage(languages[selectedLanguageId]);

			final SaveCardParams bigParams = new SaveCardParams(
					etPayeeId.getText().toString(),
					etDescription.getText().toString(),
					generateNumber(),
					etEmailAddress.getText().toString()
			);

			resultLauncher.launch(bigParams);
		}
	}

	private String generateNumber() {
		// test bil number generation replace with real one

		return "Test" + Calendar.getInstance().getTime().hashCode();
	}

	private void saveCard(Bill shopBill) {
		SharedPreferences.Editor editor = getSharedPreferences("test", MODE_PRIVATE).edit();

		editor.putString("id", etPayeeId.getText().toString());
		editor.putString("card", shopBill.getCardMask());
		editor.putString("token", shopBill.getToken());
		editor.apply();
	}
}