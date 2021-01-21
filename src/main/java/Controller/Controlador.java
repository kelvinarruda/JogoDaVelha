package Controller;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logica_jogo.Arvore;

@WebServlet(name = "Controlador", urlPatterns = { "/Controlador" })
public class Controlador extends HttpServlet {

    private static final long serialVersionUID = 1L;
    int jogadorDaVez = 0;
    int primeiroJogador = 0;
    int[][] tabuleiro = new int[3][3];
    boolean inicio = true;
    int qtdeJogadas = 0;
    String jogador1 = "IA";
    String jogador2;
    int vencedor = 0;
    boolean emAndamento = true;
    int nivelJogo = 2;


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	int comecou;

	if (request.getParameter("comecou") != null)
	    comecou = Integer.valueOf(request.getParameter("comecou"));
	else
	    comecou = 0;

	if (this.inicio == true || comecou == 1) {

	    this.jogadorDaVez = sorteiaPrimeiroJogador();

	    this.inicio = false;
	    this.qtdeJogadas = 0;
	    this.emAndamento = true;

	    this.jogador2 = request.getParameter("jogador");
	    System.out.println("jogador da vez " + jogadorDaVez);
	    request.setAttribute("jogador", this.jogador2);
	    this.nivelJogo = Integer.parseInt(request.getParameter("nivel"));

	    inicializaTabuleiro();

	    if (this.jogadorDaVez == 1) {
		realizaJogadaComputador();
		this.qtdeJogadas++;
		this.jogadorDaVez = 2;
	    }

	} else {

	    System.out.println(" Jogada de numero " + (1 + this.qtdeJogadas) + "");

	    this.qtdeJogadas++;

	    if (jogadorDaVez == 1) {
		realizaJogadaComputador();
		this.qtdeJogadas++;
		this.jogadorDaVez = 2;
	    } else {
		realizaJogadaHumano(request);
	    }

	    testaVencedor(request);

	    request.setAttribute("jogador", this.jogador2);

	    if (this.jogadorDaVez == 2 && this.emAndamento) {
		realizaJogadaComputador();
		this.qtdeJogadas++;
		testaVencedor(request);
	    }
	}

