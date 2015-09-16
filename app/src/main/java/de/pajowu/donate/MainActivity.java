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

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.*;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.JsonFactory;
import java.io.InputStream;
import java.io.IOException;
import java.lang.Exception;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.FragmentActivity;
import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.text.SpannableString;
import android.text.Spannable;

/**
 * Activity that allows the user to select the account they want to use to sign in. The class also
 * implements integration with Google Play Services and Google Accounts.
 */
public class MainActivity extends FragmentActivity {
  static final boolean SIGN_IN_REQUIRED = false;
  private static final String AUDIENCE = "server:client_id:760560844994-04u6qkvpf481an26cnhkaauaf2dvjfk0.apps.googleusercontent.com";
  private static final String ACCOUNT_NAME_SETTING_NAME = "accountName";

  // constants for startActivityForResult flow
  static final int REQUEST_ACCOUNT_PICKER = 1;
  static final int REQUEST_GOOGLE_PLAY_SERVICES = 2;
  static final int REQUEST_AUTHORIZATION = 3;

  static GoogleAccountCredential credential;
  final static String TAG = "Donate";
  public HashMap<String, Category> categories = new HashMap<String, Category>();
  public String gplus_url;
  public TinyDB mTinyDB;
  Drawer mDrawer;
  final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
  final ArrayList<String> mTitles = new ArrayList<String>();

  /**
   * Initializes the activity content and then navigates to the MainActivity if the user is already
   * signed in or if the app is configured to not require the sign in. Otherwise it initiates
   * starting the UI for the account selection and a check for Google Play Services being up to
   * date.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    new DrawerBuilder().withActivity(this).build();

    /*allowArrowAnimation();

    this.disableLearningPattern();
    this.getToolbar().setTitle(getString(R.string.app_name));*/
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
  private void fillLayout() {
    SpannableString s = new SpannableString(getString(R.string.app_name));
    s.setSpan(new de.pajowu.donate.TypefaceSpan(getApplicationContext(), "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    ((Toolbar) findViewById(R.id.app_bar)).setTitle(s);
    mTinyDB = new TinyDB(this);
    loadCategories();
    loadAccount();
    if (mDrawer != null && mFragments != null) {
      for (int i = 0; i < mFragments.size(); i++) {
          mDrawer.removeItem(i);
      }
    }
    
    //create the drawer and remember the `Drawer` result object
    mFragments.clear();
    mTitles.clear();

    mTitles.add(getString(R.string.sharing_local));
    mFragments.add(new LocalFragment(this));
    mTitles.add(getString(R.string.sharing_categories));
    mFragments.add(new CategoryFragment(this));
    //Log.d("MainActivity",credential.getSelectedAccountName());
    if (credential.getSelectedAccount() != null) {
      mTitles.add(getString(R.string.profile));
      mFragments.add(new ProfileFragment());
    }
    mTitles.add(getString(R.string.phrasebook));
    mFragments.add(new PhraseFragment());
    mTitles.add(getString(R.string.faq));
    mFragments.add(new FAQFragment());
    mTitles.add(getString(R.string.authority_map));
    mFragments.add(new AuthorityMapFragment());
    mTitles.add(getString(R.string.about));
    mFragments.add(new AboutFragment());
    if (mDrawer == null) {
      mDrawer = new DrawerBuilder()
      .withActivity(this)
      .withToolbar((Toolbar) findViewById(R.id.app_bar))
      .withActionBarDrawerToggle(true)
      .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        // do something with the clicked item :D
          getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
          getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragments.get(position)).addToBackStack(null).commit();
        // closes Drawer
          return false;
        }
      }).build();
    }
    mDrawer.removeAllItems();
    for (int i = 0; i < mFragments.size(); i++) {
        mDrawer.addItem(
          new PrimaryDrawerItem().withName(mTitles.get(i))
        );
    }
    getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragments.get(0)).commit();
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
        } else if (!SIGN_IN_REQUIRED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
              alert.setTitle(R.string.are_you_sure);
              alert.setMessage(R.string.not_all_features);
              alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    startMainActivity();
                  }
              });
              alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
                  }
              });
              alert.show();
            
          }
        // Signing in is required so display the dialog again
        /*if (SIGN_IN_REQUIRED && accountName == null) {
          startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        } else if (accountName != null) {
          } else {
            showMainActivity();
          }*/
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

    Intent intent = new Intent(activity, MainActivity.class);
    activity.startActivity(intent);
  }

  /**
   * Navigates to the MainActivity
   */
  private void startMainActivity() {
    fillLayout();
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
            connectionStatusCode, MainActivity.this, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
      }
    });
  }
  public HashMap<String, Category> parseCat(String json) {
        try {
            JSONObject cat_json = new JSONObject(json);
            HashMap<String, Category> map = new HashMap<String, Category>();

            Iterator<String> keysItr = cat_json.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = cat_json.get(key);
                Category cat = new Category();
                if (value instanceof JSONObject) {
                    JsonFactory factory = new AndroidJsonFactory();
                    cat = (Category) factory.fromString(value.toString(), Category.class);
                }
                map.put(key, cat);
            }
            return map;
        } catch (Exception e) {
            Log.d(TAG, "Error", e);
            return null;
        }
    }

    public void loadCategories() {
        if (mTinyDB.getString("categories") != "") {
            categories = parseCat(mTinyDB.getString("categories"));
            Log.d(TAG, mTinyDB.getString("categories"));
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();
                CategoryCollection result;
                try {
                    result = service.cat().list().execute();
                    Log.d("MainActivity", result.toString());
                    if (result.getItems() != null) {
                      for (Category cat : result.getItems()) {
                        categories.put(cat.getId(), cat);
                      }
                    }
                    
                    mTinyDB.putString("categories", new JSONObject(categories).toString());
                } catch (UserRecoverableAuthIOException e) {
                    final UserRecoverableAuthIOException e2 = e;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startActivityForResult(e2.getIntent(), 2);
                        }
                    });
                    Log.d("MainActivity", "e", e);
                } catch (Exception e) {
                    Log.d("MainActivity", "e", e);
                }
            }
        };
        new Thread(runnable).start();

    }

    public void loadAccount() {
        gplus_url = mTinyDB.getString("gplus_url");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();
                UserProtoImAddressName result;
                try {
                    result = service.user().data().execute();
                    Log.d("MainActivity", result.toString());
                    Map<String, Object> im = jsonToMap(new JSONObject(result.getIm().toString()));
                    gplus_url = (String) ((HashMap) im.get("gplus")).get("url");
                    mTinyDB.putString("gplus_url", gplus_url);
                } catch (UserRecoverableAuthIOException e) {
                    final UserRecoverableAuthIOException e2 = e;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startActivityForResult(e2.getIntent(), 2);
                        }
                    });
                    Log.d("MainActivity", "e", e);
                } catch (Exception e) {
                    Log.d("MainActivity", "e", e);
                }

            }
        };
        new Thread(runnable).start();

    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
