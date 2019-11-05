package br.com.alura.leilao.ui.activity;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.api.retrofit.client.RespostaListener;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.ui.recyclerview.adapter.ListaLeilaoAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class ListaLeilaoActivityTest {

    @Mock
    Context context;

    @Spy
    private ListaLeilaoAdapter adapter = new ListaLeilaoAdapter(context);

    @Mock
    private LeilaoWebClient client;

    @Test
    public void must_updateLeilaoList_WhenCallLeilaoApi() throws InterruptedException {
        ListaLeilaoActivity activity = new ListaLeilaoActivity();
        doNothing().when(adapter).atualizaLista();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                RespostaListener<List<Leilao>> arguments = invocation.getArgument(0);
                arguments.sucesso(new ArrayList<>(Arrays.asList(
                        new Leilao("Teste"),
                        new Leilao("Teste 2")
                )));
                return null;
            }
        }).when(client).todos(any(RespostaListener.class));

        activity.configuraAdapter();
        activity.buscaLeiloes(adapter, client);
        Thread.sleep(2000);
        int quantidadeLeiloes = adapter.getItemCount();

        assertThat(quantidadeLeiloes, is(2));

    }
}