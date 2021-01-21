<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JOGO DA VELHA</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            #tabuleiro td {
                width: 100px;
                height: 100px;
                text-align: center;
                vertical-align: middle;
                font-size: 40px;
                cursor: pointer;
            }
            
            #divResultado {
            	margin: auto;
			    width: 100%;
			    text-align: center;
            }
            
        </style>
    </head>
    <body>
        <%
        String jogador = (String)(request.getAttribute("jogador"));
        int[][] tabuleiro = (int[][])(request.getAttribute("tabuleiro"));
        int jogadorDaVez = (Integer)(request.getAttribute("jogadorDaVez"));
        %>
         <div>
            <div align="center">
            	<% if (jogadorDaVez != -1) { %>
                <h1 id="vezJogador">Sua vez de jogar jogador  <%= jogador %>!</h1>
                <% } %>
                
                <br>
                <br>
                <form id="form2" name="form2" action="Controlador" method="POST">
                <table id="tabuleiro" border="1">
                    <%
                    int pos = 1;
                    for (int i = 0; i < tabuleiro.length; i++) { %>
                    <tr>
                        <%
                        for (int j = 0; j < tabuleiro.length; j++) {
                            String labelValor = "";
                            int valor = 0;
                            if (tabuleiro[i][j] == 1) {
                                labelValor = "X";
                                valor = 1;
                            } else if(tabuleiro[i][j] == 2) {
                                labelValor = "O";
                                valor = 2;
                            }%>
                            <input type="hidden" value="<% out.print(valor); %>" name="casa_<% out.print(pos); %>" id="casa_<% out.print(pos); %>">
                            <td <% if (valor != 0) { out.print("disabled='disabled'"); } else { %> onclick="salvaJogada(<% out.print(jogadorDaVez); %>,<% out.print(pos); %>);" <% } %>><% out.print(labelValor); %></td>
                            <%
                            pos++;
                        } %>
                    </tr>
                    <%
                    }
                    %>
                </table>
                </form>
            </div>
         </div>
         <div id="divResultado">
         <%
        	int vencedor = (Integer)request.getAttribute("vencedor");
         
         	if (vencedor != -1) {
            
	            if (vencedor == 0) { %>
	                <h1>Deu velha!</h1>
	            <%
	            } else {
	        		if (vencedor == 1) { %>
	                	<h1>Que pena <%= jogador%>, você perdeu!</h1>
	                	<%
	        		} else { %>
	        		    <h1>Parabéns <%= jogador%>, você venceu!</h1>
	                	<%
	        		}
	            } %>
	            <a href="index.html">Iniciar jogo</a>
	            <%
         	} else {
        	%>
        	<script>
		        function salvaJogada(jogador, posicao) {
		            document.getElementById("casa_"+posicao).value = jogador;
		            document.getElementById("form2").submit();
		        }
		    </script>
        	<%
        } %>
        </div>
    </body>
</html>