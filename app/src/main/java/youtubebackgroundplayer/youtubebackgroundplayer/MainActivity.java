package youtubebackgroundplayer.youtubebackgroundplayer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static WebView webView;
    WebSettings webSettings;

    @Bind({R.id.desktopView, R.id.facebookView, R.id.googleView})
    List<TextView> settingsView;

    private static final String TAG = "MainActivity";
    private static final String urlWebYoutube = "https://www.youtube.com/";
    private static final String urlMobileYoutube = "https://m.youtube.com/";
    private static final String urlGoogle = "https://www.google.com";
    private static final String urlFacebook = "https://www.facebook.com";
    String url;
    private static final String urlSaveChoice = "urlSaveChoice";
    private static final String urlSaveChoiceId = "urlSaveChoiceId";

    private ProgressDialog progressBar;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final int USER_MOBILE = 0;
    public static final int USER_DESKTOP = 1;


    private MenuDrawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawer = MenuDrawer.attach(this, Position.LEFT);//, MenuDrawer.MENU_DRAG_CONTENT, Position.TOP);
        mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setMenuSize(210);
        //set content view
        mDrawer.setContentView(R.layout.activity_main);
        mDrawer.setMenuView(R.layout.menu_layout);

        webView = (WebView) findViewById(R.id.webView);

        ButterKnife.bind(this);

        for (TextView tv : settingsView) {
            tv.setOnClickListener(this);
        }

        init();


    }


    private void init() {


        //preferences = this.getSharedPreferences(urlMode,MODE_PRIVATE);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.supportMultipleWindows();
        webSettings.setCacheMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        webSettings.setLoadsImagesAutomatically(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        //set initial prefs
        url = preferences.getString(urlSaveChoice, urlWebYoutube);
        setStars(preferences.getInt(urlSaveChoiceId, R.id.webView));
        webView.loadUrl(url);

        setAlertDialog();

    }

    private void setAlertDialog() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        progressBar = ProgressDialog.show(MainActivity.this, "YouTubeInBackground", "Loading...");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //okej
                        return;
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "TRY AGAIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        webView.reload();
                        return;
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                    webView.stopLoading();

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            webView.destroy();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void saveChoice(String choice, int id) {
        editor.putString(urlSaveChoice, choice);
        editor.putInt(urlSaveChoiceId, id);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.d("id", String.valueOf(id));
        switch (id) {
            case R.id.desktopView:
                url = urlWebYoutube;
                saveChoice(url, id);
                setStars(id);
                webView.loadUrl(url);
                break;

            case R.id.facebookView:
                url = urlFacebook;
                saveChoice(url, id);
                setStars(id);
                webView.loadUrl(url);
                break;

            case R.id.googleView:
                url = urlGoogle;
                saveChoice(url, id);
                setStars(id);
                webView.loadUrl(url);
                break;
        }
    }

    private void setStars(int id) {
        for (TextView tv : settingsView) {
            if (tv.getId() == id) {
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.abc_btn_rating_star_on_mtrl_alpha, 0, 0, 0);
            } else {
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.abc_btn_rating_star_off_mtrl_alpha, 0, 0, 0);
                ;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}
