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
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class CadastroAdminActivity extends AppCompatActivity implements Urls {

    EditText nome, descricao, responsavel, logo,
            endereco, numero, bairro, complemento, tel1, tel2, email, site;
    ImageButton cadastrar;
    String idEstado = "";
    ArrayList<String> cidadesNome = new ArrayList<>();
    AutoCompleteTextView txtEstado, txtCidade;
    RadioGroup radio;
    Switch pendente;
    Spinner spnSegs;
    Integer segmento_id;
    ArrayList<String> segmentosNome = new ArrayList<>();
    ArrayList<Segmento> segmentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        spnSegs = (Spinner) findViewById(R.id.spnSegs);

        HashMap<String, String> postData = new HashMap<>();
        postData.put("android", "android");
        PostResponseAsyncTask readSegmentos = new PostResponseAsyncTask(CadastroAdminActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //System.out.println(s);
                if (s == null || s.equals("")) {
                    Toast.makeText(CadastroAdminActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    segmentos = new JsonConverter<Segmento>().toArrayList(s, Segmento.class);

                    for (Segmento seg : segmentos)
                        segmentosNome.add(seg.nome);

                    segmentosNome.add(0, "Selecione um segmento");

                    ArrayAdapter<String> segmentosAdapter =
                            new ArrayAdapter<>(CadastroAdminActivity.this, android.R.layout.simple_spinner_item, segmentosNome);

                    segmentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnSegs.setAdapter(segmentosAdapter);

                    spnSegs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            segmento_id = spnSegs.getSelectedItemPosition();
                            if (segmento_id > 0)
                                segmento_id = segmentos.get(segmento_id - 1).id;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });
        readSegmentos.execute(listaSegmentosUrl);

        txtEstado = (AutoCompleteTextView) findViewById(R.id.txtEstado);
        txtCidade = (AutoCompleteTextView) findViewById(R.id.txtCidade);
        nome = (EditText) findViewById(R.id.txtEmpresa);
        descricao = (EditText) findViewById(R.id.txtPresentation);
        responsavel = (EditText) findViewById(R.id.txtResponsavel);
        endereco = (EditText) findViewById(R.id.txtAddress);
        numero = (EditText) findViewById(R.id.txtNum);
        bairro = (EditText) findViewById(R.id.txtBairro);
        complemento = (EditText) findViewById(R.id.txtComplemento);
        tel1 = (EditText) findViewById(R.id.txtTel1);
        tel2 = (EditText) findViewById(R.id.txtTel2);
        email = (EditText) findViewById(R.id.txtEmail);
        site = (EditText) findViewById(R.id.txtSite);
        cadastrar = (ImageButton) findViewById(R.id.btnEnviar);
        radio = (RadioGroup) findViewById(R.id.rgTipoCadastro);
        //pendente = (Switch) findViewById(R.id.btnPendente);

        MaskEditTextChangedListener maskTEL = new MaskEditTextChangedListener("(##)#########", tel1);
        tel1.addTextChangedListener(maskTEL);
        MaskEditTextChangedListener maskTEL2 = new MaskEditTextChangedListener("(##)#########", tel2);
        tel2.addTextChangedListener(maskTEL2);

        ImageView btnHome = (ImageView) findViewById(R.id.toolbar_logo);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

        final PostResponseAsyncTask readEstados = new PostResponseAsyncTask(CadastroAdminActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(CadastroAdminActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<Estado> estados = new JsonConverter<Estado>().toArrayList(s, Estado.class);
                    String estadosNome[] = new String[27];
                    int i = 0;
                    for (Estado e : estados) {
                        estadosNome[i] = e.nome;
                        i++;
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(CadastroAdminActivity.this, android.R.layout.simple_list_item_1, estadosNome);
                    txtEstado.setAdapter(adapter);
                }
            }
        });
        readEstados.execute(listaEstadosUrl);

        txtEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object e = parent.getItemAtPosition(position);
                idEstado = setId(e.toString());

                HashMap<String, String> postData = new HashMap<>();
                postData.put("id", idEstado);
                postData.put("android", "android");

                PostResponseAsyncTask readCidades = new PostResponseAsyncTask(view.getContext(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        //System.out.println(s);
                        if (s == null || s.equals("")) {
                            Toast.makeText(CadastroAdminActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<Cidade> cidades = new JsonConverter<Cidade>().toArrayList(s, Cidade.class);

                            for (Cidade c : cidades)
                                cidadesNome.add(c.nome);

                            ArrayAdapter<String> cidadesAdapter =
                                    new ArrayAdapter<>(CadastroAdminActivity.this, android.R.layout.simple_list_item_1, cidadesNome);
                            txtCidade.setAdapter(cidadesAdapter);
                        }
                    }
                });
                readCidades.execute(listaCidadesEmEstadoUrl);
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {
                    String telefone1 = tel1.getText().toString();
                    String telefone2 = tel2.getText().toString();

                    telefone1 = trataFonePut(telefone1);

                    if (!telefone2.equals("")) {
                        telefone2 = trataFonePut(telefone2);
                    }

                    HashMap<String, String> postCadastro = new HashMap<>();
                    postCadastro.put("android", "android");
                    postCadastro.put("segmento_id", segmento_id.toString());
                    postCadastro.put("nome", nome.getText().toString());
                    postCadastro.put("apresentacao", descricao.getText().toString());
                    postCadastro.put("nomeResponsavel", responsavel.getText().toString());
                    postCadastro.put("estado_id", idEstado);
                    postCadastro.put("cidade", txtCidade.getText().toString());
                    postCadastro.put("endereco", endereco.getText().toString());
                    postCadastro.put("numero", numero.getText().toString());
                    postCadastro.put("bairro", bairro.getText().toString());
                    postCadastro.put("complemento", complemento.getText().toString());
                    postCadastro.put("telefone1", telefone1);
                    postCadastro.put("telefone2", telefone2);
                    postCadastro.put("email", email.getText().toString());
                    postCadastro.put("site", site.getText().toString());
                    switch (radio.getCheckedRadioButtonId()){
                        case R.id.radio_gratis:
                            postCadastro.put("tipoCadastro", "0");
                            break;
                        case R.id.radio_1:
                            postCadastro.put("tipoCadastro", "1");
                            break;
                        case R.id.radio_2:
                            postCadastro.put("tipoCadastro", "2");
                            break;
                        case R.id.radio_3:
                            postCadastro.put("tipoCadastro", "3");
                            break;
                    }
                    /*
                    if (pendente.isChecked())
                        postCadastro.put("pendente", "1");
                    else
                        postCadastro.put("pendente", "0");
                    */

                    if (!exists(txtCidade.getText().toString(), cidadesNome))
                        postCadastro.put("isDiferente", "true");

                    PostResponseAsyncTask cadastro = new PostResponseAsyncTask(v.getContext(), postCadastro, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.equals("sucesso")) {
                                Intent intent = new Intent(CadastroAdminActivity.this, AdminActivity.class);
                                startActivity(intent);
                                Toast.makeText(CadastroAdminActivity.this, "Empresa cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                            } else if (s == null || s.equals("")) {
                                Toast.makeText(CadastroAdminActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(CadastroAdminActivity.this, "Algum erro ocorreu, tente novamente!", Toast.LENGTH_LONG).show();
                          }
                    });
                    cadastro.execute(cadastroEmpresaUrl);
                } else {
                    Toast.makeText(CadastroAdminActivity.this, "Existem campos sem preencher!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pendentes) {
            Intent intent = new Intent(this, ListaPendentesActivity.class);
            startActivity(intent);
        } else if (id == R.id.pendentesPremium) {
            Intent intent = new Intent(this, ListaPendentesPremiumActivity.class);
            startActivity(intent);
        } else if (id == R.id.empresas) {
            Intent intent = new Intent(this, ListaEmpresasAdminActivity.class);
            startActivity(intent);
        } else if (id == R.id.estatisticas) {
            Intent intent = new Intent(this, EstatisticasActivity.class);
            startActivity(intent);
        } else if (id == R.id.cadastrar) {
            return true;
        } else if (id == R.id.sair) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(CadastroAdminActivity.this, LoginAdminActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    public String setId(String s){
        switch (s){
            case "Minas Gerais":
                return "1";
            case "São Paulo":
                return "2";
            case "Rio de Janeiro":
                return "3";
            case "Espírito Santo":
                return "4";
            case "Distrito Federal":
                return "5";
            case "Rio Grande do Sul":
                return "6";
            case "Santa Catarina":
                return "7";
            case "Paraná":
                return "8";
            case "Mato Grosso":
                return "9";
            case "Mato Grosso do Sul":
                return "10";
            case "Tocantins":
                return "11";
            case "Goiás":
                return "12";
            case "Amazonas":
                return "13";
            case "Pará":
                return "14";
            case "Amapá":
                return "15";
            case "Acre":
                return "16";
            case "Roraima":
                return "17";
            case "Rondônia":
                return "18";
            case "Maranhão":
                return "19";
            case "Ceará":
                return "20";
            case "Rio Grande do Norte":
                return "21";
            case "Bahia":
                return "22";
            case "Piauí":
                return "23";
            case "Alagoas":
                return "24";
            case "Pernambuco":
                return "25";
            case "Sergipe":
                return "26";
            case "Paraíba":
                return "27";
        }
        return "-1";
    }

    public String trataFonePut(String tel1) {
        if (!tel1.equals("") && tel1 != null) {
            switch (tel1.length()) {
                case 12:
                    tel1 = tel1.replace("(","");
                    tel1 = tel1.replace(")"," ");
                    String fisrt = tel1.substring(0, 7);
                    String second = tel1.substring(7, tel1.length());
                    return fisrt + '-' + second;
                case 13:
                    tel1 = tel1.replace("(","");
                    tel1 = tel1.replace(")"," ");
                    fisrt = tel1.substring(0, 8);
                    second = tel1.substring(8, tel1.length());
                    return fisrt + '-' + second;
            }
        }
        return "";
    }

    public boolean exists(String s, ArrayList cidades){
        return cidades.contains(s);
    }

    public boolean isEmpty(){
        return (txtEstado.getResources().toString().isEmpty() ||
                txtCidade.getResources().toString().isEmpty() ||
                nome.getResources().toString().isEmpty() ||
                spnSegs.getSelectedItem().equals("Selecione um segmento"));
    }
}
