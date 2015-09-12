package de.pajowu.donate;


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

import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.Category;
import com.appspot.donate_backend.donate.model.CategoryCollection;
import com.appspot.donate_backend.donate.model.UserProtoImAddressName;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.JsonFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainActivity extends MaterialNavigationDrawer {
    private Toolbar toolbar;
    public TextView app_bar_textview;
    public Typeface mainTitleTypeface;
    public DrawerLayout navDraw;
    public ListView navListView;
    private ActionBarDrawerToggle drawerToggle;
    public CharSequence mainTitle;
    public ArrayList<ListItem> arrayList;
    private Logger log = Logger.getLogger(MainActivity.class.getName());
    final static String TAG = "Donate";
    // Fragment Management
    public android.support.v4.app.FragmentManager fragmentManager;
    public android.support.v4.app.FragmentTransaction fragmentTransaction;
    public HomeFragment homeFragment;
    public ProfileFragment profileFragment;
    Donate service;
    public HashMap<String, Category> categories = new HashMap<String, Category>();
    // NAVIGATION BAR INITIATION
    public String[] navStrings;
    public String gplus_url;
    public int[] navDrawables = new int[]{
            R.drawable.ic_home_white,
            R.drawable.ic_view_module_white,
            R.drawable.ic_favorite_white,
            R.drawable.ic_pin_drop_white,
            R.drawable.ic_folder_special_white,
            R.drawable.ic_person_white,
            R.drawable.ic_settings_white

    };
    public TinyDB mTinyDB;

    //TODO Create other Fragments and accordingly other Layouts
    //TODO Get Data from API
    //TODO Implement Server functions


    /*
        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            drawerToggle.syncState();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            drawerToggle.onConfigurationChanged(newConfig);
        }


    */
    public HashMap<String, Category> getCategories() {
        return categories;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void init(Bundle bundle) {

        View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer, null);
        //setDrawerHeaderCustom(view);
        allowArrowAnimation();

        this.disableLearningPattern();
        this.getToolbar().setTitle(getString(R.string.app_name));

        mainTitleTypeface = Typeface.createFromAsset(getAssets(), "fabiolo.otf");
        mTinyDB = new TinyDB(this);
        loadCategories();
        loadAccount();

/*
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        app_bar_textview = (TextView) findViewById(R.id.app_bar_title);
        app_bar_textview.setTypeface(mainTitleTypeface);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setElevation(0);

*/


        // create sections
        String[] offersList = new String[0];
        //MaterialSection homeFragment = newSection("Home", new HomeFragment(getApplicationContext()));
        MaterialSection localFragment = newSection("Sharing - Local", new LocalFragment(this));
        MaterialSection categoriesFragment = newSection("Sharing - Categories", new CategoryFragment(this));
        MaterialSection profileFragment = newSection("Profile", new ProfileFragment());
        MaterialSection phraseFragment = newSection("German - Arabic", new PhraseFragment());
        MaterialSection faqFragment = newSection("FAQ", new FAQFragment());
        MaterialSection authorityFragment = newSection("Authority Map", new AuthorityMapFragment());

        //addSection(homeFragment);
        addSection(localFragment);
        addSection(categoriesFragment);
        addSection(profileFragment);
        addSection(phraseFragment);
        addSection(faqFragment);
        addSection(authorityFragment);
        //setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);
        /*

        CREATE CUSTOM TOOLBAR
        In following steps, a Typeface with a costum font was assigned
        The textView was referenced, which is going to be the TITLE
        Finally, the SUPPORTACTIONBAR was set and configured, to let Android show our custom ActionBar

         */
        /*

        mainTitle = getTitle();
        */
        Log.d("mainTitle = ", "" + getTitle());

        

        /*

        IMPLEMENT NAVIGATION BAR
        navStrings is an Array containing the "Topics" in the Navigation Bar
        navDraw is a DrawerLayout referenced to R.id.drawer_layout, which is needed to create the ActionBarDrawerToggle
        navListView is the Object responsible for having a list of objects in our navBar
        drawerToggle handles Opening- and Closing-Events of the NavBar

         */
        /*
        Intent intent;
        intent = new Intent(this,NavDrawer.class);
        startActivity(intent);
        */

        /*
        navStrings = getResources().getStringArray(R.array.navItems);
        navDraw = (DrawerLayout) findViewById(R.id.drawer_layout);
        navListView = (ListView) findViewById(R.id.left_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, navDraw, toolbar, R.drawable.ic_menu_white, R.drawable.ic_menu_white) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu();
            }
        };*/

        /*

        CONFIGURE DRAWER LISTENER AND OnItemClickListener
        To handle Clicks in the NavBar, a Listener therefor is needed

         */

        // Set default data for Lists


        //TODO Get data from Database
        //TODO Implement TinyDB

        /*
        navDraw.setDrawerListener(drawerToggle);
        navListView.setAdapter(new NavDrawerAdapter(this, R.layout.drawer_layout, navStrings, navDrawables));
        navListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Click event", " on: " + position);
                //TODO Create other individual events for each click

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                // TODO implement OnClickListener

                switch (position) {
                    case 0:
                        // TODO: start homefragment
                        showHomeFragment();

                        //navDraw.closeDrawers();

                        break;
                    case 1:
                        //TODO Configure custom arrayList by data from API (Not always the same arrayList)

                        showListFragment();

                        //navDraw.closeDrawers();
                        break;
                    case 2:
                        //TODO Configure custom arrayList by data from API (Not always the same arrayList)
                        showTrendingFragment();

                        Log.d("Transaction performed", "");
                        //navDraw.closeDrawers();
                        Log.d("NavigationDrawer", " closed");
                        break;
                    case 3:
                        //TODO Configure custom arrayList by data from API (Not always the same arrayList)

                        showLocalFragment();

                        //navDraw.closeDrawers();

                        break;
                    case 4:
                        //TODO Configure custom arrayList by data from API (Not always the same arrayList)

                        listFragment1 = new ListFragment(getApplicationContext(), arrayList, arrayList, arrayList);
                        fragmentTransaction.replace(R.id.container, listFragment1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        //navDraw.closeDrawers();

                        break;

                    case 5:
                        String[] arrayList6 = {"Set Database Primary Key for each offer here"};

                        //TODO Get Primary Key from Database to reference offer
                        //TODO Border around image

                        profileFragment = new ProfileFragment(new Person("John Smith", "DE", "Berlin", "john@smith.com", "01674891239", "johnsmith.com", "", arrayList6));
                        fragmentTransaction.replace(R.id.container, profileFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        //navDraw.closeDrawers();

                        // TODO Add other needed cases
                        // TODO Create User Interface and Fragment for and Profile

                }
            }
        });*/
    }

    /*
    @Override
    public void init(Bundle bundle) {
        //View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer,null);
        //setDrawerHeaderCustom(view);
        this.addSection(newSection("Section 1", new FragmentIndex()));

    }
    */

    /*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //boolean drawerOpen = navDraw.isDrawerOpen(navListView);
        //menu.findItem(R.id.action_categories).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
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
        /*final ProgressDialog progdialog = ProgressDialog.show(MainActivity.this, "", "Lade ...", true);
        progdialog.setProgressStyle(R.style.CustomHeaderLight);*/
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
                    for (Category cat : result.getItems()) {
                        categories.put(cat.getId(), cat);
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
                /*progdialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        showHomeFragment();
                    }
                });*/

            }
        };
        new Thread(runnable).start();

    }

    public void loadAccount() {
        /*final ProgressDialog progdialog = ProgressDialog.show(MainActivity.this, "", "Lade ...", true);
        progdialog.setProgressStyle(R.style.CustomHeaderLight);*/
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
                    //public Bitmap getImage(String path)
                    //boolean putImage(String theFolder, String theImageName, Bitmap theBitmap) {
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
                /*progdialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        showHomeFragment();
                    }
                });*/

            }
        };
        new Thread(runnable).start();

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
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

