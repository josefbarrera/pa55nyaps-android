package prototype.pa55nyaps.gui.model;

import android.content.Context;
import android.util.Log;

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
 * Created by Jose Barrera on 11/9/17.
 */

public class PasswordDatabaseFile {

    protected Context context;
    protected String filename;
    protected String password;
    protected File file;
    protected PasswordDatabase database;
    protected String db_directory_name;

    public static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();

    public PasswordDatabaseFile(String filename, String password, Context context) {
        this.context = context;
        this.filename = filename;
        this.password = password;
        db_directory_name = this.context.getFilesDir() + File.separator
                + this.context.getResources().getString(R.string.databases_directory);
        file = new File(db_directory_name, this.filename);
        database = new PasswordDatabase();
    }

    public boolean save() {
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
            return true;
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            Log.e("Exception", ex.getMessage());
        }
        return false;
    }

    public boolean delete()
    {
        return false;
    }
}
