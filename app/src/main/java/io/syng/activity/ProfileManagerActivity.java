package io.syng.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.syng.entities.Profile;


public class ProfileManagerActivity extends BaseActivity implements OnFragmentInteractionListener {

    private AddProfileFragment addProfileFragment;
    private ProfileManagerFragment profileManagerFragment;

    private TextView saveProfileLink;
    private TextView addProfileLink;

    private View profileManagerContainer;
    private View addProfileContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);

        saveProfileLink = (TextView)findViewById(R.id.save_profile_link);
        saveProfileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Profile profile = addProfileFragment.getProfile();
                if (profile.getPasswordProtectedProfile()) {
                    new MaterialDialog.Builder(ProfileManagerActivity.this)
                            .title(R.string.request_profile_password)
                            .customView(R.layout.profile_password, true)
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .contentColor(R.color.accent) // notice no 'res' postfix for literal color
                            .dividerColorRes(R.color.accent)
                            .backgroundColorRes(R.color.primary_dark)
                            .positiveColorRes(R.color.accent)
                            .negativeColorRes(R.color.accent)
                            .widgetColorRes(R.color.accent)
                            .callback(new MaterialDialog.ButtonCallback() {

                                @Override
                                public void onPositive(MaterialDialog dialog) {

                                    View view = dialog.getCustomView();
                                    EditText passwordInput = (EditText) view.findViewById(R.id.passwordInput);
                                    profile.encrypt(passwordInput.getText().toString());
                                    profileManagerFragment.addProfile(profile);
                                    hideAddProfile();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {

                                    dialog.hide();
                                    spinner.setSelection(currentPosition, false);
                                }
                            })
                            .build()
                            .show();
                } else {
                profileManagerFragment.addProfile(profile);
                hideAddProfile();
            }
            }
        });
        addProfileLink = (TextView) findViewById(R.id.add_profile_link);
        addProfileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileManagerFragment.resetProfilePosition();
                showAddProfile(null);
            }
        });

        if (savedInstanceState == null) {
            addProfileFragment = new AddProfileFragment();
            profileManagerFragment = new ProfileManagerFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.profileManagerFragmentContainer, profileManagerFragment)
                    .add(R.id.addProfileFragmentContainer, addProfileFragment)
                    .commit();
        }

        addProfileContainer = findViewById(R.id.addProfileFragmentContainer);
        profileManagerContainer = findViewById(R.id.profileManagerFragmentContainer);

        hideAddProfile();
    }

    public void showAddProfile(Profile profile) {

        profileManagerContainer.setVisibility(View.INVISIBLE);
        addProfileLink.setVisibility(View.INVISIBLE);

        addProfileContainer.setVisibility(View.VISIBLE);
        saveProfileLink.setVisibility(View.VISIBLE);

        addProfileFragment.setProfile(profile);

        getSupportActionBar().setTitle(R.string.add_profile);
    }

    public void hideAddProfile() {

        addProfileContainer.setVisibility(View.INVISIBLE);
        profileManagerContainer.setVisibility(View.VISIBLE);
        addProfileLink.setVisibility(View.VISIBLE);
        saveProfileLink.setVisibility(View.INVISIBLE);

        getSupportActionBar().setTitle(R.string.profile_manager_title);
    }

    public void addProfile(Profile profile) {
        profileManagerFragment.addProfile(profile);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
    }

        return super.onOptionsItemSelected(item);
    }


}