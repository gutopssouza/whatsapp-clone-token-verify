package br.com.brick.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import br.com.brick.whatsapp.R;
import br.com.brick.whatsapp.helper.Permissao;
import br.com.brick.whatsapp.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private Button btCadastrar;
    private EditText nome;
    private EditText telefone;
    private EditText ddd;
    private EditText codPais;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1,this, permissoesNecessarias);

        btCadastrar = findViewById(R.id.btCadastrarId);
        nome = findViewById(R.id.txtNomeId);
        telefone = findViewById(R.id.txtTelefoneId);
        ddd = findViewById(R.id.txtDDDId);
        codPais = findViewById(R.id.txtCodPaisId);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone,simpleMaskTelefone);
        telefone.addTextChangedListener(maskTelefone);

        SimpleMaskFormatter simpleMaskTelefoneDDD = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskTelefoneDDD = new MaskTextWatcher(ddd,simpleMaskTelefoneDDD);
        ddd.addTextChangedListener(maskTelefoneDDD);

        SimpleMaskFormatter simpleMaskTelefoneCodPais = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskTelefoneCodPais = new MaskTextWatcher(codPais,simpleMaskTelefoneCodPais);
        codPais.addTextChangedListener(maskTelefoneCodPais);



        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                        codPais.getText().toString() +
                        ddd.getText().toString() +
                        telefone.getText().toString();

                String telefoneSemFormatacao = telefoneCompleto.replace("+","");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-","");

                //gerar token
                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt((9999 - 1000) + 1000);
                String token = String.valueOf(numeroRandomico);
                String mensagem = "WhatsApp clone token de confirmação: " + token;

                //salvar dados para validacao
                Preferencias preferencias = new Preferencias(getApplicationContext());
                preferencias.salvarUsuariosPreferencias(nomeUsuario,telefoneSemFormatacao,token);

                //Envio de SMS
                telefoneSemFormatacao = "5554";//numero do emulador
                boolean enviadoSMS = enviaSMS("+" + telefoneSemFormatacao, mensagem);

                if(enviadoSMS){

                    Intent intent = new Intent(LoginActivity.this, ValidadorActivity.class);
                    startActivity(intent);
                    finish();
                    
                }else{
                    Toast.makeText(LoginActivity.this,"Problema ao enviar SMS, tente novamente!", Toast.LENGTH_LONG).show();
                }


//                HashMap<String,String> usuario = preferencias.getDadosUsuario();
//                Log.i("TOKEN","T: " + usuario.get("token"));

            }
        });
    }

    //Envio de SMS
    private boolean enviaSMS(String telefone, String msg){

        try{

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone,null,msg,null,null);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int resultado : grantResults){
            if(resultado== PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app é necessário aceitar as permissões!");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
