package logica_jogo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ArvoreBKP {

    private int valor;
    private int altura;
    private List<ArvoreBKP> filhos;

    public ArvoreBKP() {
	valor = 0;
	filhos = new ArrayList<ArvoreBKP>();
	altura = 0;
    }

    public ArvoreBKP geraArvore(ArvoreBKP no) {

	ArvoreBKP aux = new ArvoreBKP();
	if (no.altura >= 2) { // crit�rio de parada da recursividade
	    no.valor = Integer.parseInt(JOptionPane.showInputDialog("Digite uma nota para o n�: "));
	    return no; // retorno recursivo
	} else {
	    for (int i = 0; i < 3; i++) {
		ArvoreBKP novoFilho = new ArvoreBKP();
		novoFilho.altura = no.altura + 1;
		no.filhos.add(novoFilho);
		aux = geraArvore(novoFilho); // inicio
		if (aux.valor > no.valor)
		    no.valor = aux.valor;
	    }
	    return no; // fim recursivo
	}
    }

    public void exibe(ArvoreBKP no) {
	if (no.altura < 2)
	    for (int i = 0; i < 3; i++) {
		exibe(no.filhos.get(i));
	    }
	System.out.println(no.altura + "  " + no.valor);
    }

    public static void main(String[] args) {
	ArvoreBKP g = new ArvoreBKP();
	g.geraArvore(g);
	g.exibe(g);
    }
}