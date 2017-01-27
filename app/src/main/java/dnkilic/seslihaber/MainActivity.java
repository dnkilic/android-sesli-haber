package dnkilic.seslihaber;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dnkilic.seslihaber.data.News;
import dnkilic.seslihaber.data.Radio;
import dnkilic.seslihaber.speaker.Speaker;
import dnkilic.seslihaber.view.Dialog;
import dnkilic.seslihaber.view.DialogAdapter;
import dnkilic.seslihaber.view.NewsAdapter;
import dnkilic.seslihaber.view.RadioAdapter;
import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class MainActivity extends AppCompatActivity  {

    private static final int REQUEST_CODE = 1001;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Speaker speakerManager;
    private static int currentPosition;
    private Menu menu;
    public static HashMap<Integer, ArrayList<News>> newsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTutorial();

        speakerManager = new Speaker(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    menu.getItem(0).setIcon(R.mipmap.ic_speaker_on);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            Toast.makeText(this, "Tutorial finished", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(speakerManager != null)
        {
            speakerManager.stop();
            speakerManager.shutdown();
            menu.getItem(0).setIcon(R.mipmap.ic_speaker_on);
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
                menu.getItem(0).setIcon(R.mipmap.ic_speaker_on);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
                menu.getItem(0).setIcon(R.mipmap.ic_speaker_on);
            } else {
                menu.getItem(0).setIcon(R.mipmap.ic_speaker_off);

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
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean isIntroEnabled = sharedPref.getBoolean("prefIntro", true);

                    if(isIntroEnabled) {
                        speakerManager.play("[intro]");
                    }

                    for(News i: titleList){
                        String title = i.getTitle();
                        speakerManager.speak(title);
                        speakerManager.play("[beep]");
                    }
                }
            }
            return true;
        } else if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment implements NewsResultListener, SwipeRefreshLayout.OnRefreshListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private SwipeRefreshLayout swipeContainer;
        private RecyclerView recyclerView;
        private RecyclerView.Adapter adapter,errorDialogAdapter,radioAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private ArrayList<News> dataset;
		private ArrayList<Radio> radioDataset;
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
			radioDataset = new ArrayList<>();

            adapter = new NewsAdapter(dataset, getActivity());
            recyclerView.setAdapter(adapter);
            progressBar = (ProgressBar) rootView.findViewById(R.id.pbQueryNews);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

            showProgress(true);
            swipeContainer.setRefreshing(false);// set refreshing metodunu manipule ediyoruz, başlangıçta dönmemesi için false diyoruz
            makeNewsRequest(0);

            return rootView;
        }


        private void makeNewsRequest(int control) {
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
                    if(control == 0){
                        radioData();
                        showProgress(false);
                        radioAdapter = new RadioAdapter(radioDataset, getContext());
                        recyclerView.setAdapter(radioAdapter);
                    }
                    break;
            }
        }

		private void radioData() {
            radioDataset.add(new Radio("TRT RADYO","http://trtcanlifm-lh.akamaihd.net/i/RADYO1_1@182345/master.m3u8"));
            radioDataset.add(new Radio("HALK TV HABER","http://live4.radyotvonline.com:6670/"));
            radioDataset.add(new Radio("RADYO HABER","http://46.165.233.175:4118/"));
            radioDataset.add(new Radio("TRT RADYO HABER","http://46.20.3.210/listen.pls"));
        }
		
        @Override
        public void onSuccess(ArrayList<News> news) {
            showProgress(false);
            swipeContainer.setRefreshing(false);
            // eğer işlem başarılıysa durduuyoruz

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

        private boolean isNewsDuplicate(News news, ArrayList<News> currentNews)
        {
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
            makeNewsRequest(1);
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
