package dnkilic.seslihaber;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dnkilic.seslihaber.data.News;
import dnkilic.seslihaber.speaker.Speaker;
import dnkilic.seslihaber.view.Dialog;
import dnkilic.seslihaber.view.DialogAdapter;
import dnkilic.seslihaber.view.NewsAdapter;

public class MainActivity extends AppCompatActivity  {

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
            menu.getItem(0).setIcon(R.mipmap.ic_speaker_on);
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
                    speakerManager.play("[intro]");

                    for(News i: titleList){
                        String title = i.getTitle();
                        speakerManager.speak(title);
                        speakerManager.play("[beep]");
                    }
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment implements NewsResultListener, SwipeRefreshLayout.OnRefreshListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private SwipeRefreshLayout swipeContainer;
        private RecyclerView rvNews;
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

            rvNews = (RecyclerView) rootView.findViewById(R.id.rvNews);
            mLayoutManager = new LinearLayoutManager(getContext());
            rvNews.setLayoutManager(mLayoutManager);
            dataset = new ArrayList<>();

            adapter = new NewsAdapter(dataset, getActivity());
            rvNews.setAdapter(adapter);
            progressBar = (ProgressBar) rootView.findViewById(R.id.pbQueryNews);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

            showProgress(true);

            makeNewsRequest();

            return rootView;
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
            }
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
                rvNews.setAdapter(errorDialogAdapter);

            }else
            {
                Dialog errorDialog = new Dialog(getString(R.string.common_error));
                errorDialogList.add(errorDialog);
                errorDialogAdapter = new DialogAdapter(errorDialogList);
                rvNews.setAdapter(errorDialogAdapter);
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
            return 12;
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
            }
            return null;
        }
    }
}
