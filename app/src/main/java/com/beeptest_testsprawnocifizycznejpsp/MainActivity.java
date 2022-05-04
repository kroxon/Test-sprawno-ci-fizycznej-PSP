package com.beeptest_testsprawnocifizycznejpsp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wefika.horizontalpicker.HorizontalPicker;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private ConstraintLayout constraintLayoutBS, constraitLRod, constraitLBall;
    private Button bStop, bPlay, bStagePlus, bStageMinus, bSectionPlus, bSectionMinus;
    private SeekBar seekBar;
    private TextView textStart, textStage, textSection, textSpeed, textDistance, textPoints, textStageCount,
            textSectionCount, etRod, textMen, textWomen, tvage29, tvage34, tvage39, tvage44, tvage49, tvage50, tvRank, tvRankDesc;
    Thread updateSeekbar;
    private EditText etEnvelop, etBall;
    private LinearLayout lFocus, linearRank;
    private ScrollView myScrollView;
    private RadioGroup rgSex, rgAge;
    private RadioButton rbSex, rbAge, rbmen, rbwomen, rba29, rba34, rba39, rba44, rba49, rba50;
    private HorizontalPicker picker;
    private ImageView imgArrowR, imgArrowL;
//    BarVisualizer barVisualizer;

    static MediaPlayer mediaPlayer;
    int position;
    private int CurrentProgress = 0;
    int countReplay = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (barVisualizer != null)
