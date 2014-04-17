package com.gibb.taskone.app;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.gibb.taskone.app.contentprovider.MyTodoContentProvider;
import com.gibb.taskone.app.database.TodoTable;


/***
 * Die Aktivität TodoDetailActivity erlaubt die Eingabe neuer Tasks oder das Ändern von bestehenden Tasks
 */
public class TodoDetailActivity extends Activity {
    /***
     * Spinner: Dropdown-Element zur Auswahl der Kategorie
     * EditText: Texteingabefelder für Titel und Inhalt
     * todoUri: Uri-Objekt (wird verwendet für ContentProvider)
     */
    private Spinner mCategory;
    private EditText mTitleText;
    private EditText mBodyText;
    private Uri todoUri;

    /***
     *
     * @param bundle Map von String-Werten
     *               Übergibt die Parameter bei Erstellung an die Superklasse (Activity)
     *               --> erstellt das Activity-Fenster (setContentView)
     *
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.todo_edit);

        /***
         * Übernehmen der Layout-Elemente (byID)
         */
        mCategory = (Spinner) findViewById(R.id.category);
        mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
        mBodyText = (EditText) findViewById(R.id.todo_edit_description);
        Button confirmButton = (Button) findViewById(R.id.todo_edit_button);

        /***
         * Zusatzinformationen des Intent auslesen
         */
        Bundle extras = getIntent().getExtras();

        /***
         * Falls bundle nicht leer: Übernehmen der URI aus der Klasse MyTodoContentProvider
         */
        todoUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        /***
         * Oder Übernehmen der Uri aus dem extras-Bundle falls dieses nicht leer ist (--> aus der anderen Activity)
         */
        if (extras != null) {
            todoUri = extras
                    .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);
            fillData(todoUri);
        }
        /***
         * onClick-EventListener auf dem confirm-Button setzen
         */
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Falls kein Inhalt vorhanden: Benachrichtigung
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    /***
     *
     * @param uri
     */
    private void fillData(Uri uri) {
        String[] projection = { TodoTable.COLUMN_SUMMARY,
                TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));

            for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String) mCategory.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }

            mTitleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_SUMMARY)));
            mBodyText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));

            // always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String category = (String) mCategory.getSelectedItem();
        String summary = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();

        // only save if either summary or description
        // is available

        if (description.length() == 0 && summary.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TodoTable.COLUMN_CATEGORY, category);
        values.put(TodoTable.COLUMN_SUMMARY, summary);
        values.put(TodoTable.COLUMN_DESCRIPTION, description);

        if (todoUri == null) {
            // New todo
            todoUri = getContentResolver().insert(MyTodoContentProvider.CONTENT_URI, values);
        } else {
            // Update todo
            getContentResolver().update(todoUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(TodoDetailActivity.this, "Please maintain a summary",
                Toast.LENGTH_LONG).show();
    }
} 