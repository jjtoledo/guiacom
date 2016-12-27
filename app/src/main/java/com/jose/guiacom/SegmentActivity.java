package com.jose.guiacom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SegmentActivity extends AppCompatActivity implements Urls {

    String cidade, cidadeSigla, id;
    TextView txtCidade;
    ArrayList<Segmento> segmentos;
    FunDapter<Segmento> segmentoAdapter;
    ListView lvSegmentos;
    ArrayList<Cidade> cidades;
    ArrayAdapter<String> cidadesNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        lvSegmentos = (ListView) findViewById(R.id.lvSegmentos);

        cidadeSigla = getIntent().getStringExtra("cidade");
        cidade = cidadeSigla.substring(0, cidadeSigla.indexOf(" -"));

        txtCidade = (TextView) findViewById(R.id.txtCidade);
        txtCidade.setText(cidadeSigla);

        ImageButton btnEmpresas = (ImageButton) findViewById(R.id.btnEmpresas);
        btnEmpresas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            }
        });

        ImageButton btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView btnHome = (ImageView) findViewById(R.id.toolbar_logo);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SegmentActivity.this, CatalogoActivity.class);
                if (cidadeSigla != null) {
                    intent.putExtra("cidade", cidadeSigla);
                }
                startActivity(intent);
            }
        });

        final ImageButton btnTrocarCidade = (ImageButton) findViewById(R.id.btnTrocarCidade);
        btnTrocarCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> postData = new HashMap<>();
                postData.put("android", "android");

                PostResponseAsyncTask readCidades = new PostResponseAsyncTask(SegmentActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        //System.out.println(s);
                        if (s == null || s.equals("")) {
                            Toast.makeText(SegmentActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        } else {
                            cidades = new JsonConverter<Cidade>().toArrayList(s, Cidade.class);

                            cidadesNome = new ArrayAdapter<>(SegmentActivity.this, android.R.layout.simple_list_item_1);

                            for (Cidade c : cidades)
                                cidadesNome.add(c.nome + " - " + getSigla(c.estado_id));

                            AlertDialog.Builder builder = new AlertDialog.Builder(SegmentActivity.this);
                            builder.setTitle(R.string.dialog_troca_cidade_title)
                                    .setAdapter(cidadesNome, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(SegmentActivity.this, MainActivity.class);
                                            intent.putExtra("cidade", cidadesNome.getItem(which));
                                            startActivity(intent);
                                        }
                                    });
                            builder.show();
                        }
                    }
                });
                readCidades.execute(listaCidadesPossuemEmpresasUrl);
            }
        });

        HashMap<String, String> postCidade = new HashMap<>();
        postCidade.put("cidade", cidade);
        postCidade.put("android", "android");

        PostResponseAsyncTask getCidade = new PostResponseAsyncTask(SegmentActivity.this, postCidade, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(SegmentActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    id = s;
                    HashMap<String, String> postId = new HashMap<>();
                    postId.put("id", id);
                    postId.put("android", "android");

                    PostResponseAsyncTask readSegmentos = new PostResponseAsyncTask(SegmentActivity.this, postId, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            //System.out.println(s);

                            if (!s.equals("nenhum")) {
                                segmentos = new JsonConverter<Segmento>().toArrayList(s, Segmento.class);

                                BindDictionary<Segmento> dictSegmento = new BindDictionary<>();
                                dictSegmento.addStringField(R.id.tvNome, new StringExtractor<Segmento>() {
                                    @Override
                                    public String getStringValue(Segmento segmento, int position) {
                                        return segmento.nome;
                                    }
                                });

                                segmentoAdapter = new FunDapter<>(
                                        SegmentActivity.this, segmentos, R.layout.layout_segmentos, dictSegmento);

                                lvSegmentos.setAdapter(segmentoAdapter);

                                lvSegmentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Segmento selected = segmentos.get(position);
                                        Intent intent = new Intent(SegmentActivity.this, EmpresasActivity.class);
                                        intent.putExtra("segmento", selected.id.toString());
                                        intent.putExtra("segmento_nome", selected.nome);
                                        intent.putExtra("cidade", cidadeSigla);
                                        startActivity(intent);
                                    }
                                });
                            } else
                                Toast.makeText(SegmentActivity.this, "Nenhum segmento foi encontrado...", Toast.LENGTH_LONG).show();
                        }
                    });
                    readSegmentos.execute(listaSegmentosEmCidadeUrl);
                }
            }
        });
        getCidade.execute(getCidadeUrl);
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
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("cidade", cidadeSigla);
            startActivity(intent);
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, CadastroActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.catalogo) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(this, CatalogoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, CatalogoActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.admin) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(this, LoginAdminActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginAdminActivity.class);
                startActivity(intent);
            }
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.contato) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(this, ContatoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ContatoActivity.class);
                startActivity(intent);
            }
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
}
