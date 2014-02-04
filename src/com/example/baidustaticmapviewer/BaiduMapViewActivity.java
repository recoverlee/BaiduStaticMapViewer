package com.example.baidustaticmapviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class BaiduMapViewActivity extends Activity {

    private final static String TAG = BaiduMapViewActivity.class.getSimpleName();
    private ImageView mImage = null;

    private EditText mEditZoom = null;
    private EditText mEditWidth = null;
    private EditText mEditHeight = null;
    private EditText mEditLng = null;
    private EditText mEditLat = null;
    private Spinner mSpinMarkerSize = null;
    private Spinner mSpinMarkerColor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImage = (ImageView) this.findViewById(R.id.map_view);
        mEditZoom = (EditText) this.findViewById(R.id.zoom);
        mEditWidth = (EditText) this.findViewById(R.id.width);
        mEditHeight = (EditText) this.findViewById(R.id.height);
        mEditLng = (EditText) this.findViewById(R.id.lng);
        mEditLat = (EditText) this.findViewById(R.id.lat);
        mSpinMarkerSize = (Spinner) findViewById(R.id.spin_marker_size);
        mSpinMarkerColor = (Spinner) findViewById(R.id.spin_marker_color);

        ArrayAdapter<CharSequence> adSpinMarkerSize, adSpinMarkerColor;
        adSpinMarkerSize = ArrayAdapter.createFromResource(this, R.array.marker_size, android.R.layout.simple_dropdown_item_1line);
        adSpinMarkerColor = ArrayAdapter.createFromResource(this, R.array.marker_color, android.R.layout.simple_dropdown_item_1line);
        mSpinMarkerSize.setAdapter(adSpinMarkerSize);
        mSpinMarkerSize.setSelection(0);
        mSpinMarkerColor.setAdapter(adSpinMarkerColor);
        mSpinMarkerColor.setSelection(0);
        init();
        getMapFromUrl();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void init() {
        String zoom = "14";
        String width = "400";
        String height = "300";
        String lng = "116.403874";
        String lat = "39.914889";

        mEditZoom.setText(zoom);
        mEditWidth.setText(width);
        mEditHeight.setText(height);
        mEditLng.setText(lng);
        mEditLat.setText(lat);
    }

    public void getMapFromUrl() {

        String zoom = mEditZoom.getText().toString();
        String width = mEditWidth.getText().toString();
        String height = mEditHeight.getText().toString();
        String lng = mEditLng.getText().toString();
        String lat = mEditLat.getText().toString();
        String markerSize = mSpinMarkerSize.getSelectedItem().toString();
        String markerColor = mSpinMarkerColor.getSelectedItem().toString();

        String markerUrl = "http://api.map.baidu.com/staticimage?";
        markerUrl += "width=" + width + "&";
        markerUrl += "height=" + height + "&";
        markerUrl += "center=" + lng + "," + lat + "&";
        markerUrl += "zoom=" + zoom + "&";
        markerUrl += "markers=" + lng + "," + lat + "&";
        markerUrl += "markerStyles=" + markerSize + ",," + markerColor;

        Log.d(TAG, "URL : " + markerUrl);

        ImageLoader mImageLoader;
        mImageLoader = VolleySingleton.getInstance(getApplicationContext()).getImageLoader();
        mImageLoader.get(markerUrl, new ImageListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "onErrorResponse" + volleyError.getMessage());
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                Log.d(TAG, "onResponse " + isImmediate);
                if (response.getBitmap() != null) {

                    mImage.setImageBitmap(response.getBitmap());
                    // BaseMapDemo.saveBitmap(BaseMapDemo.this, response.getBitmap(), "volley_test.png");
                }
            }
        });
    }

    public void onRefresh(View v) {
        getMapFromUrl();

    }
}
