package dnkilic.anadoluajans;

import android.support.design.widget.TabLayout;
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
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dnkilic.anadoluajans.data.News;
import dnkilic.anadoluajans.view.NewsAdapter;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements NewsResultListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private RecyclerView rvNews;
        private RecyclerView.Adapter adapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private ArrayList<News> dataset;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rvNews = (RecyclerView) rootView.findViewById(R.id.rvNews);
            mLayoutManager = new LinearLayoutManager(getContext());
            rvNews.setLayoutManager(mLayoutManager);
            dataset = new ArrayList<>();
            adapter = new NewsAdapter(dataset, getContext());
            rvNews.setAdapter(adapter);

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
            }

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, ));

            return rootView;
        }

        @Override
        public void onSuccess(ArrayList<News> news) {
            for(News item : news)
            {
                dataset.add(item);
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFail() {

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
            // Show 3 total pages.
            return 3;
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
            }
            return null;
        }
    }
}
