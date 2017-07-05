package prototype.pa55nyaps.gui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.pa55nyaps.pa55nyaps.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StartScreen extends AppCompatActivity
                         implements CreateDatabaseFragment.DialogListener {

    File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        renderFileListing();

        Toolbar toolbar = (Toolbar) findViewById(R.id.primaryToolbar);
        setSupportActionBar(toolbar);

        ImageButton addDatabase = (ImageButton) findViewById(R.id.addDatabase);
        addDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open create database dialog
                DialogFragment dialog = new CreateDatabaseFragment();
                dialog.show(getSupportFragmentManager(), "CreateDatabaseFragment");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addDatabase:
                return true;
            case R.id.edit_settings_databases:
                editSettingsDatabases();
            default:
                // If we get here, the user's action was not recognised.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        renderFileListing();
    }

    public void editSettingsDatabases() {
        Log.i("Debugging", "Clicked on edit");
    }

    private void renderFileListing() {
        String db_directory_name = getResources().getString(R.string.databases_directory);
        File databases_directory = new File(getBaseContext().getFilesDir(), db_directory_name);
        if (!databases_directory.isDirectory()) {
            Log.i("Create directory", "Creating parent directory " + databases_directory.getName());
            databases_directory.mkdir();
        }
        files = databases_directory.listFiles();
        TextView helpText = (TextView) findViewById(R.id.createDbHelpText);
        ListView filesListing = (ListView) findViewById(R.id.filesListing);

        Log.i("Number of files", "There are currently " + files.length + " files");
        if (files.length == 0) {
            // Make help text visible and files listing invisible
            helpText.setVisibility(View.VISIBLE);
            filesListing.setVisibility(View.GONE);
            return;
        }

        // Set the file listing footer
        View footerView = getLayoutInflater().inflate(R.layout.file_listing_footer_layout, null, false);
        filesListing.addFooterView(footerView);

        // Hide the help text
        helpText.setVisibility(View.GONE);

        ArrayList<HashMap<String, String>> filesList = new ArrayList<>();
        for (File file : files) {
            Date unformattedLastModified = new Date(file.lastModified());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String lastModified= "Updated: " + formatter.format(unformattedLastModified);

            HashMap<String, String> fileDetails = new HashMap<>();
            fileDetails.put("fileName", file.getName());
            fileDetails.put("lastModified", lastModified);
            filesList.add(fileDetails);
        }

        String[] from = {"fileName", "lastModified"};
        int[] to = {R.id.fileName, R.id.lastModified};

        // Add the file items to the list view and display it
        filesListing.setAdapter(new SimpleAdapter(StartScreen.this, filesList, R.layout.file_list_layout, from, to));
        filesListing.setVisibility(View.VISIBLE);
        // TODO call renderFileListing when a database is deleted

    }
}
