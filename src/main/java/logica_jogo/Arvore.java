package logica_jogo;

import java.util.ArrayList;
import java.util.List;

public class Arvore {

    private int altura;
    private List<Arvore> filhos;
    private int[][] tabuleiro;
    private final int jogadorDaVez;
    private int pesoVitoria;
    private int pesoDerrota;
    private final int alturaPadrao = 9;

    public Arvore(int[][] tabuleiro, int jogadorDaVez) {
	filhos = new ArrayList<Arvore>();
	this.tabuleiro = tabuleiro;
	this.jogadorDaVez = jogadorDaVez;
	pesoVitoria = 0;
	pesoDerrota = 0;
    }

    public int quantidadeJogadasPossiveis(int[][] tabuleiro) {
	int jogadasPossiveis = 0;
        for (int[] tabuleiro1 : tabuleiro) {
            for (int j = 0; j < tabuleiro.length; j++) {
                if (tabuleiro1[j] == 0) {
                    jogadasPossiveis++;
                }
            }
        }

	return jogadasPossiveis;
    }

    public int escolheMelhorJogada(Arvore no) {
	int maiorNotaVitoria = no.filhos.get(0).pesoVitoria;
	int menorNotaDerrota = no.filhos.get(0).pesoDerrota;
	int posicaoVitoria = 0;
	int posicaoDerrota = 0;

	System.out.println("i:" + 0 + " pesoV:" + no.filhos.get(0).pesoVitoria + " pesoD:" + no.filhos.get(0).pesoDerrota + " tab:" + exibeMatriz(no.filhos.get(0).tabuleiro));
	int i;
	for (i = 1; i < no.filhos.size(); i++) {
	    System.out.println("i:" + i +  " pesoV:" + no.filhos.get(i).pesoVitoria + " pesoD:" + no.filhos.get(i).pesoDerrota + " tab:" + exibeMatriz(no.filhos.get(i).tabuleiro));
	    if (no.filhos.get(i).pesoVitoria > maiorNotaVitoria) {
		posicaoVitoria = i;
		maiorNotaVitoria = no.filhos.get(i).pesoVitoria;
	    }
	}
	    return posicaoVitoria;
    }

    public List<Arvore> getFilhos() {
	return filhos;
    }

    public void setFilhos(List<Arvore> filhos) {
	this.filhos = filhos;
    }

    private void exibe(Arvore no) {
	if (no.altura < alturaPadrao) {
	    for (int i = 0; i < no.filhos.size(); i++) {
		exibe(no.filhos.get(i));
	    }
	}
	System.out.println("altura: " + no.altura + " peso: " + no.pesoVitoria + " tab: " + exibeMatriz(no.tabuleiro));
    }

    public String exibeMatriz(int[][] mat) {
	String ret = "";

        for (int[] mat1 : mat) {
            for (int j = 0; j < mat.length; j++) {
                ret += mat1[j];
            }
            ret += " ";
        }

	return ret;
    }

    public Arvore geraArvore(Arvore no, int[][] tabuleiro) {

	Arvore aux;
	int multiplicador = 9 - no.altura;
	int pesoV = verificaSituacaoTabuleiroVitoria(no.tabuleiro);
	int pesoD = (verificaSituacaoTabuleiroDerrota(no.tabuleiro)==1) ? no.altura : 0;

	if (no.altura == 9) {
	    no.pesoVitoria = pesoV;
	} else {
	    no.pesoVitoria = pesoV * multiplicador;
	}
	no.pesoDerrota = pesoD;

	if (no.altura >= alturaPadrao || pesoD != 0 || pesoV == 1) {
	    return no;
	} else {
	    int aux_for = alturaPadrao - no.altura;
	    for (int i = 0; i < aux_for; i++) {
		int[][] tab_aux = clonaMatriz(no.tabuleiro);
		Arvore novoFilho = new Arvore(tab_aux, (no.jogadorDaVez * -1));
		novoFilho.altura = no.altura + 1;
		novoFilho.tabuleiro = realizaJogadaComputador(tab_aux, novoFilho.jogadorDaVez, novoFilho.altura, i, no);
		no.filhos.add(novoFilho);
		aux = geraArvore(novoFilho, novoFilho.tabuleiro);
		if (aux.pesoVitoria > no.pesoVitoria) {
		    no.pesoVitoria = aux.pesoVitoria;
		}
		if (no.pesoDerrota == 0) {
		    no.pesoDerrota = aux.pesoDerrota;
		}
	    }
	    return no; 
	}
    }

