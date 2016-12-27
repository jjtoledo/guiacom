package com.jose.guiacom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class EditarActivity extends AppCompatActivity implements Urls {

    EditText nome, descricao, responsavel, logo,
            endereco, numero, bairro, tel1, tel2, email, site;
    Integer id;
    Button salvar;
    String idEstado = "", estadoNome = "";
    ArrayList<String> cidadesNome = new ArrayList<>();
    AutoCompleteTextView txtEstado, txtCidade;
    RadioGroup radio;
    Switch pendente;
    Spinner spnSegs;
    ArrayList<Segmento> segmentos;
    ArrayList<String> segmentosNome = new ArrayList<>();
    Integer segmento_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        final Empresa empresa = (Empresa) getIntent().getSerializableExtra("empresa");

        spnSegs = (Spinner) findViewById(R.id.spnSegs);

        HashMap<String, String> postData = new HashMap<>();
        postData.put("android", "android");
        PostResponseAsyncTask readSegmentos = new PostResponseAsyncTask(EditarActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //System.out.println(s);
                if (s == null || s.equals("")) {
                    Toast.makeText(EditarActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    segmentos = new JsonConverter<Segmento>().toArrayList(s, Segmento.class);

                    for (Segmento seg : segmentos)
                        segmentosNome.add(seg.nome);

                    segmentosNome.remove(empresa.segmento_nome);
                    segmentosNome.add(0, empresa.segmento_nome);

                    ArrayAdapter<String> segmentosAdapter =
                            new ArrayAdapter<>(EditarActivity.this, android.R.layout.simple_spinner_item, segmentosNome);

                    segmentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnSegs.setAdapter(segmentosAdapter);

                    spnSegs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            segmento_id = spnSegs.getSelectedItemPosition();
                            segmento_id = segmentos.get(segmento_id).id;
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
        tel1 = (EditText) findViewById(R.id.txtTel1);
        tel2 = (EditText) findViewById(R.id.txtTel2);
        email = (EditText) findViewById(R.id.txtEmail);
        site = (EditText) findViewById(R.id.txtSite);
        salvar = (Button) findViewById(R.id.btnSalvar);
        radio = (RadioGroup) findViewById(R.id.rgTipoCadastro);
        //pendente = (Switch) findViewById(R.id.btnPendente);

        MaskEditTextChangedListener maskTEL = new MaskEditTextChangedListener("(##)#########", tel1);
        tel1.addTextChangedListener(maskTEL);
        MaskEditTextChangedListener maskTEL2 = new MaskEditTextChangedListener("(##)#########", tel2);
        tel2.addTextChangedListener(maskTEL2);

        if (empresa != null) {
            id = empresa.id;
            nome.setText(empresa.nome);
            descricao.setText(empresa.apresentacao);
            responsavel.setText(empresa.nomeResponsavel);
            endereco.setText(empresa.endereco);
            numero.setText(String.valueOf(empresa.numero));
            bairro.setText(empresa.bairro);
            tel1.setText(trataFoneGet(empresa.telefone1));
            tel2.setText(trataFoneGet(empresa.telefone2));
            email.setText(empresa.email);
            site.setText(empresa.site);
            txtCidade.setText(empresa.cidade_nome);

            switch (empresa.tipoCadastro){
                case 0:
                    radio.check(R.id.radio_gratis);
                    break;
                case 1:
                    radio.check(R.id.radio_1);
                    break;
                case 2:
                    radio.check(R.id.radio_2);
                    break;
                case 3:
                    radio.check(R.id.radio_3);
                    break;
            }

            /*
            switch (empresa.pendente){
                case 0:
                    pendente.setChecked(false);
                    break;
                case 1:
                    pendente.setChecked(true);
                    break;
            }*/

            final HashMap<String, String> postId = new HashMap<>();
            postId.put("cidade_id", empresa.cidade_id.toString());
            postId.put("android", "android");
            final PostResponseAsyncTask getEstado = new PostResponseAsyncTask(EditarActivity.this, postId, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s == null || s.equals("")) {
                        Toast.makeText(EditarActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayList<Estado> estados = new JsonConverter<Estado>().toArrayList(s, Estado.class);
                        txtEstado.setText(estados.get(0).nome);
                        estadoNome = txtEstado.getText().toString();
                    }
                }
            });
            getEstado.execute(getEstadoUrl);
        }

        final PostResponseAsyncTask readEstados = new PostResponseAsyncTask(EditarActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(EditarActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<Estado> estados = new JsonConverter<Estado>().toArrayList(s, Estado.class);
                    String estadosNome[] = new String[27];
                    int i = 0;
                    for (Estado e : estados) {
                        estadosNome[i] = e.nome;
                        i++;
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(EditarActivity.this, android.R.layout.simple_list_item_1, estadosNome);
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

                if (!e.toString().equals(estadoNome)) {
                    txtCidade.setText("");
                }

                HashMap<String, String> postData = new HashMap<>();
                postData.put("id", idEstado);
                postData.put("android", "android");

                PostResponseAsyncTask readCidades = new PostResponseAsyncTask(view.getContext(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s == null || s.equals("")) {
                            Toast.makeText(EditarActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<Cidade> cidades = new JsonConverter<Cidade>().toArrayList(s, Cidade.class);

                            for (Cidade c : cidades)
                                cidadesNome.add(c.nome);

                            ArrayAdapter<String> cidadesAdapter =
                                    new ArrayAdapter<>(EditarActivity.this, android.R.layout.simple_list_item_1, cidadesNome);
                            txtCidade.setAdapter(cidadesAdapter);
                        }
                    }
                });
                readCidades.execute(listaCidadesEmEstadoUrl);
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()){
                    String telefone1 = tel1.getText().toString();
                    String telefone2 = tel2.getText().toString();

                    telefone1 = trataFonePut(telefone1);

                    if (!telefone2.equals("")) {
                        telefone2 = trataFonePut(telefone2);
                    }

                    HashMap<String, String> postUpdate = new HashMap<>();
                    postUpdate.put("android", "android");
                    postUpdate.put("id", id.toString());
                    postUpdate.put("segmento_id", segmento_id.toString());
                    postUpdate.put("nome", nome.getText().toString());
                    postUpdate.put("apresentacao", descricao.getText().toString());
                    postUpdate.put("nomeResponsavel", responsavel.getText().toString());
                    postUpdate.put("estado_id", idEstado);
                    postUpdate.put("cidade", txtCidade.getText().toString());
                    postUpdate.put("endereco", endereco.getText().toString());
                    postUpdate.put("numero", numero.getText().toString());
                    postUpdate.put("bairro", bairro.getText().toString());
                    postUpdate.put("telefone1", telefone1);
                    postUpdate.put("telefone2", telefone2);
                    postUpdate.put("email", email.getText().toString());
                    postUpdate.put("site", site.getText().toString());
                    switch (radio.getCheckedRadioButtonId()){
                        case R.id.radio_gratis:
                            postUpdate.put("tipoCadastro", "0");
                            break;
                        case R.id.radio_1:
                            postUpdate.put("tipoCadastro", "1");
                            break;
                        case R.id.radio_2:
                            postUpdate.put("tipoCadastro", "2");
                            break;
                        case R.id.radio_3:
                            postUpdate.put("tipoCadastro", "3");
                            break;
                    }
                    /*
                    if (pendente.isChecked())
                        postUpdate.put("pendente", "1");
                    else
                        postUpdate.put("pendente", "0");
                    */

                    if (!exists(txtCidade.getText().toString(), cidadesNome))
                        postUpdate.put("isDiferente", "true");

                    PostResponseAsyncTask update = new PostResponseAsyncTask(v.getContext(), postUpdate, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            //System.out.println(s);
                            if (s.equals("sucesso")) {
                                Intent intent = new Intent(EditarActivity.this, AdminActivity.class);
                                startActivity(intent);
                                Toast.makeText(EditarActivity.this, "Empresa atualizada com sucesso!", Toast.LENGTH_LONG).show();
                            } else if (s == null || s.equals("")) {
                                Toast.makeText(EditarActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(EditarActivity.this, "Algum erro ocorreu, tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    });
                    update.execute(updateEmpresaUrl);
                } else
                    Toast.makeText(EditarActivity.this, "Existem campos em branco!", Toast.LENGTH_LONG).show();
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
            return true;
        } else if (id == R.id.empresas) {
            return true;
        } else if (id == R.id.estatisticas) {
            return true;
        } else if (id == R.id.sair) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(EditarActivity.this, LoginAdminActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
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

    public String trataFoneGet(String tel1) {
        if (!tel1.equals("") && tel1 != null) {
            tel1 = tel1.replace("-", "");
            tel1 = tel1.replace(" ", "");
            return tel1;
        }
        return "";
    }

    public boolean exists(String s, ArrayList cidades){
        return cidades.contains(s);
    }

    public boolean isEmpty(){
        return (txtEstado.getResources().toString().isEmpty() ||
                txtCidade.getResources().toString().isEmpty() ||
                nome.getResources().toString().isEmpty());
    }
}
