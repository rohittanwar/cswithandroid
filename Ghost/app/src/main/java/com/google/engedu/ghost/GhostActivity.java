package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String COMPUTER_WON = "Computer Won";
    private static final String USER_TURN = "Your turn";
    private static final String USER_WON = "You Won";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private boolean userchallenged = false;
    private Random random = new Random();
    private SimpleDictionary dict;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager astmgr = getAssets();
        final TextView text = (TextView) findViewById(R.id.ghostText);
        final Button chbtn=(Button)findViewById(R.id.challengebtn);
        final TextView label = (TextView) findViewById(R.id.gameStatus);
        chbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Boolean res=challenge(text.getText().toString());
                if(userchallenged)
                {
                    if((text.length()>=4 && dict.isWord(text.getText().toString())))
                    {
                        if(label.getText()==USER_TURN)
                            label.setText(USER_WON);
                        else
                            label.setText(COMPUTER_WON);
                    }
                    else
                    {
                        if(label.getText()==USER_TURN)
                            label.setText(COMPUTER_WON);
                        else
                            label.setText(USER_WON);
                    }
                }

                else
                {
                    Log.d("Result",res.toString());
                    Log.d("Label",label.getText().toString());
                if (res)
                {
                    if(label.getText()==USER_TURN)
                        label.setText(USER_WON);
                    else
                        label.setText(COMPUTER_WON);
                }
                else
                {
                    if(label.getText()==USER_TURN)
                        label.setText(COMPUTER_WON);
                    else
                        label.setText(USER_WON);
                }
                }

            }


        });
        final Button resetbtn=(Button)findViewById(R.id.resetbtn);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userchallenged=false;
                userTurn=false;
               onStart(null);
            }
        });
        InputStream is = null;
        try {
            is = astmgr.open("words.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
             dict = new SimpleDictionary(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    private void computerTurn() {
        final TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        //userTurn = true;
//        label.setText(USER_TURN);
        userTurn=false;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                final TextView text = (TextView) findViewById(R.id.ghostText);
                String txt=text.getText().toString();
                if(txt.length()>=4 && dict.isWord(txt))
                {
                    Log.d("Comp","Valid Word>4");
                    Toast.makeText(getApplicationContext(),"You are challenged and You Lose",Toast.LENGTH_SHORT).show();
                    label.setText(COMPUTER_WON);
                }
                else if(dict.getAnyWordStartingWith(txt)!=null)
                {
                    Log.d("Label1",label.getText().toString());
                    int len=txt.length();
                    char ch=dict.getAnyWordStartingWith(txt).charAt(len);
                    txt+=ch;
                    text.setText(txt);
                    label.setText(USER_TURN);
                }
                else if(dict.getAnyWordStartingWith(txt)==null)
                {
                    Log.d("Comp","User CHallenged");
                    Toast.makeText(getApplicationContext(),"You are challenged, Provide a word " +
                            "starting with the given prefix or You Lose",Toast.LENGTH_SHORT).show();
                    userchallenged=true;
                    label.setText(USER_TURN);

                }

            }
        }, 1000);

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
    public boolean onKeyUp (int keyCode, KeyEvent event)
    {
        TextView text = (TextView) findViewById(R.id.ghostText);
            if(keyCode>=29 &&keyCode<=54) {
                char unicodeChar = (char)event.getUnicodeChar();
                Log.d("Key Pres", Character.toString(unicodeChar));
                String txt=text.getText().toString();
                String newtext=txt+Character.toString(unicodeChar);
                text.setText(newtext);
                TextView label = (TextView) findViewById(R.id.gameStatus);

                if(!userchallenged) {
                    Log.d("Label2", label.getText().toString());
                    label.setText(COMPUTER_TURN);
                    computerTurn();
                }

                return true;
            }
            else
                return super.onKeyUp(keyCode,event);
    }
    public boolean challenge(String text)
    {
        if((text.length()>=4 && dict.isWord(text)) ||  dict.getAnyWordStartingWith(text)==null )
        {
            return true;
        }
        else if(dict.getAnyWordStartingWith(text)!=null)
        {
            final TextView txt = (TextView) findViewById(R.id.ghostText);
            txt.setText(dict.getAnyWordStartingWith(text));
            return false;
        }
        return true;
    }
}
