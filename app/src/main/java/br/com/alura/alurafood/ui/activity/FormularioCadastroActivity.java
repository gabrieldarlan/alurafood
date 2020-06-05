package br.com.alura.alurafood.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import br.com.alura.alurafood.R;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class FormularioCadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro);
        setTitle("Formulário chavoso");

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
        adicionaValidacaoPadrao(textInputTelefoneComDdd);
    }

    private void configuraCampoCpf() {
        final TextInputLayout textInputCpf = findViewById(R.id.formulario_cadastro_campo_cpf);
        final EditText campoCpf = textInputCpf.getEditText();
        final CPFFormatter cpfFormatter = new CPFFormatter();
        campoCpf.setOnFocusChangeListener((v, hasFocus) -> {
            String cpf = campoCpf.getText().toString();
            if (!hasFocus) {
                if (!validaCampoObrigatorio(cpf, textInputCpf)) return;
                if (!validaCampoComOnzeDigitos(cpf, textInputCpf)) return;
                if (!validaCalculoCpf(textInputCpf, cpf)) return;
                removeErro(textInputCpf);

                String cpfFormatado = cpfFormatter.format(cpf);
                campoCpf.setText(cpfFormatado);
            } else {
                try {
                    String cpfSemFormato = cpfFormatter.unformat(cpf);
                    campoCpf.setText(cpfSemFormato);
                } catch (IllegalArgumentException e) {
                    Log.e("erro formatacao cpf", e.getMessage());
                }
            }
        });
    }

    private boolean validaCalculoCpf(TextInputLayout textInputCpf, String cpf) {
        try {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.assertValid(cpf);
        } catch (InvalidStateException e) {
            textInputCpf.setError("CPF inválido");
            return false;
        }
        return true;
    }

    private void removeErro(TextInputLayout textInput) {
        textInput.setError(null);
        textInput.setErrorEnabled(false);
    }

    private boolean validaCampoComOnzeDigitos(String cpf, TextInputLayout textInputCpf) {
        if (cpf.length() != 11) {
            textInputCpf.setError("O CPF precisa ter 11 dígitos");
            return false;
        }
        return true;
    }

    private void configuraCampoNomeCompleto() {
        TextInputLayout textInputNomeCompleto = findViewById(R.id.formulario_cadastro_campo_nome_completo);
        adicionaValidacaoPadrao(textInputNomeCompleto);
    }

    private void adicionaValidacaoPadrao(final TextInputLayout textInputCampo) {
        final EditText campo = textInputCampo.getEditText();
        campo.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            String texto = campo.getText().toString();
            if (!hasFocus) {
                if (!validaCampoObrigatorio(texto, textInputCampo)) return;
                removeErro(textInputCampo);
            }
        });
    }

    private boolean validaCampoObrigatorio(String texto, TextInputLayout textInputCampo) {
        if (texto.isEmpty()) {
            textInputCampo.setError("Campo obrigatório");
            return false;
        }
        return true;
    }

}