package com.jose.guiacom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements Urls {

    TextView txtCidade;
    ArrayList<Empresa> empresas;
    ArrayList<Cidade> cidades;
    ArrayList<Foto> fotos;
    ArrayList items = new ArrayList();
    ArrayAdapter<String> cidadesNome;
    //EntryAdapter empresasAdapter;
    FunDapter<Empresa> empresasAdapter;
    ListView lvEmpresas;
    String id = "", cidade, cidadeSigla;
    ImageView imgCity;
    TextView txtHistoria;
    View div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

        div = findViewById(R.id.Divisor1);

        lvEmpresas = (ListView) findViewById(R.id.lvEmpresas);

        ImageView btnHome = (ImageView) findViewById(R.id.toolbar_logo);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CatalogoActivity.class);
                if (cidadeSigla != null) {
                    intent.putExtra("cidade", cidadeSigla);
                }
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

        final ImageButton btnTrocarCidade = (ImageButton) findViewById(R.id.btnTrocarCidade);
        btnTrocarCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> postData = new HashMap<>();
                postData.put("android", "android");

                PostResponseAsyncTask readCidades = new PostResponseAsyncTask(SearchActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        //System.out.println(s);
                        if (s == null || s.equals("")) {
                            Toast.makeText(SearchActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        } else {
                            cidades = new JsonConverter<Cidade>().toArrayList(s, Cidade.class);

                            cidadesNome = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1);

                            for (Cidade c : cidades)
                                cidadesNome.add(c.nome + " - " + getSigla(c.estado_id));

                            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                            builder.setTitle(R.string.dialog_troca_cidade_title)
                                    .setAdapter(cidadesNome, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, final int which) {
                                            final HashMap<String, String> postCidade = new HashMap<>();
                                            postCidade.put("cidade", cidadesNome.getItem(which).substring(0, cidadesNome.getItem(which).indexOf(" -")));
                                            postCidade.put("android", "android");
                                            Log.i("cidade", cidadesNome.getItem(which));

                                            PostResponseAsyncTask getCidade = new PostResponseAsyncTask(SearchActivity.this, postCidade, new AsyncResponse() {
                                                @Override
                                                public void processFinish(String s) {
                                                    if (s == null || s.equals("")) {
                                                        Toast.makeText(SearchActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        id = s;
                                                        final HashMap<String, String> postCidade = new HashMap<>();
                                                        postCidade.put("cidade_id", id);
                                                        postCidade.put("android", "android");

                                                        PostResponseAsyncTask check = new PostResponseAsyncTask(SearchActivity.this, postCidade, new AsyncResponse() {
                                                            @Override
                                                            public void processFinish(String s) {
                                                                if (s == null || s.equals("")) {
                                                                    Toast.makeText(SearchActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                                                                } else if (s.equals("s")) {
                                                                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                                                                    intent.putExtra("cidade", cidadesNome.getItem(which));
                                                                    startActivity(intent);
                                                                } else {
                                                                    Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                                                                    intent.putExtra("cidade", cidadesNome.getItem(which));
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                                        check.execute(checkCidadePossuiEmpresaPremiumUrl);
                                                    }
                                                }
                                            });
                                            getCidade.execute(getCidadeUrl);
                                        }
                                    });
                            builder.show();
                        }
                    }
                });
                readCidades.execute(listaCidadesPossuemEmpresasUrl);
            }
        });

        cidadeSigla = getIntent().getStringExtra("cidade");
        cidade = cidadeSigla.substring(0, cidadeSigla.indexOf(" -"));

        txtCidade = (TextView) findViewById(R.id.txtCidade);
        txtCidade.setText(cidadeSigla);

        txtHistoria = (TextView) findViewById(R.id.txtHistoria);
        imgCity = (ImageView) findViewById(R.id.imgCity);

        final EditText txtSearch = (EditText) findViewById(R.id.txtSearch);
        ViewGroup.LayoutParams params = txtSearch.getLayoutParams();
        params.width = width/2;

        ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);

        ImageButton btnSegmentos = (ImageButton) findViewById(R.id.btnSegmentos);
        btnSegmentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SegmentActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            }
        });

        final HashMap<String, String> postCidade = new HashMap<>();
        postCidade.put("cidade", cidade);
        postCidade.put("android", "android");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        txtSearch.getWindowToken(), 0);

                PostResponseAsyncTask getCidade = new PostResponseAsyncTask(SearchActivity.this, postCidade, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s == null || s.equals("")) {
                            Toast.makeText(SearchActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        } else {
                            id = s;

                            HashMap<String, String> postData = new HashMap<>();
                            postData.put("id_cidade", id);
                            postData.put("empresa", txtSearch.getText().toString());
                            postData.put("android", "android");

                            PostResponseAsyncTask readEmpresas = new PostResponseAsyncTask(SearchActivity.this, postData, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    //System.out.println(s);

                                    if (!s.equals("nenhum")) {
                                        /*
                                        empresas = new JsonConverter<Empresa>().toArrayList(s, Empresa.class);

                                        items = new ArrayList();
                                        for (int i = 0; i < empresas.size(); i++) {
                                            Empresa e = empresas.get(i);
                                            items.add(new EntryItem(e.nome, e.segmento_nome, e.telefone1));
                                        }

                                        addSection(empresas, items);

                                        empresasAdapter = new EntryAdapter(SearchActivity.this, items);
                                        */

                                        empresas = new JsonConverter<Empresa>().toArrayList(s, Empresa.class);

                                        BindDictionary<Empresa> dictEmpresas = new BindDictionary<>();
                                        dictEmpresas.addStringField(R.id.tvNome, new StringExtractor<Empresa>() {
                                            @Override
                                            public String getStringValue(Empresa empresa, int position) {
                                                return empresa.nome;
                                            }
                                        });

                                        dictEmpresas.addStringField(R.id.tvSegmento, new StringExtractor<Empresa>() {
                                            @Override
                                            public String getStringValue(Empresa empresa, int position) {
                                                if (empresa.segmento_nome.length() > 18)
                                                    return empresa.segmento_nome.substring(0,18) + "...";
                                                return empresa.segmento_nome;
                                            }
                                        });

                                        dictEmpresas.addStringField(R.id.tvTel, new StringExtractor<Empresa>() {
                                            @Override
                                            public String getStringValue(Empresa empresa, int position) {

                                                return empresa.telefone1;
                                            }
                                        });

                                        empresasAdapter = new FunDapter<>(
                                                SearchActivity.this, empresas, R.layout.layout_empresas_all, dictEmpresas);

                                        lvEmpresas.setAdapter(empresasAdapter);

                                        lvEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Empresa selected = empresas.get(position);
                                                if (selected.tipoCadastro == 0) {
                                                    Intent intent = new Intent(SearchActivity.this, DetalhesActivity.class);
                                                    intent.putExtra("empresa", selected);
                                                    intent.putExtra("cidade", cidadeSigla);
                                                    intent.putExtra("voltar", Search);
                                                    startActivity(intent);
                                                } else {
                                                    Intent intent = new Intent(SearchActivity.this, DetalhesPremiumActivity.class);
                                                    intent.putExtra("empresa", selected);
                                                    intent.putExtra("cidade", cidadeSigla);
                                                    intent.putExtra("voltar", Search);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    } else
                                        Toast.makeText(SearchActivity.this, "Nenhuma empresa foi encontrada...", Toast.LENGTH_LONG).show();
                                }
                            });
                            readEmpresas.execute(pesquisaEmpresasEmCidadeUrl);
                        }
                    }
                });
                getCidade.execute(getCidadeUrl);
            }
        });

        PostResponseAsyncTask getCidade = new PostResponseAsyncTask(SearchActivity.this, postCidade, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(SearchActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    if (!s.equals("nenhum")) {
                        //System.out.println(s);
                        id = s;

                        final HashMap<String, String> postCidade = new HashMap<>();
                        postCidade.put("cidade_id", id);
                        postCidade.put("android", "android");

                        PostResponseAsyncTask check = new PostResponseAsyncTask(SearchActivity.this, postCidade, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if (s == null || s.equals("")) {
                                    Toast.makeText(SearchActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                                } else if (s.equals("n")) {
                                    PostResponseAsyncTask getFotos = new PostResponseAsyncTask(SearchActivity.this, postCidade, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            if (!s.equals("null")) {
                                                imgCity.setVisibility(View.VISIBLE);
                                                txtHistoria.setVisibility(View.VISIBLE);
                                                div.setVisibility(View.VISIBLE);

                                                fotos = new JsonConverter<Foto>().toArrayList(s, Foto.class);

                                                int d = width(width, 4);
                                                Picasso.with(SearchActivity.this)
                                                        .load(img + fotos.get(0).foto)
                                                        .transform(new CircleTransform())
                                                        .centerCrop()
                                                        .placeholder(R.drawable.place)
                                                        .resize(d, d)
                                                        .into(imgCity);

                                                imgCity.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(SearchActivity.this, DetalhesCidadeActivity.class);
                                                        intent.putExtra("cidade", txtCidade.getText().toString());
                                                        intent.putExtra("fotos", fotos);
                                                        startActivity(intent);
                                                    }
                                                });

                                                txtHistoria.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(SearchActivity.this, DetalhesCidadeActivity.class);
                                                        intent.putExtra("cidade", txtCidade.getText().toString());
                                                        intent.putExtra("fotos", fotos);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    getFotos.execute(getFotosUrl);
                                } else {
                                    div.setVisibility(View.GONE);
                                }
                            }
                        });
                        check.execute(checkCidadePossuiEmpresaPremiumUrl);
                    }
                }
            }
        });
        getCidade.execute(getCidadeUrl);

        getCidade = new PostResponseAsyncTask(SearchActivity.this, postCidade, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(SearchActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    if (!s.equals("nenhum")) {
                        //System.out.println(s);
                        id = s;

                        HashMap<String, String> postId = new HashMap<>();
                        postId.put("id", id);
                        postId.put("android", "android");
                        PostResponseAsyncTask readEmpresas = new PostResponseAsyncTask(SearchActivity.this, postId, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                //System.out.println(s);
                                if (!s.equals("nenhum")) {
                                    empresas = new JsonConverter<Empresa>().toArrayList(s, Empresa.class);

                                    /*
                                    for (int i = 0; i < empresas.size(); i++) {
                                        Empresa e = empresas.get(i);
                                        items.add(new EntryItem(e.nome, e.segmento_nome, e.telefone1));
                                    }

                                    addSection(empresas, items);

                                    empresasAdapter = new EntryAdapter(SearchActivity.this, items);
                                    */

                                    BindDictionary<Empresa> dictEmpresas = new BindDictionary<>();
                                    dictEmpresas.addStringField(R.id.tvNome, new StringExtractor<Empresa>() {
                                        @Override
                                        public String getStringValue(Empresa empresa, int position) {
                                            return empresa.nome;
                                        }
                                    });

                                    dictEmpresas.addStringField(R.id.tvSegmento, new StringExtractor<Empresa>() {
                                        @Override
                                        public String getStringValue(Empresa empresa, int position) {
                                            if (empresa.segmento_nome.length() > 18)
                                                return empresa.segmento_nome.substring(0,18) + "...";
                                            return empresa.segmento_nome;
                                        }
                                    });

                                    dictEmpresas.addStringField(R.id.tvTel, new StringExtractor<Empresa>() {
                                        @Override
                                        public String getStringValue(Empresa empresa, int position) {

                                            return empresa.telefone1;
                                        }
                                    });

                                    empresasAdapter = new FunDapter<>(
                                            SearchActivity.this, empresas, R.layout.layout_empresas_all, dictEmpresas);

                                    lvEmpresas.setAdapter(empresasAdapter);

                                    lvEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Empresa selected = empresas.get(position);
                                            if (selected.tipoCadastro == 0) {
                                                Intent intent = new Intent(SearchActivity.this, DetalhesActivity.class);
                                                intent.putExtra("empresa", selected);
                                                intent.putExtra("cidade", cidadeSigla);
                                                intent.putExtra("voltar", Search);
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(SearchActivity.this, DetalhesPremiumActivity.class);
                                                intent.putExtra("empresa", selected);
                                                intent.putExtra("cidade", cidadeSigla);
                                                intent.putExtra("voltar", Search);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        readEmpresas.execute(listaEmpresasEmCidadeUrl);
                    } else
                        System.out.println("Erro");
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
            return true;
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(this, CadastroActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                intent.putExtra("voltar", Search);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, CadastroActivity.class);
                intent.putExtra("voltar", Search);
                startActivity(intent);
            }
        } else if (id == R.id.catalogo) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(this, CatalogoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                intent.putExtra("voltar", Search);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, CatalogoActivity.class);
                intent.putExtra("voltar", Search);
                startActivity(intent);
            }
        } else if (id == R.id.admin) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(this, LoginAdminActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                intent.putExtra("voltar", Search);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginAdminActivity.class);
                intent.putExtra("voltar", Search);
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

    public void addSection(ArrayList<Empresa> empresas, ArrayList items){
        int j=0;
        int section[] = new int[26];

        for (int i = 0; i < 26; i++) {
            section[i] = 0;
        }

        for (int i = 0; i < empresas.size(); i++) {
            Empresa e = empresas.get(i);
            String s = e.nome.substring(0,1);
            s = s.toUpperCase();

            if (s.equals("A") && section[0] == 0){
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[0] = 1;
            } else if (s.equals("B") && section[1] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[1] = 1;
            } else if (s.equals("C") && section[2] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[2] = 1;
            } else if (s.equals("D") && section[3] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[3] = 1;
            } else if (s.equals("E") && section[4] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[4] = 1;
            } else if (s.equals("F") && section[5] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[5] = 1;
            } else if (s.equals("G") && section[6] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[6] = 1;
            } else if (s.equals("H") && section[7] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[7] = 1;
            } else if (s.equals("I") && section[8] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[8] = 1;
            } else if (s.equals("J") && section[9] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[9] = 1;
            } else if (s.equals("K") && section[10] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[10] = 1;
            } else if (s.equals("L") && section[11] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[11] = 1;
            } else if (s.equals("M") && section[12] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[12] = 1;
            } else if (s.equals("N") && section[13] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[13] = 1;
            } else if (s.equals("O") && section[14] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[14] = 1;
            } else if (s.equals("P") && section[15] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[15] = 1;
            } else if (s.equals("Q") && section[16] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[16] = 1;
            } else if (s.equals("R") && section[17] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[17] = 1;
            } else if (s.equals("S") && section[18] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[18] = 1;
            } else if (s.equals("T") && section[19] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[19] = 1;
            } else if (s.equals("U") && section[20] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[20] = 1;
            } else if (s.equals("V") && section[21] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[21] = 1;
            } else if (s.equals("W") && section[22] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[22] = 1;
            } else if (s.equals("X") && section[23] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[23] = 1;
            } else if (s.equals("Y") && section[24] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[24] = 1;
            } else if (s.equals("Z") && section[25] == 0) {
                items.add(j, new SectionItem(s));
                e.tipoCadastro = 4;
                empresas.add(j, e);
                j++;
                section[25] = 1;
            } else
                j++;
        }
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

}
