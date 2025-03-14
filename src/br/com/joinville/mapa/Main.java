package br.com.joinville.mapa;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Mapeamento de Áreas Verdes de Joinville!");
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\nDigite a opção que deseja acessar:");
            System.out.println("1- Listar Áreas Verdes");
            System.out.println("2- Avaliar Área Verde");
            System.out.println("3- Ver detalhe de uma Área Verde");
            System.out.println("4- Cadastrar nova Área Verde");
            System.out.println("0- Sair");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    AreaVerdeRepository.listarAreasVerdes().forEach(System.out::println);
                    break;
                case 2:
                    System.out.println("Avaliação em construção...");
                    break;
                case 3:
                    System.out.println("Detalhes em construção...");
                    break;
                case 4:
                    System.out.println("Cadastro em construção...");
                    break;
            }
        } while (opcao != 0);
        scanner.close();
    }

}
