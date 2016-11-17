package com.example.vipulsublaniya.cs654a_assign1_dac;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    GridView gridView;
    //String[] calcItem={"C","÷","X","7","8","9","4","5","6","1","2","3",".","0","+/-"};
    ArrayAdapter adapter;
    TextView check;
    EditText etext;
    String operator,result,operand1,operand2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView=(GridView)findViewById(R.id.gridView);
        check=(TextView) findViewById(R.id.textfinal);
        etext=(EditText)findViewById(R.id.editText);
        check.setText("DAC");
        /*define arrayadapter---------------------------
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, calcItem);
        gridView.setAdapter(adapter);
        /-------------------------------------------------*/

        //------define base adapter--------------------

        gridView.setAdapter(new Badapter(this));

        //--------------------------------------------


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                //etext.setText(etext.getText().insert(etext.getText().length(), String.valueOf(position)));
                //check.setText("vipul");

                if(position>3 && position<7){
                    etext.setText(etext.getText().insert(etext.getText().length(), String.valueOf(position + 3)));
                }else if(position>7 && position<11){
                    etext.setText(etext.getText().insert(etext.getText().length(), String.valueOf(position-4)));
                }else if(position>11 && position<15){
                    etext.setText(etext.getText().insert(etext.getText().length(), String.valueOf(position-11)));
                }else if(position==16){
                    etext.setText(etext.getText().insert(etext.getText().length(), "."));
                }else if(position==17){
                    etext.setText(etext.getText().insert(etext.getText().length(), "0"));
                }else if(position==3){
                    etext.setText(etext.getText().toString().substring(0,etext.getText().toString().length()-1));
                }else if(position==18){
                    if(etext.getText().toString()==""){
                        etext.setText(etext.getText().insert(etext.getText().length(), "-"));
                    }
                    else if(etext.getText().toString().charAt(0)=='-'){
                        etext.setText(etext.getText().toString().substring(1,etext.getText().toString().length()));
                    }else{
                        etext.setText("-"+etext.getText().toString());
                    }
                }else if(position==1 || position==2 ||position==7 ||position==11){
                    if(position==11){
                        operator="+";
                        operand1=etext.getText().toString();
                        etext.setText("");
                    }else if(position==7){
                        operator="-";
                        operand1=etext.getText().toString();
                        etext.setText("");
                    }else if(position==2){
                        operator="*";
                        operand1=etext.getText().toString();
                        etext.setText("");
                    }if(position==1){
                        operator="/";
                        operand1=etext.getText().toString();
                        etext.setText("");
                    }
                }else if(position==0){
                    etext.setText("");
                }else if(position==19){
                    operand2=etext.getText().toString();
                    Atask calculate=new Atask();
                    calculate.execute(operand1, operand2, operator);

                }
                Selection.setSelection(etext.getText(),etext.getText().toString().length());
            }
        });

    }
    class Singleitem{
        String title;
        //int image
        //Now make constructor
        Singleitem(String title){
            this.title=title;
        }
    }
    class Badapter extends BaseAdapter{
        ArrayList<Singleitem> list;
        Context context;

        Badapter(Context c){
            context=c;
            list=new ArrayList<Singleitem>();
            Resources res=c.getResources();
            String[] title=res.getStringArray(R.array.calcitem);

            for(int i=0;i<20;i++){
                list.add(new Singleitem(title[i]));
            }

        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.sampleview, parent, false);


            TextView title=(TextView)row.findViewById(R.id.textView);

            Singleitem temp=list.get(position);
            title.setText(temp.title);
            //title.setGravity(Gravity.CENTER);

            return row;
        }
    }

    class Atask extends AsyncTask<String,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            etext.setText(result);
            Selection.setSelection(etext.getText(), etext.getText().toString().length());
        }

        @Override
        protected Void doInBackground(String... params) {
            String url="http://dac.comli.com/finalDac.php";
            //String url="http://192.168.137.1/phpfiles/dac/finalDac.php";//address of my localhost
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(url);
                try {
                    JSONObject jobj=new JSONObject();
                    jobj.put("operand1",params[0]);
                jobj.put("operand2",params[1]);
                jobj.put("operator",params[2]);

                Log.e("mainToPost", "mainToPost" + jobj.toString());

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("req", jobj.toString()));

                StringEntity se = new StringEntity(jobj.toString());
                //se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse=httpClient.execute(httpPost);



                result = TextHelper.GetText(httpResponse).toString();
                //return result;
                result=result.substring(0,result.length()-150);//-150
                    //resultString = Regex.Match(subjectString, @"\d+").Sampler.Value;
                Log.e("response", "response -----" + result);



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    static class TextHelper {
        public static String GetText(InputStream in) {
            String text = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                text = sb.toString();
            } catch (Exception ex) {
                Log.e("sdfs","dsfslkdjf nhi hua");
            } finally {
                try {

                    in.close();
                } catch (Exception ex) {
                }
            }
            return text;
        }

        public static String GetText(HttpResponse response) {
            String text = "";
            try {
                text = GetText(response.getEntity().getContent());
            } catch (Exception ex) {
            }
            return text;
        }
    }

}











