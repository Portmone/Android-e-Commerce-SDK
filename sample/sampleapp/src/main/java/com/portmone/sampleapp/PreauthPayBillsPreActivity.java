package com.portmone.sampleapp;

import static com.portmone.ecomsdk.util.Constant$Language.EN;
import static com.portmone.ecomsdk.util.Constant$Language.RU;
import static com.portmone.ecomsdk.util.Constant$Language.SYSTEM;
import static com.portmone.ecomsdk.util.Constant$Language.UK;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;
import com.portmone.ecomsdk.data.SaveCardBillsParams;
import com.portmone.ecomsdk.ui.savecard.bills.PreauthCardBillsContract;
import com.portmone.ecomsdk.util.Constant$Language;
import com.portmone.sampleapp.R;

public class PreauthPayBillsPreActivity extends AppCompatActivity
        implements View.OnClickListener {

    private Spinner spLanguage;
    private EditText etAttribute1;
    private EditText etContractNumber;
    private TextView tvResult;

    @Constant$Language
    private String[] languages = new String[]{
            SYSTEM, UK, EN, RU
    };

    private ActivityResultLauncher<SaveCardBillsParams> resultLauncher =
            registerForActivityResult(
            new PreauthCardBillsContract(),
            result -> {
                result.handleResult(
                        (successResult) -> {
                            tvResult.setText("Payment result:\n" + successResult.getBill().toString());
                            saveCardBills(successResult.getBill());
                        },
                        (failureResult) -> {
                            tvResult.setText("Payment error:\n" + "Code" + failureResult.getCode() +
                                    "\n" + failureResult.getMessage());
                        },
                        () -> {
                            tvResult.setText("Payment has been cancelled");
                        });
            }
    );


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preauth_bills);

        tvResult = findViewById(R.id.tv_result);
        etAttribute1 = findViewById(R.id.et_attribute1);
        etContractNumber = findViewById(R.id.et_contract_number);

        spLanguage = findViewById(R.id.sp_language);

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
        PortmoneSDK.setUid(null);
        if (v.getId() == R.id.btn_open_preauth_bills) {

            final int selectedLanguageId = spLanguage.getSelectedItemPosition();
            PortmoneSDK.setLanguage(languages[selectedLanguageId]);

            // payeeId and billNumber send as is
            final SaveCardBillsParams saveCardBillsParams = new SaveCardBillsParams(
                    "uk",
                    "VERIFICATION",
                    etAttribute1.getText().toString(),
                    etContractNumber.getText().toString(),
                    "CREATE_TOKEN_BILL");

            resultLauncher.launch(saveCardBillsParams);
        }
    }

    private void saveCardBills(Bill bill) {
        Log.d("TEST_BILLS", "save - cardMask = " + bill.getCardMask());
        Log.d("TEST_BILLS", "save - token = " + bill.getToken());
        Log.d("TEST_BILLS", "save - contractNumber = " + etContractNumber.getText().toString());
        Log.d("TEST_BILLS", "save - credentials = " + etAttribute1.getText().toString());
        SharedPreferences.Editor editor = getSharedPreferences("testBills", MODE_PRIVATE).edit();

        editor.putString("token", bill.getToken());
        editor.putString("contractNumber",etContractNumber.getText().toString());
        editor.putString("credentials", etAttribute1.getText().toString());
        editor.apply();
        finish();
    }
}
