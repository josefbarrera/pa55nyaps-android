package prototype.pa55nyaps.gui;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.pa55nyaps.pa55nyaps.R;

import java.io.File;

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

    private void renderFileListing() {
        String db_directory_name = getResources().getString(R.string.databases_directory);
        File databases_directory = new File(getBaseContext().getFilesDir(), db_directory_name);
        if (!databases_directory.isDirectory()) {
            Log.i("Create directoy", "Creating parent directoy " + databases_directory.getName());
            databases_directory.mkdir();
        }
        files = databases_directory.listFiles();

        Log.i("Stat debugging", "Start listing " + files.length + " files");
        for (File file : files) {
            Log.i("File found", file.getName());
        }
        // TODO
        // List all databases if any found
        // otherwise show the help text
        // call renderFileListing when a database is deleted

    }
}
