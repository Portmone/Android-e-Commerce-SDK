package com.portmone.sampleapp_v3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.portmone.ecomsdk.PortmoneSDK;
import com.portmone.ecomsdk.data.Bill;
import com.portmone.ecomsdk.data.style.AppStyle;
import com.portmone.ecomsdk.data.style.ButtonStyle;
import com.portmone.ecomsdk.data.style.EditTextStyle;
import com.portmone.ecomsdk.data.style.EditTextStyleAdditional;
import com.portmone.ecomsdk.data.style.TextStyle;

public class MainActivity extends AppCompatActivity implements PortmoneSDK.PaymentCallback {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        AppStyle appStyle = new AppStyle();
        EditTextStyle editTextStyle = new EditTextStyle();
        editTextStyle.setTextFont(com.portmone.ecomsdk.R.font.roboto_700);
        appStyle.setEditTextStyle(editTextStyle);

        appStyle.setIconPayment(R.drawable.ic_portmone);


        TextStyle descriptionTextStyle = new TextStyle();
        descriptionTextStyle.setTextColor(Color.parseColor("#DA70D6"));
        descriptionTextStyle.setFontSize(14);
        appStyle.setDescriptionTextStyle(descriptionTextStyle);

        TextStyle titleTextStyle = new TextStyle();
        titleTextStyle.setTextColor(Color.parseColor("#DC143C"));
        appStyle.setNameCompanyTextStyle(titleTextStyle);

        TextStyle sumTextStyle = new TextStyle();
        sumTextStyle.setTextColor(Color.parseColor("#F0E68C"));
        sumTextStyle.setFontSize(44);
        appStyle.setSumTextStyle(sumTextStyle);

        //appStyle.setBackground(Color.parseColor("#8FBC8F"));

        appStyle.setToolbarColor(Color.parseColor("#3CB371"));

        EditTextStyleAdditional editTextStyleAdditional = new EditTextStyleAdditional();
        editTextStyleAdditional.setTextColor(Color.parseColor("#6A5ACD"));
        editTextStyleAdditional.setErrorTextColor(Color.parseColor("#90EE90"));
        editTextStyleAdditional.setHintTextColor(Color.parseColor("#C0C0C0"));

        editTextStyleAdditional.setFocusedBorderColor(Color.parseColor("#7FFFD4"));
        editTextStyleAdditional.setUnfocusedBorderColor(Color.parseColor("#4682B4"));
        editTextStyleAdditional.setSizeFocusedBorderThickness(4);
        editTextStyleAdditional.setSizeUnfocusedBorderThickness(2);
        editTextStyleAdditional.setSizeRoundedCornerShape(16);
        appStyle.setEditTextStyleAdditional(editTextStyleAdditional);

        ButtonStyle buttonStyle = new ButtonStyle();
        //settings
        buttonStyle.setTextColor(Color.parseColor("#98FB98"));
        buttonStyle.setTextColorPressed(Color.parseColor("#FFE4C4"));
        buttonStyle.setTextColorDisabled(Color.parseColor("#E0FFFF"));
        buttonStyle.setBackgroundColor(Color.parseColor("#228B22"));
        buttonStyle.setBackgroundColorPressed(Color.parseColor("#CD853F"));
        buttonStyle.setDisabledBackgroundColor(Color.parseColor("#AFEEEE"));
        appStyle.setButtonStyle(buttonStyle);

//        PortmoneSDK.setAppStyle(appStyle);
//        PortmoneSDK.setNameCompany("PORTMONE");
//        PortmoneSDK.setAdditionalCustomize(true);
          PortmoneSDK.setStandartResultFlow(true);
//        PortmoneSDK.setUAHSymbol(true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText("Version SDK: " + PortmoneSDK.getVersionSDK());
    }

    public void onClick(View view) {
        if(view.getId() == R.id.card) startActivity(new Intent(this, CardActivity.class));
        if(view.getId() == R.id.preauth) startActivity(new Intent(this, PreauthCardActivity.class));
        if(view.getId() == R.id.token) startActivity(new Intent(this, TokenActivity.class));
        if(view.getId() == R.id.token_transfer) startActivity(new Intent(this, TokenTransferActivity.class));
        if(view.getId() == R.id.preauth_pay_bills) 	startActivity(new Intent(this, PreauthPayBillsPreActivity.class));
        if(view.getId() == R.id.pay_bills) startActivity(new Intent(this, PayBillsActivity.class));
        if(view.getId() == R.id.btn_open_theme_screen) 	startActivity(new Intent(this, ThemeConfigureActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PortmoneSDK.setPaymentCallback(null);
    }

    @Override
    public void paymentSuccess(@NonNull Bill bill, boolean paidThroughGooglePay) {
        Log.d(TAG, "paymentSuccess() called with: bill = [" + bill + "], paidThroughGooglePay: " + paidThroughGooglePay);
    }

    @Override
    public void paymentError(long code, String message) {
        Log.d(TAG, "paymentError() called with: code = [" + code + "], message = [" + message + "]");
    }

}