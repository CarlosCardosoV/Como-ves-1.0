package com.eduardoapps.comoves;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.system.Os;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Revistas extends Fragment {


    public Revistas() {
        // Required empty public constructor
    }

    private ProgressDialog mProgressDialog;

    /**
     * Ponemos los parametros para la autorización de la API
     **/

    private static final String KEY = "X-AUTHORIZATION";
    private static final String VALUE = "Bc3f9995e0321b7fe8a8de50318a392aadda6a42";
    /**
     * ----------------
     **/

    /**
     *Agregamos las variables de las categorias
     ***/

    JSONObject jsonObject;
    List<String> nombre = new ArrayList<>(),
            keywords = new ArrayList<>(),
            covers = new ArrayList<>(); //En estás listas guardarmos las diferentes categorias

    public static List<Revista> revistaNatura = new ArrayList<>(), revistaRobots = new ArrayList<>(), revistaAnimal = new ArrayList<>();


    private RecyclerView natura;
    private RecyclerView animal;
    private RecyclerView robots;
    String url = "https://mipeiper.com/api/v1/categories/1/";

    String respuesta;

    private AdaptadorRevistas adaptadorAnimal, adaptadorNatura, adaptadorRobots;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revistas, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        natura = (RecyclerView) getView().findViewById(R.id.natura);
        animal = (RecyclerView) getView().findViewById(R.id.animal);
        robots = (RecyclerView) getView().findViewById(R.id.robots);
        natura.setHasFixedSize(true);
        animal.setHasFixedSize(true);
        robots.setHasFixedSize(true);
        adaptadorAnimal = new AdaptadorRevistas(this, revistaAnimal);
        adaptadorNatura = new AdaptadorRevistas(this, revistaNatura);
        adaptadorRobots = new AdaptadorRevistas(this, revistaRobots);
        natura.setAdapter(adaptadorNatura);
        animal.setAdapter(adaptadorAnimal);
        robots.setAdapter(adaptadorRobots);
        natura.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        animal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        robots.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        for (int i = 0; i < 11; i++) {

            Revista r = new Revista();

            r.setTitulo("Revista " + String.valueOf(i));
            r.setPortada(R.drawable.portada);
            revistaAnimal.add(r);
            Log.d("Agregando " + r.getTitulo(), String.valueOf(revistaAnimal.size()));
            revistaNatura.add(r);
            Log.d("Agregando " + r.getTitulo(), String.valueOf(revistaNatura.size()));
            revistaRobots.add(r);
            Log.d("Agregando " + r.getTitulo(), String.valueOf(revistaRobots.size()));
            adaptadorAnimal.notifyDataSetChanged();
            adaptadorNatura.notifyDataSetChanged();
            adaptadorRobots.notifyDataSetChanged();







            /*JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });

        }*/

        }

        new ConectarBackGround().execute(url, KEY, VALUE);
    }


    private class ConectarBackGround extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                HttpURLConnection connection = null;
                String json = "";
                URL url = new URL("https://mipeiper.com/api/v1/categories/1");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET"); // Aquí ven que se especifica el tipo de petición al servidor.
                // Agrega el header con llave-valor
                connection.addRequestProperty(KEY, VALUE);  // Aquí van las llaves para poder acceder.
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {    // Si la conexión se logró
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    // La linea de arriba prepara el buffer de datos para recibir lo que envia el servidor
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {    //Proceso de lectura de datos y los agrega a line.
                        sb.append(line);
                    }
                    br.close();
                    json = sb.toString();

                } else {
                    Log.d("Conexion: ", connection.getResponseMessage());
                }

                connection.disconnect();

                return json;      // Contiene el json que devuelve el servidor.

            } catch (Exception e) {
                Log.e(MainActivity.class.toString(), e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideProgressDialog();

            Log.d("Respuesta JSON", s);

            try {

                jsonObject = new JSONObject(s);  //CONVERTIRMOS LA CADENA OBTENIDA, A UN JSON OBJECT
                Log.d("JSON Object", jsonObject.toString());

            } catch (Throwable t) {

                Log.e("Revistas", "Could not parse malformed JSON: \"" + s + "\"");

            }

                        try{
                            JSONArray categoria = jsonObject.getJSONArray("data");
                            for(int i = 0; i<categoria.length(); i++){
                                JSONObject categories = categoria.getJSONObject(i);
                                String name = categories.getString("nombre");
                                String key = categories.getString("keywords");
                                String cvrs = categories.getString("covers");

                                nombre.add(name);
                                Log.d("Agregando: "+name, String.valueOf(nombre.size()));
                                keywords.add(key);
                                Log.d("Agregando: "+key, String.valueOf(keywords.size()));
                                covers.add(cvrs);
                                Log.d("Agregando: "+cvrs, String.valueOf(covers.size()));

                            }
                        }catch(JSONException e){
                            Log.e("ERROR JSON", e.getMessage());
                        }

        }
    }


    


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("cargando");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }


}
