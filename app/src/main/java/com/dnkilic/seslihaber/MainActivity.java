package com.dnkilic.seslihaber;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.canelmas.let.AskPermission;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dnkilic.seslihaber.data.News;
import com.dnkilic.seslihaber.data.Radio;
import com.dnkilic.seslihaber.recognition.RecognitionManager;
import com.dnkilic.seslihaber.speaker.Speaker;
import com.dnkilic.seslihaber.view.Dialog;
import com.dnkilic.seslihaber.view.DialogAdapter;
import com.dnkilic.seslihaber.view.NewsAdapter;
import com.dnkilic.seslihaber.view.RadioAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity implements RecognitionListener, RuntimePermissionListener {

    private static final int REQUEST_CODE = 1001;
    private RecognitionManager recognitionManager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Speaker speakerManager;
    private static int currentPosition;
    private Menu menu;
    public static HashMap<Integer, ArrayList<News>> newsMap = new HashMap<>();
    private FirebaseAnalytics mFirebaseAnalytics;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseCrash.log("Activity created : ");

        act = this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        loadTutorial();

        speakerManager = new Speaker(this);
        recognitionManager = new RecognitionManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

                if(speakerManager != null)
                {
                    speakerManager.stop();

                    if(menu != null)
                    {
                        menu.getItem(0).setIcon(R.mipmap.ic_speaker_play);
                    }
                }

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void loadTutorial() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean firstLaunch = sharedPref.getBoolean("prefFirstLaunch", true);

        if(firstLaunch)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("prefFirstLaunch", false);
            editor.apply();

            Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
            mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
            startActivityForResult(mainAct, REQUEST_CODE);
        }
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.pull_to_refresh), null,
                R.color.slide_1, R.drawable.pull_to_refresh);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.news_title, R.string.news_2_title,
                R.color.slide_2,  R.drawable.news_title,  R.drawable.news_title);

        TutorialItem tutorialItem3 = new TutorialItem(R.string.read_news, R.string.read_2_news,
                R.color.slide_3,  R.drawable.read_news, R.drawable.read_news_background);

        TutorialItem tutorialItem4 = new TutorialItem(context.getString(R.string.listen_radio), null,
                R.color.slide_4, R.drawable.listen_radio);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(speakerManager != null)
        {
            speakerManager.stop();
            speakerManager.shutdown();
            menu.getItem(0).setIcon(R.mipmap.ic_speaker_play);
        }

        if(recognitionManager != null)
        {
            recognitionManager.destroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(speakerManager != null)
        {
            speakerManager.stop();

            if(menu != null)
            {
                menu.getItem(0).setIcon(R.mipmap.ic_speaker_play);
            }
        }

        if(recognitionManager != null)
        {
            recognitionManager.destroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Let.handle(this, requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        try
        {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    final View speakerButton = act.findViewById(R.id.speakerButton);
                    if(speakerButton != null)
                    {
                        speakerButton.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return false;
                            }
                        });

                    }
                    final View about = act.findViewById(R.id.about);
                    if(about != null)
                    {
                        about.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return false;
                            }
                        });

                    }
                    final View settings = act.findViewById(R.id.settings);
                    if(settings != null)
                    {
                        settings.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return false;
                            }
                        });

                    }
                    final View microphoneButton = act.findViewById(R.id.microphoneButton);
                    if(microphoneButton != null)
                    {
                        microphoneButton.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return false;
                            }
                        });

                    }
                }
            });
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if(item.getItemId()== R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if(item.getItemId() == R.id.speakerButton){
            if(speakerManager.isSpeaking()){
                speakerManager.stop();
                menu.getItem(0).setIcon(R.mipmap.ic_speaker_play);
            } else {

                ArrayList<News> titleList = null;

                for(Map.Entry<Integer, ArrayList<News>> entry : newsMap.entrySet()) {
                    Integer key = entry.getKey();

                    if (currentPosition == key) {
                        titleList = entry.getValue();
                        break;
                    }
                }

                if(titleList != null && !titleList.isEmpty())
                {
                    menu.getItem(0).setIcon(R.mipmap.ic_speaker_stop);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean isIntroEnabled = sharedPref.getBoolean("prefIntro", true);

                    if(isIntroEnabled) {
                        speakerManager.play("[intro]");
                    }

                    for(News i: titleList){
                        String title = i.getTitle();
                        if(speakerManager.speak(title) == TextToSpeech.SUCCESS)
                        {
                            speakerManager.play("[beep]");
                        }
                    }
                }
                else
                {
                    Toast.makeText(this, "Seslendirilecek haber bulunamadı.", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        } else if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.microphoneButton){
            startRecognition();

        }
        return super.onOptionsItemSelected(item);
    }

    @AskPermission(RECORD_AUDIO)
    private void startRecognition() {
        recognitionManager.start();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {}

    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onRmsChanged(float rmsdB) {}

    @Override
    public void onBufferReceived(byte[] buffer) {}

    @Override
    public void onEndOfSpeech() {}

    @Override
    public void onError(int error) {

        switch (error)
        {
            case SpeechRecognizer.ERROR_AUDIO:
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                break;
            case SpeechRecognizer.ERROR_SERVER:
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                break;
        }

        Toast.makeText(this, "Ses tanıma yapılırken bir hata oluştu. Hata kodu : " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> textMatchList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if(textMatchList != null && !textMatchList.isEmpty())
        {
            String result = textMatchList.get(0).toLowerCase();

            if(result.contains("güncel")) {
                mViewPager.setCurrentItem(0);
            } else if (result.contains("spor")) {
                mViewPager.setCurrentItem(1);
            } else if (result.contains("ekonomi")) {
                mViewPager.setCurrentItem(2);
            } else if (result.contains("türkiye")) {
                mViewPager.setCurrentItem(3);
            } else if (result.contains("dünya")) {
                mViewPager.setCurrentItem(4);
            } else if (result.contains("kültür") || result.contains("sanat")) {
                mViewPager.setCurrentItem(5);
            } else if (result.contains("politika")) {
                mViewPager.setCurrentItem(6);
            } else if (result.contains("bilim") || result.contains("teknoloji")) {
                mViewPager.setCurrentItem(7);
            } else if (result.contains("yaşam")) {
                mViewPager.setCurrentItem(8);
            } else if (result.contains("sağlık")) {
                mViewPager.setCurrentItem(9);
            } else if (result.contains("analiz")) {
                mViewPager.setCurrentItem(10);
            } else if (result.contains("günün") || result.contains("başlıkları")) {
                mViewPager.setCurrentItem(11);
            } else if (result.contains("radyo")) {
                mViewPager.setCurrentItem(12);
            }

            Toast.makeText(this, "Tanıma sonucu : " + textMatchList.get(0), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    @Override
    public void onShowPermissionRationale(List<String> permissionList, final RuntimePermissionRequest permissionRequest) {

        new AlertDialog.Builder(this, R.style.AppCompatProgressDialogStyle).setTitle("İzine İhtiyaç Var!")
                .setMessage("Ses tanıma yapabilmek için Mikrofon iznine ihtiyacım var.")
                .setCancelable(true)
                .setNegativeButton("Hayır Teşekkkürler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Tekrar Dene", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionRequest.retry();
                    }
                })
                .show();
    }

    @Override
    public void onPermissionDenied(List<DeniedPermission> deniedPermissionList) {
        new AlertDialog.Builder(this, R.style.AppCompatProgressDialogStyle).setTitle("Ayarlara Giderek İzin Ver")
                .setMessage("Ses tanıma yapabilmek için Mikrofon iznine ihtiyacım var.")
                .setCancelable(true)
                .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 1);

                        dialog.dismiss();
                    }
                }).show();
    }

    public static class PlaceholderFragment extends Fragment implements NewsResultListener, SwipeRefreshLayout.OnRefreshListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private SwipeRefreshLayout swipeContainer;
        private RecyclerView recyclerView;
        private RecyclerView.Adapter adapter,errorDialogAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private ArrayList<News> dataset;
        private ArrayList<Dialog> errorDialogList;
        private ProgressBar progressBar;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                    R.color.colorPrimaryDark,
                    R.color.colorPrimary,
                    R.color.colorPrimaryDark);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            dataset = new ArrayList<>();

            adapter = new NewsAdapter(dataset, getActivity());
            recyclerView.setAdapter(adapter);
            progressBar  = (ProgressBar) rootView.findViewById(R.id.pbQueryNews);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

            showProgress(true);
            swipeContainer.setRefreshing(false);
            makeNewsRequest();

            return rootView;
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        private void makeNewsRequest() {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    new RssFeedParser(this).execute("guncel");
                    break;
                case 1:
                    new RssFeedParser(this).execute("spor");
                    break;
                case 2:
                    new RssFeedParser(this).execute("ekonomi");
                    break;
                case 3:
                    new RssFeedParser(this).execute("turkiye");
                    break;
                case 4:
                    new RssFeedParser(this).execute("dunya");
                    break;
                case 5:
                    new RssFeedParser(this).execute("kultur-sanat");
                    break;
                case 6:
                    new RssFeedParser(this).execute("politika");
                    break;
                case 7:
                    new RssFeedParser(this).execute("bilim-teknoloji");
                    break;
                case 8:
                    new RssFeedParser(this).execute("yasam");
                    break;
                case 9:
                    new RssFeedParser(this).execute("saglik");
                    break;
                case 10:
                    new RssFeedParser(this).execute("analiz-haber");
                    break;
                case 11:
                    new RssFeedParser(this).execute("gunun-basliklari");
                    break;
                case 12:
                    showProgress(false);
                    swipeContainer.setEnabled(false);
                    RadioAdapter radioAdapter = new RadioAdapter(insertRadioChannels(), getContext());
                    recyclerView.setAdapter(radioAdapter);
                    break;
            }
        }

        private ArrayList<Radio> insertRadioChannels() {
            ArrayList<Radio> radioList = new ArrayList<>();
            radioList.add(new Radio("Trt Radyo","http://trtcanlifm-lh.akamaihd.net/i/RADYO1_1@182345/master.m3u8"));
            radioList.add(new Radio("Halk Tv Haber","http://live4.radyotvonline.com:6670/"));
            radioList.add(new Radio("Radyo Haber","http://46.165.233.175:4118/"));
            radioList.add(new Radio("Trt Radyo Haber","http://46.20.3.210/listen.pls"));
            return radioList;
        }

        @Override
        public void onSuccess(ArrayList<News> news) {
            showProgress(false);
            swipeContainer.setRefreshing(false);

            for(News item : news)
            {
                if(!isNewsDuplicate(item, dataset))
                {
                    dataset.add(item);
                }
            }

            newsMap.put(getArguments().getInt(ARG_SECTION_NUMBER), dataset);
            adapter.notifyDataSetChanged();
        }

        private boolean isNewsDuplicate(News news, ArrayList<News> currentNews) {
            for(News current : currentNews)
            {
                if(news.getId().equals(current.getId()))
                {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void onFail(boolean error, String errorMessage) {
            errorDialogList = new ArrayList<>();
            showProgress(false);
            swipeContainer.setRefreshing(false);

            if (error)
            {
                Dialog errorDialog = new Dialog(errorMessage);
                errorDialogList.add(errorDialog);
                errorDialogAdapter = new DialogAdapter(errorDialogList);
                recyclerView.setAdapter(errorDialogAdapter);

            }else
            {
                Dialog errorDialog = new Dialog(getString(R.string.common_error));
                errorDialogList.add(errorDialog);
                errorDialogAdapter = new DialogAdapter(errorDialogList);
                recyclerView.setAdapter(errorDialogAdapter);
            }
        }


        private void showProgress(final boolean show) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }

        @Override
        public void onRefresh() {
            makeNewsRequest();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 13;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "GÜNCEL";
                case 1:
                    return "SPOR";
                case 2:
                    return "EKONOMİ";
                case 3:
                    return "TÜRKİYE";
                case 4:
                    return "DÜNYA";
                case 5:
                    return "KÜLTÜR SANAT";
                case 6:
                    return "POLİTİKA";
                case 7:
                    return "BİLİM TEKNOLOJİ";
                case 8:
                    return "YAŞAM";
                case 9:
                    return "SAĞLIK";
                case 10:
                    return "ANALİZ HABER";
                case 11:
                    return "GÜNÜN BAŞLIKLARI";
                case 12:
                    return "RADYO";
            }
            return null;
        }
    }
}
