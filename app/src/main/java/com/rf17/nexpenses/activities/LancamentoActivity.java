package com.rf17.nexpenses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.dao.LancamentoDao;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.StringUtils;
import com.rf17.nexpenses.utils.UtilsApp;
import com.rf17.nexpenses.utils.UtilsUI;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LancamentoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Lancamento lancamento;

    private LancamentoDao lancamentoDao = new LancamentoDao(this);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private EditText editText_valor, editText_data, editText_descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lancamento);
            AppPreferences appPreferences = NexpensesApplication.getAppPreferences();

            RelativeLayout toolbar_valor  = (RelativeLayout) findViewById(R.id.toolbar_valor);
            editText_valor = (EditText) findViewById(R.id.txt_valor);
            editText_data = (EditText) findViewById(R.id.txt_data);
            editText_descricao = (EditText) findViewById(R.id.txt_descricao);
            final FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            UtilsApp.setAppColor(getWindow(), toolbar);
            toolbar_valor.setBackgroundColor(appPreferences.getPrimaryColorPref());// Header

            Integer id = getIntent().getIntExtra("id", 0);
            if (id != 0) {//Editar
                lancamentoDao.open();
                lancamento = lancamentoDao.getById(id);//Busca lancamento no banco de dados
                lancamentoDao.close();
            } else {//Novo
                lancamento = new Lancamento();
                lancamento.setTipo(getIntent().getStringExtra("tipo"));
            }

            if (getSupportActionBar() != null ) {
                getSupportActionBar().setTitle(lancamento.getTipo().equals("R") ? "Receita" : "Despesa");
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            //DatePicker
            editText_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                LancamentoActivity.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.show(getFragmentManager(), "Datepickerdialog");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //Salvar
            fab_save.setColorNormal(appPreferences.getAccentColorPref());
            fab_save.setColorPressed(UtilsUI.darker(appPreferences.getAccentColorPref(), 0.8));
            fab_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salvar();
                }
            });

            editText_valor.addTextChangedListener(new TextWatcher() {// ValueChange valor
                public void afterTextChanged(Editable s) { }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (StringUtils.formataVerificaValor(editText_valor.getText().toString()) > 0.01) {
                            fab_save.setVisibility(View.VISIBLE);
                        }else{
                            fab_save.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        //Nada
                    }
                }
            });

            lancamentoToForm(lancamento);

        } catch (Exception e) {
            e.printStackTrace();
            UtilsApp.showToast(LancamentoActivity.this, "Erro (Motivo: "+e.getMessage()+")");
        }
    }

    private void lancamentoToForm(Lancamento Lancamento) throws Exception {
        try{
            editText_valor.setText(Lancamento.getValor() == 0.0 ? "" : StringUtils.formataDouble(Lancamento.getValor(), 2));
            editText_data.setText(sdf.format(Lancamento.getData()));
            editText_descricao.setText(lancamento.getDescricao());
        } catch (Exception e) {
            throw new Exception("Erro ao preencher lanšamento (Motivo: "+e.getMessage()+")");
        }
    }

    private void salvar(){
        try{
            lancamento.setValor(StringUtils.formataVerificaValor(editText_valor.getText().toString()));
            lancamento.setData(sdf.parse(editText_data.getText().toString()));
            lancamento.setDescricao(editText_descricao.getText().toString());

            if (lancamento.getValor() < 0.01) {
                throw new Exception("Valor deve ser maior que 0,00 (zero)");
            } else {
                lancamentoDao.open();
                lancamentoDao.saveOrUpdate(lancamento);// Atualiza ou salva
                lancamentoDao.close();

                voltar();
            }
        } catch (Exception e) {
            UtilsApp.showToast(LancamentoActivity.this, "Erro ao salvar (Motivo: " + e.getMessage() + ")");
        }
    }

    // ## Action Bar ##
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lancamento, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332://Voltar
                voltar();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // ## Action Bar ##

    private void voltar(){
        Intent myIntent = new Intent(LancamentoActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    // ## Voltar ##
    @Override
    public void onBackPressed() {
        voltar();
    }
    // ## Voltar ##

    // ## DataPicker ##
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        editText_data.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
    }
    // ## DataPicker ##

}
