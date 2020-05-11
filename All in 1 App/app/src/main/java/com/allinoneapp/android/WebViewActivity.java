package com.allinoneapp.android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.net.URISyntaxException;

public class WebViewActivity extends AppCompatActivity {

    WebView wvMain;
    Button btnClearCache,btnExit,fabHome,fabForword,btnReload;
    SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout drawer;
    FloatingActionButton fabOpenDrawer;
    public NavigationView navigationView;
    int MY_PERMISSION_REQUEST_LOCATION = 1;
    private String mGeoLocationRequestOrigin;
    private GeolocationPermissions.Callback mGeoLocationCallback;
    private ProgressBar progressBar;
    final Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        if (ContextCompat.checkSelfPermission(WebViewActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(WebViewActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(WebViewActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(WebViewActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        progressBar =  findViewById(R.id.progressbar);
        wvMain = findViewById(R.id.webMain);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        btnReload = findViewById(R.id.btnReload);


        fabHome = findViewById(R.id.btnAllApp);

        fabOpenDrawer = findViewById(R.id.fabOpenDrawer);
        fabForword = findViewById(R.id.btnGoForwar);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wvMain.clearCache(true);
                fabOpenDrawer.show();
                wvMain.reload();
            }
        });


        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wvMain.clearCache(true);
                wvMain.reload();
                setDrawer();
            }
        });

        fabForword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wvMain.canGoForward()){
                    wvMain.goForward();
                }
                setDrawer();
            }
        });

        fabOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawer();
            }
        });

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                wvMain.clearCache(true);
                wvMain.loadUrl("https://all-in-one-app-1-d914a.web.app/");
                setDrawer();
            }
        });

        btnExit = navigationView.findViewById(R.id.bnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawer();
                Toast.makeText(getApplicationContext(),"Bye.. See you soon .. Hope you liked All in 1 app.. :)",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnClearCache = navigationView.findViewById(R.id.bnClearCache);
        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Write your code Here for click
                wvMain.clearCache(true);
                Toast.makeText(getApplicationContext(),"Temporary File Cleared",Toast.LENGTH_LONG).show();
                setDrawer();
            }
        });


        wvMain.getSettings().setLoadsImagesAutomatically(true);
        wvMain.getSettings().setLoadWithOverviewMode(true);
        wvMain.getSettings().setAppCacheEnabled(true);
        wvMain.getSettings().setDomStorageEnabled(true);
        wvMain.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.getSettings().setSaveFormData(true);
        wvMain.getSettings().setSupportZoom(true);
        wvMain.getSettings().setUseWideViewPort(true);
        wvMain.getSettings().setSupportMultipleWindows(true);
        wvMain.getSettings().setNeedInitialFocus(true);
        wvMain.getSettings().setLoadWithOverviewMode(true);
        wvMain.getSettings().setGeolocationEnabled(true);
        wvMain.getSettings().setDisplayZoomControls(true);
        wvMain.getSettings().setDatabaseEnabled(true);
        wvMain.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wvMain.getSettings().setAppCacheEnabled(true);
        wvMain.getSettings().setEnableSmoothTransition(true);
        wvMain.getSettings().setGeolocationEnabled(true);
        wvMain.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvMain.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvMain.getSettings().setSupportMultipleWindows(true);
//        wvMain.setWebChromeClient(new MyBrowser());

        wvMain.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view,String u){
                swipeRefreshLayout.setRefreshing(false);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fabOpenDrawer.hide();
                    }
                },10000);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( URLUtil.isNetworkUrl(url) ) {
                    view.loadUrl(url);
                    fabOpenDrawer.show();
                    return false;
                }
                if (appInstalledOrNot(url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity( intent );
                } else {
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                return true;
            }
        });


        wvMain.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
//                mGeoLocationRequestOrigin = null;
//                mGeoLocationCallback = null;
//                if(ContextCompat.checkSelfPermission(WebViewActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                    if(ActivityCompat.shouldShowRequestPermissionRationale(WebViewActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
//                        new AlertDialog.Builder(WebViewActivity.this)
//                                .setMessage("Allow Appsto used your lcoation when required")
//                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        mGeoLocationRequestOrigin = origin;
//                                        mGeoLocationCallback = callback;
//                                        ActivityCompat.requestPermissions(WebViewActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
//                                    }
//                                })
//                                .show();
//                    }else{
//                        mGeoLocationRequestOrigin = origin;
//                        mGeoLocationCallback = callback;
//                        ActivityCompat.requestPermissions(WebViewActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
//                    }
//                }else{
//                    callback.invoke(origin,true,true);
//                }

                callback.invoke(origin,true,true);
            }
        });


        wvMain.loadUrl("https://all-in-one-app-1-d914a.web.app/");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabOpenDrawer.hide();
            }
        },10000);

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(WebViewActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Apps which require your location will be able to use it now. Your location will be kept confidential", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Please allow next time for best experience. Your location data is kept confidential", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void setDrawer() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    private class MyBrowser extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            getWindow().setTitle(title); //Set Activity tile to page title.
        }

    }

    @Override
    public void onBackPressed() {
        if(wvMain.canGoBack()){
            wvMain.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
