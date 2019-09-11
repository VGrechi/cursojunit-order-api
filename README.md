# Order-API

## Configurando o Projeto

1. Baixe o arquivo .zip da branch master
2. Descompacte o arquivo em uma pasta de sua escolha
3. Abra o assistente do Maven na barra lateral direita e execute **order-api > Lifecycle > _install_**
4. Clique com o direito sobre a class OrderApiApplication.java, localizada em **src/main/java/com/cursojunit/orderapi**, e clique em _Run_
5. Será criado um **Launcher** no lado direito da barra superior com o nome _ORDERAPIAPPLICATION_
    1. Selecione _Edit Configurations_
    2. Na aba _Configuration_, acesse o assistente da linha _Environment variables_
    3. Clique no **+**
    4. Adicione os valores: _BANKSLIP_MAX_VALUE_ e _1000_
    5. Clique em **OK**
    6. Clique em **APPLY**
6. Ao lado do **Launcher** clique no triãngulo verde para executar o projeto
    
## Configurando o Banco de Dados

1. Após executar o projeto, acesse em seu navegador _http://localhost:8080/h2_
2. As credenciais de acesso e URL do Banco de Dados encontram-se no arquivo **application.properties**, localizado em **src/main/resources**
3. Execute os scripts localizados em **/src/main/resources/scripts/db-initializer.sql**


