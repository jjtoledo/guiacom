package com.jose.guiacom;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class CadastroActivity extends AppCompatActivity implements Urls {

    Spinner spnSegs;
    EditText nome, descricao, responsavel,
            endereco, numero, bairro, complemento, tel1, tel2, email, site;
    TextInputLayout inputLayoutNome, inputLayoutDescricao, inputLayoutEstado,
            inputLayoutResponsavel, inputLayoutEndereco, inputLayoutNum, inputLayoutBairro,
            inputLayoutComplemento, inputLayoutTel1, inputLayoutTel2, inputLayoutEmail,
            inputLayoutSite, inputLayoutCidade;

    ImageButton cadastrar;
    String idEstado = "", voltar, cidadeSigla;
    ArrayList<String> cidadesNome = new ArrayList<>();
    ArrayList<String> segmentosNome = new ArrayList<>();
    ArrayList<Segmento> segmentos;
    AutoCompleteTextView txtEstado, txtCidade;
    CheckBox cbPremium;
    Integer segmento_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        spnSegs = (Spinner) findViewById(R.id.spnSegs);

        cidadeSigla = getIntent().getStringExtra("cidade");

        HashMap<String, String> postData = new HashMap<>();
        postData.put("android", "android");
        PostResponseAsyncTask readSegmentos = new PostResponseAsyncTask(CadastroActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //System.out.println(s);
                if (s == null || s.equals("")) {
                    Toast.makeText(CadastroActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    segmentos = new JsonConverter<Segmento>().toArrayList(s, Segmento.class);

                    for (Segmento seg : segmentos)
                        segmentosNome.add(seg.nome);

                    segmentosNome.add(0, "Selecione um segmento");

                    ArrayAdapter<String> segmentosAdapter =
                            new ArrayAdapter<>(CadastroActivity.this, android.R.layout.simple_spinner_item, segmentosNome);

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
        cbPremium = (CheckBox) findViewById(R.id.cbPremium);

        inputLayoutComplemento = (TextInputLayout) findViewById(R.id.input_layout_complemento);
        inputLayoutNome = (TextInputLayout) findViewById(R.id.input_layout_nome);
        inputLayoutDescricao = (TextInputLayout) findViewById(R.id.input_layout_descricao);
        inputLayoutResponsavel = (TextInputLayout) findViewById(R.id.input_layout_responsavel);
        inputLayoutEstado = (TextInputLayout) findViewById(R.id.input_layout_estado);
        inputLayoutCidade = (TextInputLayout) findViewById(R.id.input_layout_cidade);
        inputLayoutEndereco = (TextInputLayout) findViewById(R.id.input_layout_address);
        inputLayoutNum = (TextInputLayout) findViewById(R.id.input_layout_num);
        inputLayoutBairro = (TextInputLayout) findViewById(R.id.input_layout_bairro);
        inputLayoutTel1 = (TextInputLayout) findViewById(R.id.input_layout_tel1);
        inputLayoutTel2 = (TextInputLayout) findViewById(R.id.input_layout_tel2);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutSite = (TextInputLayout) findViewById(R.id.input_layout_site);

        nome.addTextChangedListener(new MyTextWatcher(nome));
        descricao.addTextChangedListener(new MyTextWatcher(descricao));
        responsavel.addTextChangedListener(new MyTextWatcher(responsavel));
        txtEstado.addTextChangedListener(new MyTextWatcher(txtEstado));
        txtCidade.addTextChangedListener(new MyTextWatcher(txtCidade));
        endereco.addTextChangedListener(new MyTextWatcher(endereco));
        numero.addTextChangedListener(new MyTextWatcher(numero));
        complemento.addTextChangedListener(new MyTextWatcher(complemento));
        bairro.addTextChangedListener(new MyTextWatcher(bairro));
        MaskEditTextChangedListener maskTEL = new MaskEditTextChangedListener("(##)#########", tel1);
        tel1.addTextChangedListener(maskTEL);
        tel1.addTextChangedListener(new MyTextWatcher(tel1));
        MaskEditTextChangedListener maskTEL2 = new MaskEditTextChangedListener("(##)#########", tel2);
        tel2.addTextChangedListener(maskTEL2);
        tel2.addTextChangedListener(new MyTextWatcher(tel2));
        email.addTextChangedListener(new MyTextWatcher(email));
        site.addTextChangedListener(new MyTextWatcher(site));

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
                Intent intent = new Intent(CadastroActivity.this, CatalogoActivity.class);
                if (cidadeSigla != null) {
                    intent.putExtra("cidade", cidadeSigla);
                }
                startActivity(intent);
            }
        });

        final PostResponseAsyncTask readEstados = new PostResponseAsyncTask(CadastroActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s == null || s.equals("")) {
                    Toast.makeText(CadastroActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<Estado> estados = new JsonConverter<Estado>().toArrayList(s, Estado.class);
                    String estadosNome[] = new String[27];
                    int i = 0;
                    for (Estado e : estados) {
                        estadosNome[i] = e.nome;
                        i++;
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(CadastroActivity.this, android.R.layout.simple_list_item_1, estadosNome);
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
                            Toast.makeText(CadastroActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<Cidade> cidades = new JsonConverter<Cidade>().toArrayList(s, Cidade.class);

                            for (Cidade c : cidades)
                                cidadesNome.add(c.nome);

                            ArrayAdapter<String> cidadesAdapter =
                                    new ArrayAdapter<>(CadastroActivity.this, android.R.layout.simple_list_item_1, cidadesNome);
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
                if (submitForm()){
                    String telefone1 = tel1.getText().toString();
                    String telefone2 = tel2.getText().toString();

                    telefone1 = trataFonePut(telefone1);

                    if (!telefone2.equals("")) {
                        telefone2 = trataFonePut(telefone2);
                    }

                    HashMap<String, String> postCadastro = new HashMap<>();
                    postCadastro.put("android", "android");
                    postCadastro.put("segmento_id", segmento_id.toString());
                    postCadastro.put("nome", setFirstUp(nome.getText().toString()));
                    postCadastro.put("apresentacao", descricao.getText().toString());
                    postCadastro.put("nomeResponsavel", responsavel.getText().toString());
                    postCadastro.put("estado_id", idEstado);
                    postCadastro.put("cidade", setFirstUp(txtCidade.getText().toString()));
                    postCadastro.put("endereco", endereco.getText().toString());
                    postCadastro.put("numero", numero.getText().toString());
                    postCadastro.put("bairro", bairro.getText().toString());
                    postCadastro.put("complemento", complemento.getText().toString());
                    postCadastro.put("telefone1", telefone1);
                    postCadastro.put("telefone2", telefone2);
                    postCadastro.put("email", email.getText().toString());
                    postCadastro.put("site", site.getText().toString());
                    if (cbPremium.isChecked())
                        postCadastro.put("premium", "1");
                    else
                        postCadastro.put("premium", "0");

                    if (!exists(txtCidade.getText().toString(), cidadesNome))
                        postCadastro.put("isDiferente", "true");

                    PostResponseAsyncTask cadastro = new PostResponseAsyncTask(v.getContext(), postCadastro, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.equals("sucesso")){
                                if (cidadeSigla != null) {
                                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                                    intent.putExtra("cidade", cidadeSigla);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                Toast.makeText(CadastroActivity.this, "Empresa cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                            } else if (s == null || s.equals("")) {
                                Toast.makeText(CadastroActivity.this, "Por favor, verifique sua conexão com a Internet", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(CadastroActivity.this, "Algum erro ocorreu, tente novamente!", Toast.LENGTH_LONG).show();
                                //System.out.println(s);
                            }
                        }
                    });
                    cadastro.execute(cadastroEmpresaUrl);
                } else {
                    Toast.makeText(CadastroActivity.this, "Existem campos sem preencher!", Toast.LENGTH_LONG).show();
                }
            }
        });
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
            if (cidadeSigla != null) {
                Intent intent = new Intent(CadastroActivity.this, SearchActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(CadastroActivity.this, "Por favor, selecione uma cidade", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.anuncie || id == R.id.cadastro) {
            return true;
        } else if (id == R.id.catalogo) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(CadastroActivity.this, CatalogoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CadastroActivity.this, CatalogoActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.admin) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(CadastroActivity.this, LoginAdminActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CadastroActivity.this, LoginAdminActivity.class);
                startActivity(intent);
            }
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.contato) {
            if (cidadeSigla != null) {
                Intent intent = new Intent(CadastroActivity.this, ContatoActivity.class);
                intent.putExtra("cidade", cidadeSigla);
                startActivity(intent);
            } else {
                Intent intent = new Intent(CadastroActivity.this, ContatoActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public String setFirstUp(String s) {
        String cidade[] = s.split(" ");
        String result = "";
        for (int i = 0; i < cidade.length; i++) {
            cidade[i] = cidade[i].toLowerCase();

            String aux = "";
            aux = "" + cidade[i].charAt(0);

            if (!cidade[i].equals("de") && !cidade[i].equals("da") && !cidade[i].equals("do"))
                aux = aux.toUpperCase();

            if (i == cidade.length-1)
                result = result + aux + cidade[i].substring(1);
            else
                result = result + aux + cidade[i].substring(1) + " ";
        }

        return result;
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

    private boolean submitForm() {
        return validateSeg() && validateName() &&
                validateResp() && validateEstado() && validateCidade() &&
                validateEndereco() && validateBairro() &&
                validateTel() && validateEmail() && validateSite();
    }

    private boolean validateName() {
        if (nome.getText().toString().trim().isEmpty()) {
            inputLayoutNome.setError(getString(R.string.err_msg_name));
            requestFocus(nome);
            return false;
        } else {
            inputLayoutNome.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSeg() {
        return !spnSegs.getSelectedItem().equals("Selecione um segmento");
    }

    private boolean validateResp() {
        if (responsavel.getText().toString().trim().isEmpty()) {
            inputLayoutResponsavel.setError(getString(R.string.err_msg_responsavel));
            requestFocus(responsavel);
            return false;
        } else {
            inputLayoutResponsavel.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEstado() {
        if (txtEstado.getText().toString().trim().isEmpty()) {
            inputLayoutEstado.setError(getString(R.string.err_msg_estado));
            requestFocus(txtEstado);
            return false;
        } else {
            inputLayoutEstado.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCidade() {
        if (txtEstado.getText().toString().trim().isEmpty()) {
            inputLayoutEstado.setError(getString(R.string.err_msg_estado));
            requestFocus(txtEstado);
            return false;
        } else if (txtCidade.getText().toString().trim().isEmpty()) {
            inputLayoutCidade.setError(getString(R.string.err_msg_cidade));
            requestFocus(txtCidade);
            return false;
        } else {
            inputLayoutCidade.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEndereco() {
        if (endereco.getText().toString().trim().isEmpty()) {
            inputLayoutEndereco.setError(getString(R.string.err_msg_endereco));
            requestFocus(endereco);
            return false;
        } else {
            inputLayoutEndereco.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateBairro() {
        if (bairro.getText().toString().trim().isEmpty()) {
            inputLayoutBairro.setError(getString(R.string.err_msg_bairro));
            requestFocus(bairro);
            return false;
        } else {
            inputLayoutBairro.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateTel() {
        if (tel1.getText().toString().trim().isEmpty()) {
            inputLayoutTel1.setError(getString(R.string.err_msg_tel1));
            requestFocus(tel1);
            return false;
        } else {
            inputLayoutTel1.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String e = email.getText().toString().trim();

        if (!e.equals("")){
            if (!isValidEmail(e)) {
                inputLayoutEmail.setError(getString(R.string.err_msg_email));
                requestFocus(email);
                return false;
            } else {
                inputLayoutEmail.setErrorEnabled(false);
            }
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSite() {
        String s = site.getText().toString().trim();

        if (!s.equals("")){
            if (!isValidSite(s)) {
                inputLayoutSite.setError(getString(R.string.err_msg_site));
                requestFocus(site);
                return false;
            } else {
                inputLayoutSite.setErrorEnabled(false);
            }
        } else {
            inputLayoutSite.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidSite(String site) {
        return !TextUtils.isEmpty(site) && Patterns.WEB_URL.matcher(site).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txtEmpresa:
                    validateName();
                    break;
                case R.id.txtPresentation:
                    validateName();
                    break;
                case R.id.txtResponsavel:
                    validateName();
                    validateResp();
                    break;
                case R.id.txtEstado:
                    validateResp();
                    validateName();
                    validateEstado();
                    break;
                case R.id.txtCidade:
                    validateResp();
                    validateName();
                    validateEstado();
                    validateCidade();
                    break;
                case R.id.txtAddress:
                    validateCidade();
                    validateEstado();
                    validateResp();
                    validateName();
                    validateEndereco();
                    break;
                case R.id.txtBairro:
                    validateEndereco();
                    validateCidade();
                    validateEstado();
                    validateResp();
                    validateName();
                    validateBairro();
                    break;
                case R.id.txtTel1:
                    validateBairro();
                    validateEndereco();
                    validateCidade();
                    validateEstado();
                    validateResp();
                    validateName();
                    validateTel();
                    break;
                case R.id.txtEmail:
                    validateTel();
                    validateBairro();
                    validateEndereco();
                    validateCidade();
                    validateEstado();
                    validateResp();
                    validateName();
                    validateEmail();
                    break;
                case R.id.txtSite:
                    validateTel();
                    validateBairro();
                    validateEndereco();
                    validateCidade();
                    validateEstado();
                    validateResp();
                    validateName();
                    validateSite();
                    break;
            }
        }
    }
}
