package hexaram.challengelyon.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.User;
import hexaram.challengelyon.ui.activities.ProfileViewActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements ItemMenuAdapter.ClickListener {

    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";

    private RecyclerView recyclerView;
    private ProfileViewFragment profileView;
    private View containerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ItemMenuAdapter adapter;

    private boolean mUserLearnedDrawer = false;
    private boolean mFromSavedInstanceState = false;

    private User user;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
/*
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new ItemMenuAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
*/
        profileView = (ProfileViewFragment) getChildFragmentManager().findFragmentById(R.id.fragment_navigation_profile);


        return layout;
    }

    public static List<ItemMenu> getData() {
        List<ItemMenu> data = new ArrayList<>();
        int[] icons= {R.drawable.ic_menu_home, R.drawable.ic_menu_agenda, R.drawable.ic_menu_allfriends, R.drawable.ic_menu_call};
        String[] titles = {"Profile", "Titre 2", "Titre 3", "Titre 4"};

        for (int i = 0; i < titles.length && i < icons.length; ++i) {
            ItemMenu current = new ItemMenu();
            current.iconId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, User user) {
        this.user = user;
        mDrawerLayout = drawerLayout;
        containerView = (View) getActivity().findViewById(fragmentId);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // to avoid black toolbar
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1-slideOffset);
                }
            }
        };



        if(!mUserLearnedDrawer && !mFromSavedInstanceState) {
            // app int fragment id to setUp method => video #9 6min30
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, int position) {
    if(position == 0){
        startActivity(new Intent(getActivity(), ProfileViewActivity.class));
    } else {
        //TODO
    }
       // startActivity(new Intent(getActivity(), ProfileViewActivity.class));
      /*  Log.d("myTag", R.id.pager+"");
        Fragment newFragment = ProfileViewFragment.newInstance (user);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();*/

    }
}
