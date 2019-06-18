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
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.card) {
			startActivity(new Intent(this, CardActivity.class));
		} else if (view.getId() == R.id.preauth){
			startActivity(new Intent(this, PreauthCardPreActivity.class));
		} else if (view.getId() == R.id.token) {
			startActivity(new Intent(this, TokenActivity.class));
		}
	}
}
