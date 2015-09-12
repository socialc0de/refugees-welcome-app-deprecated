/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.pajowu.donate;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.appspot.donate_backend.donate.DonateScopes;
import java.util.Collections;
import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.model.*;
import com.appspot.donate_backend.donate.model.CategoryCollection;
import com.appspot.donate_backend.donate.model.UserProto;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import java.io.IOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
/**
 * Activity that allows the user to select the account they want to use to sign in. The class also
 * implements integration with Google Play Services and Google Accounts.
 */
public class SignInActivity extends Activity {
  static final boolean SIGN_IN_REQUIRED = true;
  private static final String AUDIENCE = "server:client_id:760560844994-04u6qkvpf481an26cnhkaauaf2dvjfk0.apps.googleusercontent.com";
  private static final String ACCOUNT_NAME_SETTING_NAME = "accountName";

  // constants for startActivityForResult flow
  private static final int REQUEST_ACCOUNT_PICKER = 1;
  private static final int REQUEST_GOOGLE_PLAY_SERVICES = 2;
  private static final int REQUEST_AUTHORIZATION = 3;

  static GoogleAccountCredential credential;

  /**
   * Initializes the activity content and then navigates to the MainActivity if the user is already
   * signed in or if the app is configured to not require the sign in. Otherwise it initiates
   * starting the UI for the account selection and a check for Google Play Services being up to
   * date.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_signin);

    if (!SIGN_IN_REQUIRED) {
      // The app won't use authentication, so just launch the main activity.
      startMainActivity();
      return;
    }
    if (!checkGooglePlayServicesAvailable()) {
      // Google Play Services are required, so don't proceed until they are installed.
      return;
    }
    if (isSignedIn()) {
      startMainActivity();
    } else {
      startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

  }

  /**
   * Handles the results from activities launched to select an account and to install Google Play
   * Services.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_ACCOUNT_PICKER:
        if (data != null && data.getExtras() != null) {
          String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
          if (accountName != null) {
            onSignedIn(accountName);
          }
        }
        // Signing in is required so display the dialog again
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        break;
      case REQUEST_GOOGLE_PLAY_SERVICES:
        if (resultCode != Activity.RESULT_OK) {
          checkGooglePlayServicesAvailable();
        }
        break;
    }
  }

  /**
   * Retrieves the previously used account name from the application preferences and checks if the
   * credential object can be set to this account.
   */
  private boolean isSignedIn() {
    credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DonateScopes.USERINFO_EMAIL + " " + DonateScopes.PLUS_LOGIN));
    SharedPreferences settings = getSharedPreferences("refugees", 0);
    String accountName = settings.getString(ACCOUNT_NAME_SETTING_NAME, null);
    credential.setSelectedAccountName(accountName);

    return credential.getSelectedAccount() != null;
  }

  /**
   * Called when the user selected an account. The account name is stored in the application
   * preferences and set in the credential object.
   *
   * @param accountName the account that the user selected.
   */
  private void onSignedIn(String accountName) {
    SharedPreferences settings = getSharedPreferences("refugees", 0);
    Log.d("MainActivity",accountName);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(ACCOUNT_NAME_SETTING_NAME, accountName);
    editor.commit();
    credential.setSelectedAccountName(accountName);
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
          Builder endpointBuilder = new Donate.Builder(
              AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
              CloudEndpointBuilderHelper.getRequestInitializer());

          Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();

          User result;
          try {
              result = service.user().create(new UserProto()).execute();
              Log.d("MainAc login",result.toString());
              runOnUiThread(new Runnable() {
                  public void run() {
                      startMainActivity();
                  }
              });
          } catch (UserRecoverableAuthIOException e) {
              Log.d("MainActivity","e",e);
              final UserRecoverableAuthIOException e2 = e;
              runOnUiThread(new Runnable() {
                  public void run() {
                      startActivityForResult(e2.getIntent(), 2);
                  }
              });
              
          } catch (IOException e) {
              Log.d("MainActivity","e",e);
          }
      }
    };
    new Thread(runnable).start();
  }

  /**
   * Called to sign out the user, so user can later on select a different account.
   *
   * @param activity activity that initiated the sign out.
   */
  static void onSignOut(Activity activity) {
    SharedPreferences settings = activity.getSharedPreferences("reguees", 0);

    SharedPreferences.Editor editor = settings.edit();
    editor.putString(ACCOUNT_NAME_SETTING_NAME, "");

    editor.commit();
    credential.setSelectedAccountName("");

    Intent intent = new Intent(activity, SignInActivity.class);
    activity.startActivity(intent);
  }

  /**
   * Navigates to the MainActivity
   */
  private void startMainActivity() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }


  @Override
  protected void onResume() {
    super.onResume();
    if (!SIGN_IN_REQUIRED) {
      // The app won't use authentication, so just launch the main activity.
      startMainActivity();
      return;
    }

    if (!checkGooglePlayServicesAvailable()) {
      // Google Play Services are required, so don't proceed until they are installed.
      return;
    }

    if (isSignedIn()) {
      startMainActivity();
    } else {
      //startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }
  }

  /**
   * Checks if Google Play Services are installed and if not it initializes opening the dialog to
   * allow user to install Google Play Services.
   */
  private boolean checkGooglePlayServicesAvailable() {
    final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
      showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
      return false;
    }
    return true;
  }

  /**
   * Shows the Google Play Services dialog on UI thread.
   */
  void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
            connectionStatusCode, SignInActivity.this, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
      }
    });
  }
}
