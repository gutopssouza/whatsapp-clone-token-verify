package br.com.brick.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String NOME_ARQUIVO = "whatsapp.preferencias";
    private int MODE = 0;//ARQUIVO DE PREFERENCIAS NAO COMPARTILHADO
    private SharedPreferences.Editor editor;
    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";

    public Preferencias(Context contextoParametro){

        context = contextoParametro;
        sharedPreferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = sharedPreferences.edit();
    }

    public void salvarUsuariosPreferencias(String nome, String telefone, String token){

        editor.putString(CHAVE_NOME,nome);
        editor.putString(CHAVE_TELEFONE,telefone);
        editor.putString(CHAVE_TOKEN,token);
        editor.commit();

    }

    public HashMap<String,String> getDadosUsuario(){

        HashMap<String,String> dadosUsuario = new HashMap<>();
        dadosUsuario.put(CHAVE_NOME,sharedPreferences.getString(CHAVE_NOME,null));
        dadosUsuario.put(CHAVE_TELEFONE,sharedPreferences.getString(CHAVE_TELEFONE,null));
        dadosUsuario.put(CHAVE_TOKEN,sharedPreferences.getString(CHAVE_TOKEN,null));

        return dadosUsuario;
    }
}