//            barVisualizer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setTheme(R.style.ColoredHandleTheme);

        hideKeyboard(MainActivity.this);
        init();


        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.beep_test);

        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeekbar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currenPosition = 0;

                while (currenPosition < totalDuration) {
                    try {
                        sleep(500);
                        currenPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currenPosition);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    bPlay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                } else {
                    bPlay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                    if (!updateSeekbar.isAlive())
                        updateSeekbar.start();

                }
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bPlay.setBackgroundResource(R.drawable.ic_play);
                mediaPlayer.seekTo(0);
                mediaPlayer.pause();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                bStop.performClick();
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                }
            }

            @Override
            public void onSlide(View view, float v) {

            }
        });

        final Handler handler = new Handler();
        final int delay = 100;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                //seekbar
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                textStart.setText(currentTime);

                //stage-section
                int curTime = mediaPlayer.getCurrentPosition();
                float curTimeF = mediaPlayer.getCurrentPosition();

                if (curTime < 9000) {
                    textStage.setText("1");
                    textSection.setText("1");
                    textSpeed.setText("" + 0);
                    textDistance.setText("0");
                }
                //stage 1
                if (curTime >= 9000 && curTime < 72000) {
                    textStage.setText("1");
                    textSection.setText("" + ((curTime - 9000) / 9000 + 1));
                    textSpeed.setText("" + ((20 / 9) * 3.6));
                    float dist = (curTimeF / 1000 - 9) / (72 - 9) * 20 * 7;
                    String distString = String.format("%.1f", dist);
                    textDistance.setText(distString);
                }
                //stage 2
                if (curTime >= 72000 && curTime < 136000) {
                    textStage.setText("2");
                    textSection.setText("" + ((curTime - 72000) / 8000 + 1));
                    textSpeed.setText("" + (20 * 3.6 / 8));
                    float dist = (curTimeF / 1000 - 72) / (136 - 72) * 20 * 8;
                    String distString = String.format("%.1f", dist + 140);
                    textDistance.setText(distString);
                }
                //stage 3
                if (curTime >= 136000 && curTime < 196000) {
                    textStage.setText("3");
                    textSection.setText("" + ((curTime - 136000) / 7500 + 1));
                    textSpeed.setText("" + ((20 / 7.5) * 3.6));
                    float dist = (curTimeF / 1000 - 136) / (196 - 136) * 20 * 8;
                    String distString = String.format("%.1f", dist + 20 * 15);
                    textDistance.setText(distString);
                }
                //stage 4
                if (curTime >= 196000 && curTime < 260800) {
                    textStage.setText("4");
                    textSection.setText("" + ((curTime - 196000) / 7200 + 1));
                    textSpeed.setText("" + ((20 / 7.2) * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 196) / (260.8 - 196) * 20 * 9);
                    String distString = String.format("%.1f", dist + 20 * 23);
                    textDistance.setText(distString);
                }
                //stage 5
                if (curTime >= 260800 && curTime < 322000) {
                    textStage.setText("5");
                    textSection.setText("" + ((curTime - 260800) / 6800 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 6.8 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 260.8) / (322 - 260.8) * 20 * 9);
                    String distString = String.format("%.1f", dist + 20 * 32);
                    textDistance.setText(distString);
                }
                //stage 6 add 0.6 s
                if (curTime >= 322600 && curTime < 387000) {
                    textStage.setText("6");
                    textSection.setText("" + ((curTime - 322600) / 6500 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 6.5 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 322.6) / (387 - 322.6) * 20 * 10);
                    String distString = String.format("%.1f", dist + 20 * 41);
                    textDistance.setText(distString);
                }
                //stage 7 add 0.7 sec and 0.8 s
                if (curTime >= 387700 && curTime < 449000) {
                    textStage.setText("7");
                    textSection.setText("" + ((curTime - 387800) / 6200 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 6.2 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 387) / (449 - 387) * 20 * 10);
                    String distString = String.format("%.1f", dist + 20 * 51);
                    textDistance.setText(distString);
                }
                //stage 8 add 1.3 sec and 1.5 s
                if (curTime >= 450300 && curTime < 515000) {
                    textStage.setText("8");
                    textSection.setText("" + ((curTime - 450500) / 6000 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 6 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 449) / (515 - 449) * 20 * 11);
                    String distString = String.format("%.1f", dist + 20 * 61);
                    textDistance.setText(distString);
                }
                //stage 9 add 2 s
                if (curTime >= 517000 && curTime < 577700) {
                    textStage.setText("9");
                    textSection.setText("" + ((curTime - 517000) / 5700 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 5.7 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 515) / (577.7 - 515) * 20 * 11);
                    String distString = String.format("%.1f", dist + 20 * 72);
                    textDistance.setText(distString);
                }
                //stage 10 add 2 s and 2.5 s
                if (curTime >= 579700 && curTime < 638200) {
                    textStage.setText("10");
                    textSection.setText("" + ((curTime - 580200) / 5500 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 5.5 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 579.7) / (638.2 - 579.7) * 20 * 11);
                    String distString = String.format("%.1f", dist + 20 * 83);
                    textDistance.setText(distString);
                }
                //stage 11 add 2.8 s and 3 s
                if (curTime >= 641000 && curTime < 701800) {
                    textStage.setText("11");
                    textSection.setText("" + ((curTime - 641200) / 5300 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 5.3 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 641) / (701.8 - 641) * 20 * 12);
                    String distString = String.format("%.1f", dist + 20 * 94);
                    textDistance.setText(distString);
                }
                //stage 12 add 2.8 s and 3 s
                if (curTime >= 704600 && curTime < mediaPlayer.getDuration()) {
                    textStage.setText("12");
                    textSection.setText("" + ((curTime - 704800) / 5100 + 1));
                    textSpeed.setText(String.format("%.1f", 20 / 5.1 * 3.6));
                    float dist = (float) ((curTimeF / 1000 - 704.6) / (25.5) * 20 * 5);
                    String distString = String.format("%.1f", dist + 20 * 106);
                    textDistance.setText(distString);
                }

                handler.postDelayed(this, delay);
            }
        }, delay);


        etEnvelop.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (etEnvelop.getText().toString().equals("")) {
                        etEnvelop.clearFocus();
                        return true;
                    }
                    if (Float.parseFloat(etEnvelop.getText().toString()) < 0)
                        etEnvelop.setText("0");
                    Toast.makeText(MainActivity.this, etEnvelop.getText(), Toast.LENGTH_SHORT).show();
                    hideKeyboard(MainActivity.this);
                    allPoints();
                    etEnvelop.clearFocus();
                    return true;
                }
                return false;
            }
        });


        etEnvelop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        myScrollView.scrollTo(0, myScrollView.getBottom() + 100);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED

                        break;
                }
                return false;
            }
        });

        etBall.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (etBall.getText().toString().equals("")) {
                        etBall.clearFocus();
                        return true;
                    }
                    if (Float.parseFloat(etBall.getText().toString()) < 0)
                        etBall.setText("0");
                    Toast.makeText(MainActivity.this, etBall.getText(), Toast.LENGTH_SHORT).show();
                    hideKeyboard(MainActivity.this);
                    allPoints();
                    etBall.clearFocus();
                    return true;
                }
                return false;
            }
        });

        etBall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        myScrollView.scrollTo(0, myScrollView.getBottom() + 100);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED

                        break;
                }
                return false;
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (checkSexButton()) {
                    constraitLRod.setVisibility(View.VISIBLE);
                    constraitLBall.setVisibility(View.GONE);
                }
                if (checkSexButton() == false) {
                    constraitLRod.setVisibility(View.GONE);
                    constraitLBall.setVisibility(View.VISIBLE);
                }
            }
        });

        bStagePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberStage = Integer.parseInt(textStageCount.getText().toString());
                int numberSection = Integer.parseInt(textSectionCount.getText().toString());
                if (numberStage < 12) {
                    textStageCount.setText("" + (numberStage + 1));
                    if (numberStage == 11 && (numberSection > 5))
                        textSectionCount.setText("5");
                }
                allPoints();
            }
        });

        bStageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberStage = Integer.parseInt(textStageCount.getText().toString());
                int numberSection = Integer.parseInt(textSectionCount.getText().toString());
                if (numberStage > 1) {
                    numberStage = numberStage - 1;
                    textStageCount.setText("" + (numberStage));
                    if (numberStage == 1 && numberSection > 7)
                        textSectionCount.setText("7");
                    if (numberStage < 4 && numberSection > 8)
                        textSectionCount.setText("8");
                    if (numberStage < 6 && numberSection > 9)
                        textSectionCount.setText("9");
                    if (numberStage < 8 && numberSection > 10)
                        textSectionCount.setText("10");
                    if (numberStage < 11 && numberSection > 11)
                        textSectionCount.setText("11");
                }
                allPoints();
            }
        });

        bSectionPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberStage = Integer.parseInt(textStageCount.getText().toString());
                int numberSection = Integer.parseInt(textSectionCount.getText().toString());
                if (numberStage == 12 && numberSection < 5)
                    textSectionCount.setText("" + (numberSection + 1));
                if (numberStage == 11 && numberSection < 12)
                    textSectionCount.setText("" + (numberSection + 1));
                if ((numberStage == 10 || numberStage == 9 || numberStage == 8) && numberSection < 11)
                    textSectionCount.setText("" + (numberSection + 1));
                if ((numberStage == 7 || numberStage == 6) && numberSection < 10)
                    textSectionCount.setText("" + (numberSection + 1));
                if ((numberStage == 5 || numberStage == 4) && numberSection < 9)
                    textSectionCount.setText("" + (numberSection + 1));
                if ((numberStage == 3 || numberStage == 2) && numberSection < 8)
                    textSectionCount.setText("" + (numberSection + 1));
                if (numberStage == 1 && numberSection < 7)
                    textSectionCount.setText("" + (numberSection + 1));
                allPoints();
            }
        });

        bSectionMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberSection = Integer.parseInt(textSectionCount.getText().toString());
                if (numberSection > 1)
                    textSectionCount.setText("" + (numberSection - 1));
                allPoints();
            }
        });


        picker.setSelectedItem(13);

        picker.setOnItemClickedListener(new HorizontalPicker.OnItemClicked() {
            @Override
            public void onItemClicked(int index) {
                int sel = picker.getSelectedItem();
                Toast.makeText(MainActivity.this, "Item selected", Toast.LENGTH_SHORT).show();
                etRod.setText("" + sel);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        picker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            @Override
            public void onItemSelected(int index) {
                int sel = picker.getSelectedItem();
                Toast.makeText(MainActivity.this, "Item clicked", Toast.LENGTH_SHORT).show();
                etRod.setText("" + sel);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgArrowR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picker.getSelectedItem() != 26)
                    picker.setSelectedItem(picker.getSelectedItem() + 1);
                etRod.setText("" + picker.getSelectedItem());
                allPoints();
            }
        });

        imgArrowL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picker.getSelectedItem() != 0)
                    picker.setSelectedItem(picker.getSelectedItem() - 1);
                etRod.setText("" + picker.getSelectedItem());
                allPoints();
            }
        });


        rba29.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rba29.isChecked()) {
                    tvage29.setElevation(15);
                    tvage29.setTranslationZ(10);
                    tvage29.setTranslationY(-5);
                    tvage29.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                } else {
                    tvage29.setElevation(0);
                    tvage29.setTranslationZ(0);
                    tvage29.setTranslationY(0);
                    tvage29.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                }
            }
        });

        rba34.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rba34.isChecked()) {
                    tvage34.setElevation(15);
                    tvage34.setTranslationZ(10);
                    tvage34.setTranslationY(-5);
                    tvage34.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                } else {
                    tvage34.setElevation(0);
                    tvage34.setTranslationZ(0);
                    tvage34.setTranslationY(0);
                    tvage34.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                }
            }
        });

        rba39.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rba39.isChecked()) {
                    tvage39.setElevation(15);
                    tvage39.setTranslationZ(10);
                    tvage39.setTranslationY(-5);
                    tvage39.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                } else {
                    tvage39.setElevation(0);
                    tvage39.setTranslationZ(0);
                    tvage39.setTranslationY(0);
                    tvage39.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                }
            }
        });

        rba44.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rba44.isChecked()) {
                    tvage44.setElevation(15);
                    tvage44.setTranslationZ(10);
                    tvage44.setTranslationY(-5);
                    tvage44.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                } else {
                    tvage44.setElevation(0);
                    tvage44.setTranslationZ(0);
                    tvage44.setTranslationY(0);
                    tvage44.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                }
            }
        });

        rba49.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rba49.isChecked()) {
                    tvage49.setElevation(15);
                    tvage49.setTranslationZ(10);
                    tvage49.setTranslationY(-5);
                    tvage49.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                } else {
                    tvage49.setElevation(0);
                    tvage49.setTranslationZ(0);
                    tvage49.setTranslationY(0);
                    tvage49.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                }
            }
        });

        rba50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rba50.isChecked()) {
                    tvage50.setElevation(15);
                    tvage50.setTranslationZ(10);
                    tvage50.setTranslationY(-5);
                    tvage50.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                } else {
                    tvage50.setElevation(0);
                    tvage50.setTranslationZ(0);
                    tvage50.setTranslationY(0);
                    tvage50.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                }
            }
        });

        textWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textWomen.getTranslationZ() == 20) {
                    textWomen.setElevation(0);
                    textWomen.setTranslationZ(0);
                    textWomen.setTranslationY(0);
                    textWomen.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                    textMen.setElevation(15);
                    textMen.setTranslationZ(10);
                    textMen.setTranslationY(-5);
                    textMen.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                    rbmen.setChecked(true);
                } else {
                    textWomen.setElevation(15);
                    textWomen.setTranslationZ(20);
                    textWomen.setTranslationY(-5);
                    textWomen.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                    textMen.setElevation(0);
                    textMen.setTranslationZ(0);
                    textMen.setTranslationY(0);
                    textMen.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                    rbwomen.setChecked(true);
                }
                allPoints();
            }
        });

        if (textMen.getTranslationZ() == 0) {
            textMen.setTranslationZ(20);
            textMen.setElevation(15);
        }
        textMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textMen.getTranslationZ() == 20) {
                    textMen.setElevation(0);
                    textMen.setTranslationZ(0);
                    textMen.setTranslationY(0);
                    textMen.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                    textWomen.setElevation(15);
                    textWomen.setTranslationZ(10);
                    textWomen.setTranslationY(-5);
                    textWomen.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                    rbwomen.setChecked(true);
                } else {
                    textMen.setElevation(15);
                    textMen.setTranslationZ(20);
                    textMen.setTranslationY(-5);
                    textMen.setBackground(getResources().getDrawable(R.drawable.button_bg_click));
                    textWomen.setElevation(0);
                    textWomen.setTranslationZ(0);
                    textWomen.setTranslationY(0);
                    textWomen.setBackground(getResources().getDrawable(R.drawable.button_bg_unclick));
                    rbmen.setChecked(true);
                }
                allPoints();
            }
        });

        bStagePlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        bStagePlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_orange)));
                        bStagePlus.setTranslationZ(-20);
                        bStagePlus.setTranslationY(-5);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        bStagePlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_grey_light)));
                        bStagePlus.setTranslationZ(0);
                        bStagePlus.setTranslationY(0);
                        break;
                }
                return false;
            }
        });

        bStageMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        bStageMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_orange)));
                        bStageMinus.setTranslationZ(-20);
                        bStageMinus.setTranslationY(5);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        bStageMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_grey_light)));
                        bStageMinus.setTranslationZ(0);
                        bStageMinus.setTranslationY(0);
                        break;
                }
                return false;
            }
        });

        bSectionMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        bSectionMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_orange)));
                        bSectionMinus.setTranslationZ(-20);
                        bSectionMinus.setTranslationY(5);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        bSectionMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_grey_light)));
                        bSectionMinus.setTranslationZ(0);
                        bSectionMinus.setTranslationY(0);
                        break;
                }
                return false;
            }
        });

        bSectionPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        bSectionPlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_orange)));
                        bSectionPlus.setTranslationZ(-20);
                        bSectionPlus.setTranslationY(-5);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        bSectionPlus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.xiaomi_grey_light)));
                        bSectionPlus.setTranslationZ(0);
                        bSectionPlus.setTranslationY(0);
                        break;
                }
                return false;
            }
        });


        tvage29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rba29.setChecked(true);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvage34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rba34.setChecked(true);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvage39.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rba39.setChecked(true);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvage44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rba44.setChecked(true);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvage49.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rba49.setChecked(true);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvage50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rba50.setChecked(true);
                try {
                    //set time in mili
                    Thread.sleep(10);
                    allPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        etBall.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!etBall.hasFocus()) {
                    hideKeyboard(MainActivity.this);
                }
            }
        });

        findViewById(R.id.bInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }


    private void init() {
        this.constraintLayoutBS = findViewById(R.id.bottomSheet);
        this.constraitLRod = findViewById(R.id.layoutRod);
        this.constraitLBall = findViewById(R.id.layoutBall);
        this.linearRank = findViewById(R.id.linearRank);
        this.myScrollView = findViewById(R.id.scrollView);
//        this.bottomSheetBehavior = BottomSheetBehavior.from(constraintLayoutBS);
        this.bottomSheetBehavior = BottomSheetBehavior.from(constraintLayoutBS);
        this.seekBar = findViewById(R.id.seekBar);
//        this.barVisualizer = findViewById(R.id.blast);
        this.bPlay = findViewById(R.id.bPlay);
        this.bStop = findViewById(R.id.bStop);
        this.bStagePlus = findViewById(R.id.bStagePlus);
        this.bStageMinus = findViewById(R.id.bStageMinus);
        this.bSectionPlus = findViewById(R.id.bSectionPlus);
        bSectionMinus = findViewById(R.id.bSectionMinus);
        this.textStart = findViewById(R.id.textsStart);
        this.textStage = findViewById(R.id.stage);
        this.textSection = findViewById(R.id.section);
        this.textSpeed = findViewById(R.id.tSpeed);
        this.textDistance = findViewById(R.id.textDistance);
        this.textPoints = findViewById(R.id.pointsTextView);
        this.textSectionCount = findViewById(R.id.sectionCount);
        this.textStageCount = findViewById(R.id.stageCount);
        this.tvage29 = findViewById(R.id.tvage29);
        this.tvage34 = findViewById(R.id.tvage34);
        this.tvage39 = findViewById(R.id.tvage39);
        this.tvage44 = findViewById(R.id.tvage44);
        this.tvage49 = findViewById(R.id.tvage49);
        this.tvage50 = findViewById(R.id.tvage50);
        this.etRod = findViewById(R.id.editTextRod);
        this.textMen = findViewById(R.id.tvMen);
        this.textWomen = findViewById(R.id.tvWomen);
        this.tvRank = findViewById(R.id.tvRank);
        this.tvRankDesc = findViewById(R.id.tvRankDesc);
        this.etEnvelop = findViewById(R.id.editTextEnvelope);
        this.etBall = findViewById(R.id.editTextBall);
        this.lFocus = findViewById(R.id.layoutForFocus);
        this.rgSex = findViewById(R.id.radiogroupSex);
        this.rgAge = findViewById(R.id.rgAge);
        this.rbmen = findViewById(R.id.rbmen);
        this.rbwomen = findViewById(R.id.rbwomen);
        this.rba29 = findViewById(R.id.rba29);
        this.rba34 = findViewById(R.id.rba34);
        this.rba39 = findViewById(R.id.rba39);
        this.rba44 = findViewById(R.id.rba44);
        this.rba49 = findViewById(R.id.rba49);
        this.rba50 = findViewById(R.id.rba50);
        this.picker = findViewById(R.id.picker);
        this.imgArrowR = findViewById(R.id.imgArrowRight);
        this.imgArrowL = findViewById(R.id.imgArrowLeft);

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.dialog_layout, (ConstraintLayout) findViewById(R.id.dialog_container));

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.bInfoClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();


    }

    public String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        time += min + ":";

        if (sec < 10)
            time += "0";
        time += sec;

        return time;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean checkSexButton() {
        boolean flagSex = true;
        int radioId = rgSex.getCheckedRadioButtonId();
        rbSex = findViewById(radioId);
        if (rbSex.getId() == rbmen.getId())
            flagSex = true;
        if (rbSex.getId() == rbwomen.getId())
            flagSex = false;
        return flagSex;
    }

    public int checkAgeButton() {
        int age = 1;
        int radioId = rgAge.getCheckedRadioButtonId();
        rbAge = findViewById(radioId);
        if (rbAge.getId() == rba29.getId()) {
            age = 0;
        }
        if (rbAge.getId() == rba34.getId()) {
            age = 10;
        }
        if (rbAge.getId() == rba39.getId()) {
            age = 20;
        }
        if (rbAge.getId() == rba44.getId()) {
            age = 25;
        }
        if (rbAge.getId() == rba49.getId()) {
            age = 30;
        }
        if (rbAge.getId() == rba50.getId()) {
            age = 35;
        }
        return age;
    }

    public int pRod(int count) {
        int points = 0;
        if (count <= 0) {
            points = 0;
        }
        if (count == 1) {
            points = 1;
        }
        if (count == 2) {
            points = 5;
        }
        if (count == 3) {
            points = 10;
        }
        if (count == 4) {
            points = 15;
        }
        if (count == 5) {
            points = 20;
        }
        if (count == 6) {
            points = 25;
        }
        if (count == 7) {
            points = 30;
        }
        if (count == 8) {
            points = 35;
        }
        if (count == 9) {
            points = 40;
        }
        if (count == 10) {
            points = 45;
        }
        if (count == 11) {
            points = 50;
        }
        if (count == 12) {
            points = 55;
        }
        if (count == 13) {
            points = 58;
        }
        if (count == 14) {
            points = 61;
        }
        if (count == 15) {
            points = 63;
        }
        if (count == 16) {
            points = 65;
        }
        if (count == 17) {
            points = 66;
        }
        if (count == 18) {
            points = 67;
        }
        if (count == 19) {
            points = 68;
        }
        if (count == 20) {
            points = 69;
        }
        if (count == 21) {
            points = 70;
        }
        if (count == 22) {
            points = 71;
        }
        if (count == 23) {
            points = 72;
        }
        if (count == 24) {
            points = 73;
        }
        if (count == 25) {
            points = 74;
        }
        if (count >= 26) {
            points = 75;
        }

        return points;
    }

    public int pBall(float distance) {
        int points = 0;
        if (distance < 1) {
            points = 0;
        }
        if (distance >= 5.00 && distance < 5.1) {
            points = 1;
        }
        if (distance >= 5.10 && distance < 5.2) {
            points = 2;
        }
        if (distance >= 5.20 && distance < 5.3) {
            points = 3;
        }
        if (distance >= 5.30 && distance < 5.4) {
            points = 4;
        }
        if (distance >= 5.40 && distance < 5.5) {
            points = 5;
        }
        if (distance >= 5.50 && distance < 5.6) {
            points = 6;
        }
        if (distance >= 5.60 && distance < 5.7) {
            points = 7;
        }
        if (distance >= 5.70 && distance < 5.8) {
            points = 9;
        }
        if (distance >= 5.80 && distance < 5.9) {
            points = 11;
        }
        if (distance >= 5.90 && distance < 6.0) {
            points = 13;
        }
        if (distance >= 6.00 && distance < 6.1) {
            points = 15;
        }
        if (distance >= 6.10 && distance < 6.2) {
            points = 17;
        }
        if (distance >= 6.20 && distance < 6.3) {
            points = 19;
        }
        if (distance >= 6.30 && distance < 6.4) {
            points = 21;
        }
        if (distance >= 6.40 && distance < 6.5) {
            points = 23;
        }
        if (distance >= 6.50 && distance < 6.6) {
            points = 25;
        }
        if (distance >= 6.60 && distance < 6.7) {
            points = 27;
        }
        if (distance >= 6.70 && distance < 6.8) {
            points = 29;
        }
        if (distance >= 6.80 && distance < 6.9) {
            points = 31;
        }
        if (distance >= 6.90 && distance < 7.0) {
            points = 33;
        }
        if (distance >= 7.00 && distance < 7.1) {
            points = 35;
        }
        if (distance >= 7.10 && distance < 7.2) {
            points = 37;
        }
        if (distance >= 7.20 && distance < 7.3) {
            points = 39;
        }
        if (distance >= 7.30 && distance < 7.4) {
            points = 41;
        }
        if (distance >= 7.40 && distance < 7.5) {
            points = 43;
        }
        if (distance >= 7.50 && distance < 7.6) {
            points = 45;
        }
        if (distance >= 7.60 && distance < 7.7) {
            points = 47;
        }
        if (distance >= 7.70 && distance < 7.8) {
            points = 49;
        }
        if (distance >= 7.80 && distance < 7.9) {
            points = 51;
        }
        if (distance >= 7.90 && distance < 8.0) {
            points = 53;
        }
        if (distance >= 8.00 && distance < 8.1) {
            points = 55;
        }
        if (distance >= 8.10 && distance < 8.2) {
            points = 56;
        }
        if (distance >= 8.20 && distance < 8.3) {
            points = 57;
        }
        if (distance >= 8.30 && distance < 8.4) {
            points = 58;
        }
        if (distance >= 8.40 && distance < 8.5) {
            points = 59;
        }
        if (distance >= 8.50 && distance < 8.6) {
            points = 60;
        }
        if (distance >= 8.60 && distance < 8.7) {
            points = 61;
        }
        if (distance >= 8.70 && distance < 8.8) {
            points = 62;
        }
        if (distance >= 8.80 && distance < 8.9) {
            points = 63;
        }
        if (distance >= 8.90 && distance < 9.0) {
            points = 64;
        }
        if (distance >= 9.00 && distance < 9.1) {
            points = 65;
        }
        if (distance >= 9.10 && distance < 9.2) {
            points = 66;
        }
        if (distance >= 9.20 && distance < 9.3) {
            points = 67;
        }
        if (distance >= 9.30 && distance < 9.4) {
            points = 68;
        }
        if (distance >= 9.40 && distance < 9.5) {
            points = 69;
        }
        if (distance >= 9.50 && distance < 9.6) {
            points = 70;
        }
        if (distance >= 9.60 && distance < 9.7) {
            points = 71;
        }
        if (distance >= 9.70 && distance < 9.8) {
            points = 72;
        }
        if (distance >= 9.80 && distance < 9.9) {
            points = 73;
        }
        if (distance >= 9.90 && distance < 10.0) {
            points = 74;
        }
        if (distance >= 10.00) {
            points = 75;
        }

        return points;
    }

    public int pEnvelope(String Stime) {
        float time = Float.parseFloat(Stime);
        int points = 0;
        if (time <= 27.9 && time > 27.8) {
            points = 1;
        }
        if (time <= 27.8 && time > 27.7) {
            points = 2;
        }
        if (time <= 27.7 && time > 27.6) {
            points = 3;
        }
        if (time <= 27.6 && time > 27.5) {
            points = 4;
        }
        if (time <= 27.5 && time > 27.4) {
            points = 5;
        }
        if (time <= 27.4 && time > 27.3) {
            points = 6;
        }
        if (time <= 27.3 && time > 27.2) {
            points = 7;
        }
        if (time <= 27.2 && time > 27.1) {
            points = 8;
        }
        if (time <= 27.1 && time > 27.0) {
            points = 9;
        }
        if (time <= 27.0 && time > 26.9) {
            points = 10;
        }
        if (time <= 26.9 && time > 26.8) {
            points = 11;
        }
        if (time <= 26.8 && time > 26.7) {
            points = 12;
        }
        if (time <= 26.7 && time > 26.6) {
            points = 13;
        }
        if (time <= 26.6 && time > 26.5) {
            points = 14;
        }
        if (time <= 26.5 && time > 26.4) {
            points = 15;
        }
        if (time <= 26.4 && time > 26.3) {
            points = 16;
        }
        if (time <= 26.3 && time > 26.2) {
            points = 17;
        }
        if (time <= 26.2 && time > 26.1) {
            points = 18;
        }
        if (time <= 26.1 && time > 26.0) {
            points = 19;
        }
        if (time <= 26.0 && time > 25.9) {
            points = 20;
        }
        if (time <= 25.9 && time > 25.8) {
            points = 21;
        }
        if (time <= 25.8 && time > 25.7) {
            points = 22;
        }
        if (time <= 25.7 && time > 25.6) {
            points = 23;
        }
        if (time <= 25.6 && time > 25.5) {
            points = 24;
        }
        if (time <= 25.5 && time > 25.4) {
            points = 25;
        }
        if (time <= 25.4 && time > 25.3) {
            points = 26;
        }
        if (time <= 25.3 && time > 25.2) {
            points = 27;
        }
        if (time <= 25.2 && time > 25.1) {
            points = 28;
        }
        if (time <= 25.1 && time > 25.0) {
            points = 29;
        }
        if (time <= 25.0 && time > 24.9) {
            points = 30;
        }
        if (time <= 24.9 && time > 24.8) {
            points = 31;
        }
        if (time <= 24.8 && time > 24.7) {
            points = 32;
        }
        if (time <= 24.7 && time > 24.6) {
            points = 33;
        }
        if (time <= 24.6 && time > 24.5) {
            points = 34;
        }
        if (time <= 24.5 && time > 24.4) {
            points = 35;
        }
        if (time <= 24.4 && time > 24.3) {
            points = 36;
        }
        if (time <= 24.3 && time > 24.2) {
            points = 37;
        }
        if (time <= 24.2 && time > 24.1) {
            points = 38;
        }
        if (time <= 24.1 && time > 24.0) {
            points = 39;
        }
        if (time <= 24.0 && time > 23.9) {
            points = 40;
        }
        if (time <= 23.9 && time > 23.8) {
            points = 41;
        }
        if (time <= 23.8 && time > 23.7) {
            points = 42;
        }
        if (time <= 23.7 && time > 23.6) {
            points = 43;
        }
        if (time <= 23.6 && time > 23.5) {
            points = 44;
        }
        if (time <= 23.5 && time > 23.45) {
            points = 45;
        }
        if (time <= 23.45 && time > 23.4) {
            points = 46;
        }
        if (time <= 23.4 && time > 23.35) {
            points = 47;
        }
        if (time <= 23.35 && time > 23.3) {
            points = 48;
        }
        if (time <= 23.3 && time > 23.25) {
            points = 49;
        }
        if (time <= 23.25 && time > 23.2) {
            points = 50;
        }
        if (time <= 23.2 && time > 23.15) {
            points = 51;
        }
        if (time <= 23.15 && time > 23.1) {
            points = 52;
        }
        if (time <= 23.1 && time > 23.05) {
            points = 53;
        }
        if (time <= 23.05 && time > 23.00) {
            points = 54;
        }
        if (time <= 23.00 && time > 22.95) {
            points = 55;
        }
        if (time <= 22.95 && time > 22.9) {
            points = 56;
        }
        if (time <= 22.9 && time > 22.85) {
            points = 57;
        }
        if (time <= 22.85 && time > 22.8) {
            points = 58;
        }
        if (time <= 22.8 && time > 22.75) {
            points = 59;
        }
        if (time <= 22.75 && time > 22.7) {
            points = 60;
        }
        if (time <= 22.7 && time > 22.65) {
            points = 61;
        }
        if (time <= 22.65 && time > 22.6) {
            points = 62;
        }
        if (time <= 22.6 && time > 22.55) {
            points = 63;
        }
        if (time <= 22.55 && time > 22.5) {
            points = 64;
        }
        if (time <= 22.5 && time > 22.45) {
            points = 65;
        }
        if (time <= 22.45 && time > 22.4) {
            points = 66;
        }
        if (time <= 22.4 && time > 22.35) {
            points = 67;
        }
        if (time <= 22.35 && time > 22.3) {
            points = 68;
        }
        if (time <= 22.3 && time > 22.25) {
            points = 69;
        }
        if (time <= 22.25 && time > 22.2) {
            points = 70;
        }
        if (time <= 22.2 && time > 22.15) {
            points = 71;
        }
        if (time <= 22.15 && time > 22.1) {
            points = 72;
        }
        if (time <= 22.1 && time > 22.05) {
            points = 73;
        }
        if (time <= 22.05 && time > 22) {
            points = 74;
        }
        if (time <= 22) {
            points = 75;
        }

        return points;
    }

    public int pBeepTest(int stage, int section) {
        int points = 0;
        if (stage < 5) {
            points = 0;
        }
        if (stage == 5 && section < 5) {
            points = 0;
        }
        if (stage == 5 && section == 5) {
            points = 1;
        }
        if (stage == 5 && section == 6) {
            points = 2;
        }
        if (stage == 5 && section == 7) {
            points = 3;
        }
        if (stage == 5 && section == 8) {
            points = 4;
        }
        if (stage == 5 && section == 9) {
            points = 5;
        }
        if (stage == 6 && section == 1) {
            points = 6;
        }
        if (stage == 6 && section == 2) {
            points = 7;
        }
        if (stage == 6 && section == 3) {
            points = 8;
        }
        if (stage == 6 && section == 4) {
            points = 9;
        }
        if (stage == 6 && section == 5) {
            points = 10;
        }
        if (stage == 6 && section == 6) {
            points = 11;
        }
        if (stage == 6 && section == 7) {
            points = 12;
        }
        if (stage == 6 && section == 8) {
            points = 13;
        }
        if (stage == 6 && section == 9) {
            points = 14;
        }
        if (stage == 6 && section == 10) {
            points = 15;
        }
        if (stage == 7 && section == 1) {
            points = 16;
        }
        if (stage == 7 && section == 2) {
            points = 17;
        }
        if (stage == 7 && section == 3) {
            points = 18;
        }
        if (stage == 7 && section == 4) {
            points = 19;
        }
        if (stage == 7 && section == 5) {
            points = 20;
        }
        if (stage == 7 && section == 6) {
            points = 21;
        }
        if (stage == 7 && section == 7) {
            points = 22;
        }
        if (stage == 7 && section == 8) {
            points = 23;
        }
        if (stage == 7 && section == 9) {
            points = 24;
        }
        if (stage == 7 && section == 10) {
            points = 25;
        }
        if (stage == 8 && section == 1) {
            points = 26;
        }
        if (stage == 8 && section == 2) {
            points = 27;
        }
        if (stage == 8 && section == 3) {
            points = 28;
        }
        if (stage == 8 && section == 4) {
            points = 29;
        }
        if (stage == 8 && section == 5) {
            points = 30;
        }
        if (stage == 8 && section == 6) {
            points = 31;
        }
        if (stage == 8 && section == 7) {
            points = 32;
        }
        if (stage == 8 && section == 8) {
            points = 33;
        }
        if (stage == 8 && section == 9) {
            points = 34;
        }
        if (stage == 8 && section == 10) {
            points = 35;
        }
        if (stage == 8 && section == 11) {
            points = 36;
        }
        if (stage == 9 && section == 1) {
            points = 37;
        }
        if (stage == 9 && section == 2) {
            points = 38;
        }
        if (stage == 9 && section == 3) {
            points = 39;
        }
        if (stage == 9 && section == 4) {
            points = 40;
        }
        if (stage == 9 && section == 5) {
            points = 41;
        }
        if (stage == 9 && section == 6) {
            points = 42;
        }
        if (stage == 9 && section == 7) {
            points = 43;
        }
        if (stage == 9 && section == 8) {
            points = 44;
        }
        if (stage == 9 && section == 9) {
            points = 45;
        }
        if (stage == 9 && section == 10) {
            points = 46;
        }
        if (stage == 9 && section == 11) {
            points = 47;
        }
        if (stage == 10 && section == 1) {
            points = 48;
        }
        if (stage == 10 && section == 2) {
            points = 49;
        }
        if (stage == 10 && section == 3) {
            points = 50;
        }
        if (stage == 10 && section == 4) {
            points = 51;
        }
        if (stage == 10 && section == 5) {
            points = 52;
        }
        if (stage == 10 && section == 6) {
            points = 53;
        }
        if (stage == 10 && section == 7) {
            points = 54;
        }
        if (stage == 10 && section == 8) {
            points = 55;
        }
        if (stage == 10 && section == 9) {
            points = 56;
        }
        if (stage == 10 && section == 10) {
            points = 57;
        }
        if (stage == 10 && section == 11) {
            points = 58;
        }
        if (stage == 11 && section == 1) {
            points = 59;
        }
        if (stage == 11 && section == 2) {
            points = 60;
        }
        if (stage == 11 && section == 3) {
            points = 61;
        }
        if (stage == 11 && section == 4) {
            points = 62;
        }
        if (stage == 11 && section == 5) {
            points = 63;
        }
        if (stage == 11 && section == 6) {
            points = 64;
        }
        if (stage == 11 && section == 7) {
            points = 65;
        }
        if (stage == 11 && section == 8) {
            points = 66;
        }
        if (stage == 11 && section == 9) {
            points = 67;
        }
        if (stage == 11 && section == 10) {
            points = 68;
        }
        if (stage == 11 && section == 11) {
            points = 69;
        }
        if (stage == 11 && section == 12) {
            points = 70;
        }
        if (stage == 12 && section == 1) {
            points = 71;
        }
        if (stage == 12 && section == 2) {
            points = 72;
        }
        if (stage == 12 && section == 3) {
            points = 73;
        }
        if (stage == 12 && section == 4) {
            points = 74;
        }
        if (stage == 12 && section == 5) {
            points = 75;
        }

        return points;
    }

    public void allPoints() {
        float points;
        int pointsEnvelope;
        points = pBeepTest(Integer.parseInt(textStageCount.getText().toString()),
                Integer.parseInt(textSectionCount.getText().toString()));
        if (etEnvelop.getText().toString().equals(""))
            points = points;
        else
            points += pEnvelope(etEnvelop.getText().toString());
        if (checkSexButton()) {
            // Rod
            if (etRod.getText().toString().equals(""))
                points = points;
            else
                points += pRod(Integer.parseInt(etRod.getText().toString()));
            //  Ball throw
        } else {
            if (etBall.getText().toString().equals(""))
                points = points;
            else
                points += pBall(Float.parseFloat(etBall.getText().toString()));
        }
        points = points / 3;

        points = points + checkAgeButton();

        String p = "" + points;
        p = p.substring(0, p.indexOf('.'));
        int pointsInt = Integer.parseInt(p);

        textPoints.setText(p);
        if (pointsInt <= 40) {
            tvRank.setText("1");
            tvRankDesc.setText("Niedosateczna");
        }
        if (pointsInt >= 41 && pointsInt <= 45) {
            tvRank.setText("2");
            tvRankDesc.setText("Słaba");
        }
        if (pointsInt >= 46 && pointsInt <= 50) {
            tvRank.setText("3");
            tvRankDesc.setText("Dostateczna");
        }
        if (pointsInt >= 51 && pointsInt <= 55) {
            tvRank.setText("4");
            tvRankDesc.setText("Dobra");
        }
        if (pointsInt >= 56 && pointsInt <= 60) {
            tvRank.setText("5");
            tvRankDesc.setText("Bardzo dobra");
        }
        if (pointsInt >= 61) {
            tvRank.setText("6");
            tvRankDesc.setText("Wybitna");
        }
        linearRank.setVisibility(View.VISIBLE);
    }
}