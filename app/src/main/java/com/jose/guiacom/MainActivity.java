package com.jose.guiacom;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Urls {

    ArrayList<String> cidadesNome = new ArrayList<>();
    ArrayList<Cidade> cidades;
    ArrayList<Empresa> empresas, premium1, premium2, premium3;
    ArrayList<Foto> fotos;
    String cidadeSigla, cidade;
    Spinner spinner;
    TextView txtCidade, txtAnunciantes, txtNenhum, txtCities, txtHistoria;
    RelativeLayout rlMain;
    ImageView[] ivArray;
    ImageView imgCity;
    View divisor, div;
    Integer[] tipo;

    int TAMANHO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TAMANHO = 51;
        tipo = new Integer[]{
                1, 1, 1, 2, 1, 1, 3, 1, 2, 1,
                1, 1, 1, 3, 1, 1, 2, 2, 1, 1,
                3, 1, 1, 2, 1, 1, 1, 2, 1, 1,
                3, 1, 1, 1, 1, 1, 2, 2, 1, 1,
                3, 2, 1, 1, 3, 1, 1, 1, 1, 1,
                2, 1, 1, 1, 2, 1, 1, 3, 1, 2,
                1, 1, 1, 1, 3, 1, 1, 2, 2, 1,
                1, 3, 1, 1, 2, 1, 1, 1, 2, 1,
                1, 3, 1, 1, 1, 1, 1, 2, 2, 1,
                1, 3, 2, 1, 1, 3, 1, 1, 1, 1,
                1, 2};

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

        txtCidade = (TextView) findViewById(R.id.txtCidade);
        txtAnunciantes = (TextView) findViewById(R.id.txtAnunciantes);
        txtNenhum = (TextView) findViewById(R.id.txtNenhum);
        txtCities = (TextView) findViewById(R.id.txtCities);
        txtHistoria = (TextView) findViewById(R.id.txtHistoria);
        imgCity = (ImageView) findViewById(R.id.imgCity);

        divisor = findViewById(R.id.Divisor2);
        div = findViewById(R.id.Divisor);
        final ViewGroup.LayoutParams params = divisor.getLayoutParams();
        final Float height = dpToPx(1);

        spinner = (Spinner) findViewById(R.id.spnCities);

        rlMain = (RelativeLayout) findViewById(R.id.rlMain);

        ivArray = new ImageView[TAMANHO];

        ivArray[0] = (ImageView) findViewById(R.id.imageView1);
        ivArray[1] = (ImageView) findViewById(R.id.imageView2);
        ivArray[2] = (ImageView) findViewById(R.id.imageView3);
        ivArray[3] = (ImageView) findViewById(R.id.imageView4);
        ivArray[4] = (ImageView) findViewById(R.id.imageView5);
        ivArray[5] = (ImageView) findViewById(R.id.imageView6);
        ivArray[6] = (ImageView) findViewById(R.id.imageView7);
        ivArray[7] = (ImageView) findViewById(R.id.imageView8);
        ivArray[8] = (ImageView) findViewById(R.id.imageView9);
        ivArray[9] = (ImageView) findViewById(R.id.imageView10);
        ivArray[10] = (ImageView) findViewById(R.id.imageView11);
        ivArray[11] = (ImageView) findViewById(R.id.imageView12);
        ivArray[12] = (ImageView) findViewById(R.id.imageView13);
        ivArray[13] = (ImageView) findViewById(R.id.imageView14);
        ivArray[14] = (ImageView) findViewById(R.id.imageView15);
        ivArray[15] = (ImageView) findViewById(R.id.imageView16);
        ivArray[16] = (ImageView) findViewById(R.id.imageView17);
        ivArray[17] = (ImageView) findViewById(R.id.imageView18);
        ivArray[18] = (ImageView) findViewById(R.id.imageView19);
        ivArray[19] = (ImageView) findViewById(R.id.imageView20);
        ivArray[20] = (ImageView) findViewById(R.id.imageView21);
        ivArray[21] = (ImageView) findViewById(R.id.imageView22);
        ivArray[22] = (ImageView) findViewById(R.id.imageView23);
        ivArray[23] = (ImageView) findViewById(R.id.imageView24);
        ivArray[24] = (ImageView) findViewById(R.id.imageView25);
        ivArray[25] = (ImageView) findViewById(R.id.imageView26);
        ivArray[26] = (ImageView) findViewById(R.id.imageView27);
        ivArray[27] = (ImageView) findViewById(R.id.imageView28);
        ivArray[28] = (ImageView) findViewById(R.id.imageView29);
        ivArray[29] = (ImageView) findViewById(R.id.imageView30);
        ivArray[30] = (ImageView) findViewById(R.id.imageView31);
        ivArray[31] = (ImageView) findViewById(R.id.imageView32);
        ivArray[32] = (ImageView) findViewById(R.id.imageView33);
        ivArray[33] = (ImageView) findViewById(R.id.imageView34);
        ivArray[34] = (ImageView) findViewById(R.id.imageView35);
        ivArray[35] = (ImageView) findViewById(R.id.imageView36);
        ivArray[36] = (ImageView) findViewById(R.id.imageView37);
        ivArray[37] = (ImageView) findViewById(R.id.imageView38);
        ivArray[38] = (ImageView) findViewById(R.id.imageView39);
        ivArray[39] = (ImageView) findViewById(R.id.imageView40);
        ivArray[40] = (ImageView) findViewById(R.id.imageView41);
        ivArray[41] = (ImageView) findViewById(R.id.imageView42);
        ivArray[42] = (ImageView) findViewById(R.id.imageView43);
        ivArray[43] = (ImageView) findViewById(R.id.imageView44);
        ivArray[44] = (ImageView) findViewById(R.id.imageView45);
        ivArray[45] = (ImageView) findViewById(R.id.imageView46);
        ivArray[46] = (ImageView) findViewById(R.id.imageView47);
        ivArray[47] = (ImageView) findViewById(R.id.imageView48);
        ivArray[48] = (ImageView) findViewById(R.id.imageView49);
        ivArray[49] = (ImageView) findViewById(R.id.imageView50);
        ivArray[50] = (ImageView) findViewById(R.id.imageView51);
//        ivArray[51] = (ImageView) findViewById(R.id.imageView52);
//        ivArray[52] = (ImageView) findViewById(R.id.imageView53);
//        ivArray[53] = (ImageView) findViewById(R.id.imageView54);
//        ivArray[54] = (ImageView) findViewById(R.id.imageView55);
//        ivArray[55] = (ImageView) findViewById(R.id.imageView56);
//        ivArray[56] = (ImageView) findViewById(R.id.imageView57);
//        ivArray[57] = (ImageView) findViewById(R.id.imageView58);
//        ivArray[58] = (ImageView) findViewById(R.id.imageView59);
//        ivArray[59] = (ImageView) findViewById(R.id.imageView60);
//        ivArray[60] = (ImageView) findViewById(R.id.imageView61);
//        ivArray[61] = (ImageView) findViewById(R.id.imageView62);
//        ivArray[62] = (ImageView) findViewById(R.id.imageView63);
//        ivArray[63] = (ImageView) findViewById(R.id.imageView64);
//        ivArray[64] = (ImageView) findViewById(R.id.imageView65);
//        ivArray[65] = (ImageView) findViewById(R.id.imageView66);
//        ivArray[66] = (ImageView) findViewById(R.id.imageView67);
//        ivArray[67] = (ImageView) findViewById(R.id.imageView68);
//        ivArray[68] = (ImageView) findViewById(R.id.imageView69);
//        ivArray[69] = (ImageView) findViewById(R.id.imageView70);
//        ivArray[70] = (ImageView) findViewById(R.id.imageView71);
//        ivArray[71] = (ImageView) findViewById(R.id.imageView72);
//        ivArray[72] = (ImageView) findViewById(R.id.imageView73);
//        ivArray[73] = (ImageView) findViewById(R.id.imageView74);
//        ivArray[74] = (ImageView) findViewById(R.id.imageView75);
//        ivArray[75] = (ImageView) findViewById(R.id.imageView76);
//        ivArray[76] = (ImageView) findViewById(R.id.imageView77);
//        ivArray[77] = (ImageView) findViewById(R.id.imageView78);
//        ivArray[78] = (ImageView) findViewById(R.id.imageView79);
//        ivArray[79] = (ImageView) findViewById(R.id.imageView80);
//        ivArray[80] = (ImageView) findViewById(R.id.imageView81);
//        ivArray[81] = (ImageView) findViewById(R.id.imageView82);
//        ivArray[82] = (ImageView) findViewById(R.id.imageView83);
//        ivArray[83] = (ImageView) findViewById(R.id.imageView84);
//        ivArray[84] = (ImageView) findViewById(R.id.imageView85);
//        ivArray[85] = (ImageView) findViewById(R.id.imageView86);
//        ivArray[86] = (ImageView) findViewById(R.id.imageView87);
//        ivArray[87] = (ImageView) findViewById(R.id.imageView88);
//        ivArray[88] = (ImageView) findViewById(R.id.imageView89);
//        ivArray[89] = (ImageView) findViewById(R.id.imageView90);
//        ivArray[90] = (ImageView) findViewById(R.id.imageView91);
//        ivArray[91] = (ImageView) findViewById(R.id.imageView92);
//        ivArray[92] = (ImageView) findViewById(R.id.imageView93);
//        ivArray[93] = (ImageView) findViewById(R.id.imageView94);
//        ivArray[94] = (ImageView) findViewById(R.id.imageView95);
//        ivArray[95] = (ImageView) findViewById(R.id.imageView96);
//        ivArray[96] = (ImageView) findViewById(R.id.imageView97);
//        ivArray[97] = (ImageView) findViewById(R.id.imageView98);
//        ivArray[98] = (ImageView) findViewById(R.id.imageView99);
//        ivArray[99] = (ImageView) findViewById(R.id.imageView100);
//        ivArray[100] = (ImageView) findViewById(R.id.imageView101);
//        ivArray[101] = (ImageView) findViewById(R.id.imageView102);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        final HashMap<String, String> postData = new HashMap<>();
        postData.put("android", "android");

        cidadeSigla = getIntent().getStringExtra("cidade");

        ImageView btnHome = (ImageView) findViewById(R.id.toolbar_logo);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
                if (!txtCidade.getText().equals("")) {
                    intent.putExtra("cidade", txtCidade.getText().toString());
                }
                startActivity(intent);
            }
        });

        final ImageButton btnBuscar = (ImageButton) findViewById(R.id.btn_buscar);

        if (cidadeSigla != null) {
            txtAnunciantes.setText(R.string.txtAnunciantes);
            cidade = cidadeSigla.substring(0, cidadeSigla.indexOf(" -"));

            params.height = height.intValue()+1;

            txtCidade.setText(cidadeSigla);

            final HashMap<String, String> postCidade = new HashMap<>();
            postCidade.put("cidade", cidade);
            postCidade.put("android", "android");

            PostResponseAsyncTask getCidade = new PostResponseAsyncTask(MainActivity.this, postCidade, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (!s.equals("nenhum")) {
                        //System.out.println(s);
                        final String cidade_id = s;

                        final HashMap<String, String> postCidade = new HashMap<>();
                        postCidade.put("cidade_id", cidade_id);
                        postCidade.put("android", "android");

                        PostResponseAsyncTask check = new PostResponseAsyncTask(MainActivity.this, postCidade, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if (s == null || s.equals("")) {
                                    Toast.makeText(MainActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                                } else if (s.equals("n")) {
                                    for (int i = 0; i < ivArray.length; i++) {
                                        ivArray[i].setImageResource(R.drawable.none);
                                        ivArray[i].setClickable(false);
                                    }

                                    spnMargin(0, 35, 0, 0);
                                    imgCity.setVisibility(View.GONE);
                                    txtHistoria.setVisibility(View.GONE);
                                    div.setVisibility(View.GONE);
                                    txtCities.setVisibility(View.VISIBLE);
                                    txtNenhum.setText(R.string.txtNenhum);

                                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                    intent.putExtra("cidade", txtCidade.getText().toString());
                                    startActivity(intent);
                                } else {
                                    PostResponseAsyncTask getFotos = new PostResponseAsyncTask(MainActivity.this, postCidade, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            if (!s.equals("null")) {
                                                txtCities.setVisibility(View.GONE);
                                                spnMargin(0, 0, 0, 0);
                                                imgCity.setVisibility(View.VISIBLE);
                                                txtHistoria.setVisibility(View.VISIBLE);
                                                div.setVisibility(View.VISIBLE);

                                                fotos = new JsonConverter<Foto>().toArrayList(s, Foto.class);

                                                int d = width(width, 4);
                                                Picasso.with(MainActivity.this)
                                                        .load(img + fotos.get(0).foto)
                                                        .transform(new CircleTransform())
                                                        .centerCrop()
                                                        .resize(d, d)
                                                        .placeholder(R.drawable.place)
                                                        .into(imgCity);

                                                imgCity.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(MainActivity.this, DetalhesCidadeActivity.class);
                                                        intent.putExtra("cidade", txtCidade.getText().toString());
                                                        intent.putExtra("fotos", fotos);
                                                        startActivity(intent);
                                                    }
                                                });

                                                txtHistoria.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(MainActivity.this, DetalhesCidadeActivity.class);
                                                        intent.putExtra("cidade", txtCidade.getText().toString());
                                                        intent.putExtra("fotos", fotos);
                                                        startActivity(intent);
                                                    }
                                                });
                                            } else {
                                                spnMargin(0, 35, 0, 0);
                                                imgCity.setVisibility(View.GONE);
                                                txtHistoria.setVisibility(View.GONE);
                                                div.setVisibility(View.GONE);
                                                txtCities.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                    getFotos.execute(getFotosUrl);
                                }
                            }
                        });
                        check.execute(checkCidadePossuiEmpresaPremiumUrl);
                    }
                }
            });
            getCidade.execute(getCidadeUrl);

            PostResponseAsyncTask readCidades = new PostResponseAsyncTask(MainActivity.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    //System.out.println(s);
                    if (s == null || s.equals("")) {
                        Toast.makeText(MainActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        cidadesNome.add(0, "Clique aqui e selecione uma cidade");

                        ArrayAdapter<String> cidadesAdapter =
                                new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout, cidadesNome);

                        cidadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(cidadesAdapter);
                    } else {
                        cidades = new JsonConverter<Cidade>().toArrayList(s, Cidade.class);

                        for (Cidade c : cidades)
                            cidadesNome.add(c.nome + " - " + getSigla(c.estado_id));

                        cidadesNome.remove(cidadeSigla);
                        cidadesNome.add(0, cidadeSigla);

                        ArrayAdapter<String> cidadesAdapter =
                                new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout, cidadesNome);

                        cidadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(cidadesAdapter);

                        final HashMap<String, String> postCidade = new HashMap<>();
                        postCidade.put("cidade", cidade);
                        postCidade.put("android", "android");

                        PostResponseAsyncTask getCidade = new PostResponseAsyncTask(MainActivity.this, postCidade, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if (!s.equals("nenhum")) {
                                    //System.out.println(s);
                                    final String cidade_id = s;
                                    HashMap<String, String> postId = new HashMap<>();
                                    postId.put("id", s);
                                    postId.put("android", "android");
                                    PostResponseAsyncTask readEmpresas = new PostResponseAsyncTask(MainActivity.this, postId, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            //System.out.println(s);
                                            if (!s.equals("nenhum")) {

                                                txtNenhum.setText("");
                                                empresas = new JsonConverter<Empresa>().toArrayList(s, Empresa.class);
                                                Collections.shuffle(empresas);

                                                premium1 = new ArrayList<>();
                                                premium2 = new ArrayList<>();
                                                premium3 = new ArrayList<>();

                                                boolean t = false;
                                                for (Empresa e : empresas) {
                                                    switch (e.tipoCadastro) {
                                                        case 1:
                                                            premium1.add(e);
                                                            break;
                                                        case 2:
                                                            premium2.add(e);
                                                            t = true;
                                                            break;
                                                        case 3:
                                                            premium3.add(e);
                                                            t = true;
                                                            break;
                                                    }
                                                }

                                                if (!t) {
                                                    for (int i = 0; i < TAMANHO; i++) {
                                                        tipo[i] = 1;
                                                    }

                                                    TAMANHO = 30;

                                                    setViews();
                                                }

                                                final int[] x = {0, 0, 0};
                                                final int[] id = new int[TAMANHO];

                                                for (int i = 0; i < TAMANHO; i++) {
                                                    String logo;
                                                    int d;
                                                    switch (tipo[i]) {
                                                        case 1:
                                                            if (x[0] == premium1.size()) {
                                                                x[0] = 0;
                                                                Collections.shuffle(premium1);
                                                            }
                                                            logo = img + premium1.get(x[0]).logo;
                                                            d = width(width, tipo[i]);
                                                            id[i] = premium1.get(x[0]).id;
                                                            Picasso.with(MainActivity.this)
                                                                    .load(logo)
                                                                    .resize(d, d)
                                                                    .into(ivArray[i]);
                                                            x[0]++;
                                                            break;
                                                        case 2:
                                                            if (x[1] == premium2.size()) {
                                                                x[1] = 0;
                                                                Collections.shuffle(premium2);
                                                            }
                                                            logo = img + premium2.get(x[1]).logo;
                                                            d = width(width, tipo[i]);
                                                            id[i] = premium2.get(x[1]).id;
                                                            Picasso.with(MainActivity.this)
                                                                    .load(logo)
                                                                    .resize(d, d)
                                                                    .into(ivArray[i]);
                                                            x[1]++;
                                                            break;
                                                        case 3:
                                                            if (premium3.size() > 0) {
                                                                if (x[2] == premium3.size()) {
                                                                    x[2] = 0;
                                                                    Collections.shuffle(premium3);
                                                                }
                                                                logo = img + premium3.get(x[2]).logo;
                                                                d = width(width, tipo[i]);
                                                                id[i] = premium3.get(x[2]).id;
                                                                Picasso.with(MainActivity.this)
                                                                        .load(logo)
                                                                        .resize(d, d)
                                                                        .into(ivArray[i]);
                                                                x[2]++;
                                                            }
                                                            break;
                                                    }
                                                }

                                                setClicks(id);

                                                btnBuscar.setVisibility(View.VISIBLE);
                                                btnBuscar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                                        intent.putExtra("cidade", txtCidade.getText().toString());
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    readEmpresas.execute(listaEmpresasPremiumEmCidadeUrl);
                                } else
                                    System.out.println("Erro");
                            }
                        });
                        getCidade.execute(getCidadeUrl);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object e = parent.getItemAtPosition(position);
                                if (!e.equals("Clique aqui e selecione uma cidade") && !e.equals(txtCidade.getText())) {
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.putExtra("cidade", e.toString());
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                txtCidade.setText("");
                                for (int i = 0; i < ivArray.length; i++) {
                                    ivArray[i].setImageResource(R.drawable.none);
                                    ivArray[i].setClickable(false);
                                }
                            }
                        });
                    }
                }
            });
            readCidades.execute(listaCidadesPossuemEmpresasUrl);
        } else {
            PostResponseAsyncTask readCidades = new PostResponseAsyncTask(MainActivity.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    //System.out.println(s);
                    if (s == null || s.equals("")) {
                        Toast.makeText(MainActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        cidadesNome.add(0, "Clique aqui e selecione uma cidade");

                        ArrayAdapter<String> cidadesAdapter =
                                new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout, cidadesNome);

                        cidadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(cidadesAdapter);
                    } else {
                        cidades = new JsonConverter<Cidade>().toArrayList(s, Cidade.class);

                        for (Cidade c : cidades)
                            cidadesNome.add(c.nome + " - " + getSigla(c.estado_id));

                        cidadesNome.add(0, "Clique aqui e selecione uma cidade");

                        ArrayAdapter<String> cidadesAdapter =
                                new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout, cidadesNome);

                        cidadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(cidadesAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object e = parent.getItemAtPosition(position);
                                if (!e.equals("Clique aqui e selecione uma cidade")) {
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.putExtra("cidade", e.toString());
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                txtCidade.setText("");
                                for (int i = 0; i < ivArray.length; i++) {
                                    ivArray[i].setImageResource(R.drawable.none);
                                    ivArray[i].setClickable(false);
                                }
                            }
                        });
                    }
                }
            });
            readCidades.execute(listaCidadesPossuemEmpresasUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pesquisar) {
            if (!txtCidade.getText().equals("")) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("cidade", txtCidade.getText().toString());
                startActivity(intent);
            } else
                Toast.makeText(MainActivity.this, "Por favor, selecione uma cidade", Toast.LENGTH_LONG).show();
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            Intent intent = new Intent(this, CadastroActivity.class);
            if (!txtCidade.getText().equals("")) {
                intent.putExtra("cidade", txtCidade.getText().toString());
            }
            startActivity(intent);
        } else if (id == R.id.catalogo) {
            Intent intent = new Intent(this, CatalogoActivity.class);
            if (!txtCidade.getText().equals("")) {
                intent.putExtra("cidade", txtCidade.getText().toString());
            }
            startActivity(intent);
        } else if (id == R.id.admin) {
            Intent intent = new Intent(this, LoginAdminActivity.class);
            if (!txtCidade.getText().equals("")) {
                intent.putExtra("cidade", txtCidade.getText().toString());
            }
            startActivity(intent);
        } else if (id == R.id.contato) {
            Intent intent = new Intent(this, ContatoActivity.class);
            if (!txtCidade.getText().equals("")) {
                intent.putExtra("cidade", txtCidade.getText().toString());
            }
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public String getSigla(String id) {
        switch (id){
            case "1":
                return "MG";
            case "2":
                return "SP";
            case "3":
                return "RJ";
            case "4":
                return "ES";
            case "5":
                return "DF";
            case "6":
                return "RS";
            case "7":
                return "SC";
            case "8":
                return "PR";
            case "9":
                return "MT";
            case "10":
                return "MS";
            case "11":
                return "TO";
            case "12":
                return "GO";
            case "13":
                return "AM";
            case "14":
                return "PA";
            case "15":
                return "AP";
            case "16":
                return "AC";
            case "17":
                return "RR";
            case "18":
                return "RO";
            case "19":
                return "MA";
            case "20":
                return "CE";
            case "21":
                return "RN";
            case "22":
                return "BA";
            case "23":
                return "PI";
            case "24":
                return "AL";
            case "25":
                return "PE";
            case "26":
                return "SE";
            case "27":
                return "PB";
        }
        return "-1";
    }

    public Empresa getEmpresa(int id, ArrayList<Empresa> empresas){
        for (Empresa e: empresas) {
            if (e.id == id)
                return e;
        }
        return null;
    }

    public int width(int width, int tipo){
        Double result;
        switch (tipo){
            case 1:
                result = (width-(width*converte(width, 32))-(width*converte(width, 10)))*0.3333;
                return result.intValue()+1;
            case 2:
                result = (width-(width*converte(width, 32)))*0.6667;
                return result.intValue();
            case 3:
                result = width-(width*converte(width, 32));
                return result.intValue()+1;
            case 4:
                result = (width-(width*converte(width, 32))-(width*converte(width, 10)))*0.20;
                return result.intValue()+1;
        }
        return 0;
    }

    public float dpToPx(int dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public double converte(int width, int dp) {
        return dpToPx(dp)/width;
    }

    public void spnMargin(float left, float top, float right, float bottom) {
        left = dpToPx((int)left);
        top = dpToPx((int)top);
        right = dpToPx((int)right);
        bottom = dpToPx((int)bottom);

        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        llp.setMargins((int)left, (int)top, (int)right, (int)bottom); // llp.setMargins(left, top, right, bottom);
        spinner.setLayoutParams(llp);
    }

    public void setViews() {
        for (int i = 30; i < ivArray.length; i++) {
            ivArray[i].setImageResource(R.drawable.none);
            ivArray[i].setClickable(false);
        }

        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) ivArray[5].getLayoutParams();
        p.addRule(RelativeLayout.ALIGN_LEFT, 0);
        p.addRule(RelativeLayout.ALIGN_START, 0);
        p.addRule(RelativeLayout.BELOW, 0);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView4);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView5);
        p.addRule(RelativeLayout.END_OF, R.id.imageView5);
        p.setMargins((int)dpToPx(5),0,0,0);
        ivArray[5].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[6].getLayoutParams();
        p.setMargins(0,(int)dpToPx(5),(int)dpToPx(5),0);
        ivArray[6].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[7].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, R.id.imageView6);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView7);
        p.addRule(RelativeLayout.END_OF, R.id.imageView7);
        ivArray[7].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[9].getLayoutParams();
        p.setMargins(0,(int)dpToPx(5),(int)dpToPx(5),0);
        ivArray[9].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[10].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, R.id.imageView8);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView10);
        p.addRule(RelativeLayout.END_OF, R.id.imageView10);
        p.setMargins(0,(int)dpToPx(5),0,0);
        ivArray[10].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[11].getLayoutParams();
        p.setMargins(0,0,0,0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd(0);
        }
        ivArray[11].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[12].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, R.id.imageView10);
        p.addRule(RelativeLayout.RIGHT_OF, 0);
        p.addRule(RelativeLayout.END_OF, 0);
        p.addRule(RelativeLayout.ALIGN_TOP, 0);
        p.setMargins(0,(int)dpToPx(5),(int)dpToPx(5),0);
        ivArray[12].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[13].getLayoutParams();
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView13);
        p.addRule(RelativeLayout.END_OF, R.id.imageView13);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView13);
        ivArray[13].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[14].getLayoutParams();
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView14);
        p.addRule(RelativeLayout.END_OF, R.id.imageView14);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView14);
        p.setMargins((int)dpToPx(5),0,0,0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd(0);
            p.setMarginStart((int)dpToPx(5));
        }
        ivArray[14].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[16].getLayoutParams();
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView16);
        p.addRule(RelativeLayout.END_OF, R.id.imageView16);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView16);
        p.setMargins((int)dpToPx(5),0,(int)dpToPx(5),0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd((int)dpToPx(5));
            p.setMarginStart((int)dpToPx(5));
        }
        ivArray[16].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[17].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, 0);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView17);
        p.addRule(RelativeLayout.END_OF, R.id.imageView17);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView17);
        p.setMargins(0,0,0,0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd(0);
        }
        ivArray[17].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[18].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, R.id.imageView18);
        p.addRule(RelativeLayout.RIGHT_OF, 0);
        p.addRule(RelativeLayout.END_OF, 0);
        p.addRule(RelativeLayout.ALIGN_TOP, 0);
        p.setMargins(0,(int)dpToPx(5),(int)dpToPx(5),0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd((int)dpToPx(5));
        }
        ivArray[18].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[19].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, 0);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView19);
        p.addRule(RelativeLayout.END_OF, R.id.imageView19);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView19);
        p.addRule(RelativeLayout.ALIGN_BOTTOM, 0);
        p.setMargins(0,0,(int)dpToPx(5),0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd((int)dpToPx(5));
        }
        ivArray[19].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[20].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, 0);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView20);
        p.addRule(RelativeLayout.END_OF, R.id.imageView20);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView20);
        p.setMargins(0,0,0,0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd(0);
        }
        ivArray[20].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[21].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, R.id.imageView19);
        p.addRule(RelativeLayout.RIGHT_OF, 0);
        p.addRule(RelativeLayout.END_OF, 0);
        p.addRule(RelativeLayout.ALIGN_TOP, 0);
        p.setMargins(0,(int)dpToPx(5),(int)dpToPx(5),0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd((int)dpToPx(5));
        }
        ivArray[21].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[22].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, 0);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView22);
        p.addRule(RelativeLayout.END_OF, R.id.imageView22);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView22);
        p.setMargins(0,0,(int)dpToPx(5),0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd((int)dpToPx(5));
        }
        ivArray[22].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[23].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, 0);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView23);
        p.addRule(RelativeLayout.END_OF, R.id.imageView23);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView23);
        p.setMargins(0,0,0,0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd(0);
        }
        ivArray[23].setLayoutParams(p);

        p = (RelativeLayout.LayoutParams) ivArray[29].getLayoutParams();
        p.addRule(RelativeLayout.BELOW, 0);
        p.addRule(RelativeLayout.RIGHT_OF, R.id.imageView29);
        p.addRule(RelativeLayout.END_OF, R.id.imageView29);
        p.addRule(RelativeLayout.ALIGN_TOP, R.id.imageView29);
        p.setMargins((int)dpToPx(5),0,0,0);
        if (Build.VERSION.SDK_INT > 16) {
            p.setMarginEnd(0);
            p.setMarginStart((int)dpToPx(5));
        }
        ivArray[29].setLayoutParams(p);
    }

    public void setClicks(final int[] ids) {
        for (int i = 0; i < ivArray.length; i++) {
            final int x = i;
            ivArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Empresa e = getEmpresa(ids[x], empresas);
                    Log.i("teste", x+"");
                    Intent intent = new Intent(MainActivity.this, DetalhesPremiumActivity.class);
                    intent.putExtra("empresa", e);
                    intent.putExtra("cidade", txtCidade.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }
}