	if (!this.emAndamento) {
	    request.setAttribute("tabuleiro", this.tabuleiro);
	    request.setAttribute("jogadorDaVez", -1);
	    request.getRequestDispatcher("tabuleiro.jsp").forward(request, response);
	    reiniciaTabuleiro();
	} else {
	    request.setAttribute("vencedor", -1);
	    request.setAttribute("tabuleiro", this.tabuleiro);
	    request.setAttribute("jogadorDaVez", this.jogadorDaVez);
	    request.getRequestDispatcher("tabuleiro.jsp").forward(request, response);
	}
    }

    private void reiniciaTabuleiro() {
        for (int[] tabuleiro1 : this.tabuleiro) {
            for (int j = 0; j < this.tabuleiro.length; j++) {
                tabuleiro1[j] = 0;
            }
        }
    }

    private void realizaJogadaComputador() {

	switch (nivelJogo) {
	case 1:
	    realizaJogadaComputadorLevel1();
	    break;
	case 2:
	    realizaJogadaComputadorLevel2();
	    break;

	default:
	    break;
	}
    }

    private void realizaJogadaComputadorLevel1() {
	boolean encontrou = false;

	while (!encontrou) {
	    int linha = sorteiaPosicao();
	    int coluna = sorteiaPosicao();

	    if (this.tabuleiro[linha][coluna] == 0) {
		encontrou = true;
		this.tabuleiro[linha][coluna] = 1;
	    }
	}
    }
    
    private void realizaJogadaComputadorLevel2() {
	Arvore g = new Arvore(tabuleiro, -1);
	int qtdePossiveis = g.quantidadeJogadasPossiveis(tabuleiro);

	System.out.println("Jogo atual: " + g.exibeMatriz(tabuleiro));
	System.out.println("Quantidade possiveis: " + qtdePossiveis);

	g.setAltura(9 - qtdePossiveis);
	System.out.println("altura setada: " + g.getAltura());
	g.geraArvore(g, tabuleiro); 
	int melhorJogada = g.escolheMelhorJogada(g);

	System.out.println("melhor jogada: " + melhorJogada);
	
	tabuleiro = g.getFilhos().get(melhorJogada).getTabuleiro();

	System.out.println("Jogo novo: " + g.exibeMatriz(tabuleiro));

    }

    private void testaVencedor(HttpServletRequest request) {
	if (
	(this.tabuleiro[0][0] == 1 && this.tabuleiro[0][1] == 1 && this.tabuleiro[0][2] == 1)
		|| (this.tabuleiro[1][0] == 1 && this.tabuleiro[1][1] == 1 && this.tabuleiro[1][2] == 1)
		|| (this.tabuleiro[2][0] == 1 && this.tabuleiro[2][1] == 1 && this.tabuleiro[2][2] == 1) ||
		// Vertical
		(this.tabuleiro[0][0] == 1 && this.tabuleiro[1][0] == 1 && this.tabuleiro[2][0] == 1)
		|| (this.tabuleiro[0][1] == 1 && this.tabuleiro[1][1] == 1 && this.tabuleiro[2][1] == 1)
		|| (this.tabuleiro[0][2] == 1 && this.tabuleiro[1][2] == 1 && this.tabuleiro[2][2] == 1) ||
		// Transversal
		(this.tabuleiro[0][0] == 1 && this.tabuleiro[1][1] == 1 && this.tabuleiro[2][2] == 1)
		|| (this.tabuleiro[2][0] == 1 && this.tabuleiro[1][1] == 1 && this.tabuleiro[0][2] == 1)) {
	    this.vencedor = 1;
	} else if (
	// Horizontal
	(this.tabuleiro[0][0] == 2 && this.tabuleiro[0][1] == 2 && this.tabuleiro[0][2] == 2)
		|| (this.tabuleiro[1][0] == 2 && this.tabuleiro[1][1] == 2 && this.tabuleiro[1][2] == 2)
		|| (this.tabuleiro[2][0] == 2 && this.tabuleiro[2][1] == 2 && this.tabuleiro[2][2] == 2) ||
		// Vertical
		(this.tabuleiro[0][0] == 2 && this.tabuleiro[1][0] == 2 && this.tabuleiro[2][0] == 2)
		|| (this.tabuleiro[0][1] == 2 && this.tabuleiro[1][1] == 2 && this.tabuleiro[2][1] == 2)
		|| (this.tabuleiro[0][2] == 2 && this.tabuleiro[1][2] == 2 && this.tabuleiro[2][2] == 2) ||
		// Transversal
		(this.tabuleiro[0][0] == 2 && this.tabuleiro[1][1] == 2 && this.tabuleiro[2][2] == 2)
		|| (this.tabuleiro[2][0] == 2 && this.tabuleiro[1][1] == 2 && this.tabuleiro[0][2] == 2)) {
	    this.vencedor = 2;
	} else if (this.qtdeJogadas == 9) {
	    this.vencedor = -1;
	}

	if (this.vencedor != 0) {
            switch (this.vencedor) {
                case 1:
                    request.setAttribute("vencedor", 1);
                    request.setAttribute("jogador", this.jogador2);
                    break;
                case 2:
                    request.setAttribute("vencedor", 2);
                    request.setAttribute("jogador", this.jogador2);
                    break;
                default:
                    request.setAttribute("vencedor", 0);
                    request.setAttribute("jogador", this.jogador2);
                    break;
            }

	    this.vencedor = 0;
	    this.inicio = true;

	    this.emAndamento = false;

	}
    }

    private void realizaJogadaHumano(HttpServletRequest request) {
	this.tabuleiro[0][0] = Integer.valueOf(request.getParameter("casa_1"));
	this.tabuleiro[0][1] = Integer.valueOf(request.getParameter("casa_2"));
	this.tabuleiro[0][2] = Integer.valueOf(request.getParameter("casa_3"));
	this.tabuleiro[1][0] = Integer.valueOf(request.getParameter("casa_4"));
	this.tabuleiro[1][1] = Integer.valueOf(request.getParameter("casa_5"));
	this.tabuleiro[1][2] = Integer.valueOf(request.getParameter("casa_6"));
	this.tabuleiro[2][0] = Integer.valueOf(request.getParameter("casa_7"));
	this.tabuleiro[2][1] = Integer.valueOf(request.getParameter("casa_8"));
	this.tabuleiro[2][2] = Integer.valueOf(request.getParameter("casa_9"));
    }

    private void inicializaTabuleiro() {
        for (int[] tabuleiro1 : this.tabuleiro) {
            for (int j = 0; j < this.tabuleiro.length; j++) {
                tabuleiro1[j] = 0;
            }
        }
    }

    private int sorteiaPrimeiroJogador() {
	Random random = new Random();
	int intervalo_randomico = random.nextInt(2) + 1;
	return intervalo_randomico; // 1 - computador | 2 - humano
    }

    private int sorteiaPosicao() {
	Random random = new Random();
	int intervalo_randomico = random.nextInt(3);
	return intervalo_randomico; // 0, 1 ou 2 | para matriz 3x3
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
	return "Short description";
    }// </editor-fold>

}
