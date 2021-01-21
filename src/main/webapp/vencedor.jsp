<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Jogo da Velha</title>
    </head>
    <body>
        <%
            String jogador = (String) (request.getAttribute("jogador"));
        	int vencedor = (Integer)request.getAttribute("vencedor");
            
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
            }
        %>
        <a href="index.html">Novo jogo</a>
    </body>
</html>
