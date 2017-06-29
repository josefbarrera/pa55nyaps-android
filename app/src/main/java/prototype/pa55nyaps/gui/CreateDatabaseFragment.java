package prototype.pa55nyaps.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pa55nyaps.pa55nyaps.R;

import org.json.JSONException;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;

import prototype.pa55nyaps.crypto.AESCryptosystem;
import prototype.pa55nyaps.dataobjects.Ciphertext;
import prototype.pa55nyaps.dataobjects.PasswordDatabase;

/**
 */
public class CreateDatabaseFragment extends DialogFragment {

    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    DialogListener mListener;

    public static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.fragment_create_database, null);
        final TextInputEditText userInput = (TextInputEditText) promptsView.findViewById(R.id.password);

        builder.setView(promptsView)
                .setMessage(R.string.new_password_prompt)
                .setTitle(R.string.new_password_prompt_title)
                .setPositiveButton(R.string.continue_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String db_directory_name = getContext().getFilesDir() + File.separator +
                                getResources().getString(R.string.databases_directory);
                        String filename = "db" + (System.currentTimeMillis()/1000L) + ".pa55";
                        File file = new File(db_directory_name, filename);
                        PasswordDatabase database = new PasswordDatabase();
                        String password = userInput.getText().toString();
                        // TODO: do we need to check if password is not empty?
                        // create the password settings database
                        saveDatabaseToFile(database, file, password);
                        mListener.onDialogPositiveClick(CreateDatabaseFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        CreateDatabaseFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();

        // Verify that the host activity implements the callback interface
        try {
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogListener");
        }
    }

    public void saveDatabaseToFile(PasswordDatabase database, File file, String password) {
        PrintStream pos;
        try {
            String json = gson.toJson(database);
            if (json == null) {
                throw new JSONException("The password database cannot be serialized.");
            }
            Ciphertext ciphertext = AESCryptosystem.getInstance().encryptWithHmac(json, password);
            String jsoncipher = gson.toJson(ciphertext);
            pos = new PrintStream(file.getAbsolutePath());
            pos.print(jsoncipher);
            pos.flush();
            pos.close();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            Log.e("Exception", ex.getMessage());
        }
    }
}
