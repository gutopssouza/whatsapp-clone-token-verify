package br.com.brick.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import br.com.brick.whatsapp.R;
import br.com.brick.whatsapp.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button btValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidacao = findViewById(R.id.txtCodigoId);
        btValidar = findViewById(R.id.btValidarId);

        SimpleMaskFormatter simpleMaskCodigo = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskTextWatcherCodigo = new MaskTextWatcher(codigoValidacao,simpleMaskCodigo);

        codigoValidacao.addTextChangedListener(maskTextWatcherCodigo);

        btValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recuperar dados das preferencias do Usuário
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get("token");
                String tokenDigitado = codigoValidacao.getText().toString();

                if(tokenDigitado.equals(tokenGerado)){
                    Toast.makeText(ValidadorActivity.this,"Token VALIDADO!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ValidadorActivity.this,"Token INVÁLIDO!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
