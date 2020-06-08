package br.com.alura.alurafood.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import br.com.alura.alurafood.R;
import br.com.alura.alurafood.formatter.FormataTelefoneComDdd;
import br.com.alura.alurafood.validator.ValidaCpf;
import br.com.alura.alurafood.validator.ValidaTelefoneComDdd;
import br.com.alura.alurafood.validator.ValidacaoPadrao;
import br.com.caelum.stella.format.CPFFormatter;

public class FormularioCadastroActivity extends AppCompatActivity {

    private static final String ERRO_FORMATACAO_CPF = "erro formatacao cpf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro);
        inicializaCampos();
    }

    private void inicializaCampos() {
        configuraCampoNomeCompleto();
        configuraCampoCpf();
        configuraCampoTelefoneComDdd();
        configuraCampoEmail();
        configuraCampoSenha();
    }

    private void configuraCampoSenha() {
        TextInputLayout textInputSenha = findViewById(R.id.formulario_cadastro_campo_senha);
        adicionaValidacaoPadrao(textInputSenha);
    }

    private void configuraCampoEmail() {
        TextInputLayout textInputEmail = findViewById(R.id.formulario_cadastro_campo_email);
        adicionaValidacaoPadrao(textInputEmail);
    }

    private void configuraCampoTelefoneComDdd() {
        TextInputLayout textInputTelefoneComDdd = findViewById(R.id.formulario_cadastro_campo_telefone_com_ddd);
        EditText campoTelefoneComDdd = textInputTelefoneComDdd.getEditText();
        ValidaTelefoneComDdd validador = new ValidaTelefoneComDdd(textInputTelefoneComDdd);
        final FormataTelefoneComDdd formatador = new FormataTelefoneComDdd();
        campoTelefoneComDdd.setOnFocusChangeListener((v, hasFocus) -> {
            String telefoneComDdd = campoTelefoneComDdd.getText().toString();
            if (hasFocus) {
                String telefoneComDddSemFormatacao = formatador.remove(telefoneComDdd);
                campoTelefoneComDdd.setText(telefoneComDddSemFormatacao);
            } else {
                validador.estaValido();
            }
        });
    }

    private void configuraCampoCpf() {
        TextInputLayout textInputCpf = findViewById(R.id.formulario_cadastro_campo_cpf);
        final EditText campoCpf = textInputCpf.getEditText();
        final CPFFormatter formatador = new CPFFormatter();
        ValidaCpf validador = new ValidaCpf(textInputCpf);
        campoCpf.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                removeFormatacao(formatador, campoCpf);
            } else {
                validador.estaValido();
            }
        });
    }

    private void removeFormatacao(CPFFormatter formatador, EditText campoCpf) {
        String cpf = campoCpf.getText().toString();
        try {
            String cpfSemFormato = formatador.unformat(cpf);
            campoCpf.setText(cpfSemFormato);
        } catch (IllegalArgumentException e) {
            Log.e(ERRO_FORMATACAO_CPF, e.getMessage());
        }
    }

    private void configuraCampoNomeCompleto() {
        TextInputLayout textInputNomeCompleto = findViewById(R.id.formulario_cadastro_campo_nome_completo);
        adicionaValidacaoPadrao(textInputNomeCompleto);
    }

    private void adicionaValidacaoPadrao(final TextInputLayout textInputCampo) {
        final EditText campo = textInputCampo.getEditText();
        ValidacaoPadrao validador = new ValidacaoPadrao(textInputCampo);
        campo.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus) {
                validador.estaValido();
            }
        });
    }
}