    private int verificaSituacaoTabuleiroVitoria(int[][] tabuleiro) {

	int peso = 0;
	if (
	(tabuleiro[0][0] == 1 && tabuleiro[0][1] == 1 && tabuleiro[0][2] == 1)
		|| (tabuleiro[1][0] == 1 && tabuleiro[1][1] == 1 && tabuleiro[1][2] == 1)
		|| (tabuleiro[2][0] == 1 && tabuleiro[2][1] == 1 && tabuleiro[2][2] == 1) ||
		(tabuleiro[0][0] == 1 && tabuleiro[1][0] == 1 && tabuleiro[2][0] == 1)
		|| (tabuleiro[0][1] == 1 && tabuleiro[1][1] == 1 && tabuleiro[2][1] == 1)
		|| (tabuleiro[0][2] == 1 && tabuleiro[1][2] == 1 && tabuleiro[2][2] == 1) ||
		(tabuleiro[0][0] == 1 && tabuleiro[1][1] == 1 && tabuleiro[2][2] == 1)
		|| (tabuleiro[2][0] == 1 && tabuleiro[1][1] == 1 && tabuleiro[0][2] == 1)) {
	    peso = 1;
	}

	return peso;
    }
    
    private int verificaSituacaoTabuleiroDerrota(int[][] tabuleiro) {

	int pesoD = 0; 

	if (
	(tabuleiro[0][0] == 2 && tabuleiro[0][1] == 2 && tabuleiro[0][2] == 2)
		|| (tabuleiro[1][0] == 2 && tabuleiro[1][1] == 2 && tabuleiro[1][2] == 2)
		|| (tabuleiro[2][0] == 2 && tabuleiro[2][1] == 2 && tabuleiro[2][2] == 2) ||
		(tabuleiro[0][0] == 2 && tabuleiro[1][0] == 2 && tabuleiro[2][0] == 2)
		|| (tabuleiro[0][1] == 2 && tabuleiro[1][1] == 2 && tabuleiro[2][1] == 2)
		|| (tabuleiro[0][2] == 2 && tabuleiro[1][2] == 2 && tabuleiro[2][2] == 2) ||
		(tabuleiro[0][0] == 2 && tabuleiro[1][1] == 2 && tabuleiro[2][2] == 2)
		|| (tabuleiro[2][0] == 2 && tabuleiro[1][1] == 2 && tabuleiro[0][2] == 2)) {
	    pesoD = 1;
	}

	return pesoD;
    }

    private int[][] clonaMatriz(int[][] tab) {
	int[][] tab_ret = new int[3][3];

	for (int i = 0; i < tab.length; i++) {
            System.arraycopy(tab[i], 0, tab_ret[i], 0, tab.length);
	}

	return tab_ret;
    }

    public int[][] realizaJogadaComputador(int[][] tab_local, int jogadorDaVez, int altura, int i, Arvore pai) {

	boolean encontrou = false;
	int linha = 0, coluna = 0;

	switch (i) {
	case 1:
	    linha = 0;
	    coluna = 1;
	    break;
	case 2:
	    linha = 0;
	    coluna = 2;
	    break;
	case 3:
	    linha = 1;
	    coluna = 0;
	    break;
	case 4:
	    linha = 1;
	    coluna = 1;
	    break;
	case 5:
	    linha = 1;
	    coluna = 2;
	    break;
	case 6:
	    linha = 2;
	    coluna = 0;
	    break;
	case 7:
	    linha = 2;
	    coluna = 1;
	    break;
	case 8:
	    linha = 2;
	    coluna = 2;
	    break;
	default:
	    linha = 0;
	    coluna = 0;
	    break;
	}

	boolean encontrouNoPai;

	do {
	    if (tab_local[linha][coluna] == 0) {
		
		encontrouNoPai = false;
		for (int j = 0; j < pai.filhos.size(); j++) {
		    if (pai.filhos.get(j).tabuleiro[linha][coluna] != 0) {
			encontrouNoPai = true;
		    }
		}
		if (!encontrouNoPai) {
		    tab_local[linha][coluna] = (jogadorDaVez == 1) ? 1 : 2;
		    encontrou = true;
		} else {
		    if (coluna <= 1) {
			coluna++;
		    } else {
			coluna = 0;
			if (linha <= 1) {
			    linha++;
			} else {
			    linha = 0;
			}
		    }
		}
	    } else {
		if (coluna <= 1) {
		    coluna++;
		} else {
		    coluna = 0;
		    if (linha <= 1) {
			linha++;
		    } else {
			linha = 0;
		    }
		}
	    }
	} while (!encontrou);

	return tab_local;
    }

    public int getAltura() {
	return altura;
    }

    public void setAltura(int altura) {
	this.altura = altura;
    }

    public int[][] getTabuleiro() {
	return tabuleiro;
    }

    public void setTabuleiro(int[][] tabuleiro) {
	this.tabuleiro = tabuleiro;
    }

}