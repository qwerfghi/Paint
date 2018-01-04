package com.last.paint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity {
    private CanvasView mCanvasView;
    private LinearLayout mSideMenu;
    private boolean mIsShowing = true;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        mCanvasView = findViewById(R.id.canvas_view);

        mSideMenu = findViewById(R.id.side_menu);
        mSideMenu.setBackgroundColor(Color.LTGRAY);

        Button circleButton = findViewById(R.id.circle_button);
        circleButton.setOnClickListener(view -> mCanvasView.setFigureType(FigureType.CIRCLE));

        Button rectangleButton = findViewById(R.id.rectangle_button);
        rectangleButton.setOnClickListener(view -> mCanvasView.setFigureType(FigureType.RECTANGLE));

        Button ovalButton = findViewById(R.id.oval_button);
        ovalButton.setOnClickListener(view -> mCanvasView.setFigureType(FigureType.OVAL));

        Button squareButton = findViewById(R.id.square_button);
        squareButton.setOnClickListener(view -> mCanvasView.setFigureType(FigureType.SQUARE));

        Button lineButton = findViewById(R.id.line_button);
        lineButton.setOnClickListener(view -> mCanvasView.setFigureType(FigureType.LINE));

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this::undo);

        Button lineColorButton = findViewById(R.id.line_color_button);
        lineColorButton.setBackgroundColor(mCanvasView.getFigurePaint().getColor());
        lineColorButton.setOnClickListener(view -> {
            new AmbilWarnaDialog(this, mCanvasView.getFigurePaint().getColor(), true,
                    new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    mCanvasView.getFigurePaint().setColor(color);
                    lineColorButton.setBackgroundColor(color);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }
            }).show();
        });

        SeekBar lineWeightBar = findViewById(R.id.line_thickness_bar);
        SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCanvasView.getFigurePaint().setStrokeWidth(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        lineWeightBar.setOnSeekBarChangeListener(changeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_tools:
                if (mIsShowing) {
                    mIsShowing = false;
                    TransitionManager.beginDelayedTransition(mSideMenu, new Slide(Gravity.START));
                    mSideMenu.setVisibility(View.GONE);
                } else {
                    mIsShowing = true;
                    TransitionManager.beginDelayedTransition(mSideMenu, new Slide(Gravity.START));
                    mSideMenu.setVisibility(View.VISIBLE);
                }
                return true;
            case R.id.open:
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    openImage();
                }
                return true;
            case R.id.save:
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    saveImage();
                }
                return true;
            case R.id.clear:
                mCanvasView.getFigures().clear();
                mCanvasView.invalidate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void undo(View view) {
        List<Figure> figures = mCanvasView.getFigures();
        if (figures.size() != 0) {
            figures.remove(figures.size() - 1);
        }
        mCanvasView.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        mCanvasView.getFigures().add(new Picture(selectedImage));
                        mCanvasView.invalidate();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_READ_EXTERNAL_STORAGE:
                    openImage();
                    break;
                case REQUEST_WRITE_EXTERNAL_STORAGE:
                    saveImage();
                    break;
            }
        } else {
            Toast.makeText(this, "Can't get permission", Toast.LENGTH_SHORT).show();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void saveImage() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(imageFileName, ".jpeg", storageDir);
            viewToBitmap(mCanvasView).compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(image));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(image);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            Toast.makeText(getApplicationContext(), "image saved to: " + storageDir, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

    private Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}