package br.com.alura.leilao.ui;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.api.retrofit.client.RespostaListener;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.ui.activity.ListaLeilaoActivity;
import br.com.alura.leilao.ui.recyclerview.adapter.ListaLeilaoAdapter;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AtualizadorDeLeiloesTest {

    @Mock
    private Context context;

    @Mock
    private ListaLeilaoAdapter adapter;

    @Mock
    private LeilaoWebClient client;

    @Mock
    private AtualizadorDeLeiloes.ErroCarregaLeiloesListener listener;

    @Test
    public void must_updateLeilaoList_WhenCallLeilaoApi() throws InterruptedException {
        AtualizadorDeLeiloes atualizadorDeLeiloes = new AtualizadorDeLeiloes();
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

        atualizadorDeLeiloes.buscaLeiloes(adapter, client, listener);

        verify(client).todos(any(RespostaListener.class));
        verify(adapter).atualiza(new ArrayList<>(Arrays.asList(
                new Leilao("Teste"),
                new Leilao("Teste 2")
        )));
    }

    @Test
    public void must_PresentFailMessage_WhenSearchFail() {
        AtualizadorDeLeiloes atualizadorDeLeiloes = new AtualizadorDeLeiloes();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                RespostaListener<List<Leilao>> arguments = invocation.getArgument(0);
                arguments.falha(anyString());
                return null;
            }
        }).when(client).todos(any(RespostaListener.class));

        atualizadorDeLeiloes.buscaLeiloes(adapter, client, listener);

        verify(listener).erroAoCarregar(anyString());
    }

}