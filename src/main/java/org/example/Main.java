package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.A;
import org.example.models.Produto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        criarPlanilha(dataScrapping());

    }

    private static ArrayList<Produto> dataScrapping(){
        System.setProperty("webdriver.edge.driver","src/main/resources/msedgedriver.exe");

        EdgeOptions options =  new EdgeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singleton("enable-automation"));


        WebDriver driver = new EdgeDriver(options);

        driver.get("https://amazon.com.br");

        WebElement inputPesquisa = driver.findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]"));
        inputPesquisa.sendKeys("celular");
        inputPesquisa.submit();

        waitForIt(15000);

        List<WebElement> nomeProduto = driver.findElements(By.xpath("//span[@class='a-size-base-plus a-color-base a-text-normal']"));

       List<WebElement> precoProdutos = driver.findElements(By.xpath("//div[@class='a-row a-size-base a-color-base']"));
        ArrayList<Produto> produtos =  new ArrayList<Produto>();

        for (int i = 0; i < nomeProduto.size(); i++) {
           produtos.add(new Produto(nomeProduto.get(i).getText(), precoProdutos.get(i).getText()));
        }
        for (Produto produto : produtos){
            System.out.println(produto.toString());
        }
        waitForIt(10000);
        driver.quit();
        return produtos;
    }

    private static void waitForIt(long tempo){
        try {
            new Thread().sleep(tempo);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void criarPlanilha(ArrayList<Produto> produtos){
        Workbook pasta = new XSSFWorkbook();

        Sheet planilha =  pasta.createSheet("PRODUTOS");

        Font fonteNegrito =  pasta.createFont();

        CellStyle estiloNegrito = pasta.createCellStyle();
        estiloNegrito.setFont(fonteNegrito);

        Row row = planilha.createRow(0);
        Cell c1 = row.createCell(0);
        c1.setCellValue("Nome");
        c1.setCellStyle(estiloNegrito);

        Cell c2 = row.createCell(1);
        c2.setCellValue("Valor a vista");
        c2.setCellStyle(estiloNegrito);

        Cell c3 = row.createCell(2);
        c3.setCellValue("Quantidade de parcelas");
        c3.setCellStyle(estiloNegrito);

        Cell c4 = row.createCell(3);
        c4.setCellValue("Valor a Prazo");
        c4.setCellStyle(estiloNegrito);

        planilha.autoSizeColumn(0);
        planilha.autoSizeColumn(1);
        planilha.autoSizeColumn(2);
        planilha.autoSizeColumn(3);

        if(produtos.size() > 0){
            int i = 1;

            for (Produto produto:produtos){
                Row linhaProduto = planilha.createRow(i);

                Cell celulaNome = linhaProduto.createCell(0);
                celulaNome.setCellValue(produto.getNome());

                Cell celulaVista = linhaProduto.createCell(1);
                celulaVista.setCellValue(produto.getValorAVista().toString());

                Cell celulaParcela = linhaProduto.createCell(2);
                celulaParcela.setCellValue(produto.getParcelas());

                Cell celulaPrazo = linhaProduto.createCell(3);
                celulaPrazo.setCellValue(produto.getValorAPrazo().toString());
                i++;
            }
        }

        try(FileOutputStream arquivo =  new FileOutputStream("produtos.xlsx")) {
            pasta.write(arquivo);

            JOptionPane.showMessageDialog(null, "Planilha criada com sucesso");
        }catch (Exception e){
            System.out.println("Erro ao criar a planilha: "+e.getMessage());
        }finally {
            try {
                pasta.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
}