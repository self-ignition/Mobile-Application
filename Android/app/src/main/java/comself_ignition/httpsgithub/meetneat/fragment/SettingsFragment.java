package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.OutputStreamWriter;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.LoginActivity;
import comself_ignition.httpsgithub.meetneat.activity.MainActivity;
import comself_ignition.httpsgithub.meetneat.activity.SearchResultsActivity;
import comself_ignition.httpsgithub.meetneat.other.CircleTransform;
import comself_ignition.httpsgithub.meetneat.other.ResetPassword;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private Fragment fragmentInFocus;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                doMySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_logout:
                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_LONG).show();

                SaveSharedPreference.setLoggedIn(getActivity(), false);

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doMySearch(String query) {
        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
        intent.putExtra("query", query);
        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button btn = (Button) rootView.findViewById(R.id.btn_resetPassword);
        btn.setOnClickListener(this);

        Button btn2 = (Button) rootView.findViewById(R.id.btn_changeProfilePicture);
        btn2.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resetPassword:
                Intent reset = new Intent(getActivity(),ResetPassword.class);
                getActivity().startActivity(reset);
                break;
            case R.id.btn_changeProfilePicture:
                image();
                break;
            default:
                break;
        };
    }

    public void image() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);

        Intent home = new Intent(getContext(), MainActivity.class);
        getActivity().startActivity(home);
    }

}
