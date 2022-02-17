package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String EXTENSION = "php,txt,java,json,xls,xlsx,log";
    private static String text;
    private static String extension;
    private static int total = 0;

    public static void main(String[] args) {
        try {
            extension = EXTENSION;
            text = args[1];
            if (args.length == 3) {
                extension = args[2];
            }
            System.out.println("Directory: " + args[0]);
            System.out.println("Find: " + text);
            System.out.println("Extensions: " + extension);
            showFiles(args[0]);
        } catch (Exception e) {
            System.out.println("java -jar findtext.jar PATH TEXT");
            System.out.println("java -jar findtext.jar PATH TEXT EXTENSIONS(php,txt,java,json,xls,xlsx,log)");
        }
    }

    public static void showFiles(String path) {

        try {

            List<File> directories = new ArrayList<>();

            File file = new File(path);
            File[] list = file.listFiles();
            for (File f : list) {
                if (f.isDirectory()) {
                    directories.add(f);
                } else {
                    if (isTargetFile(f.getName())) {
                        int q = openFile(f.getAbsolutePath());
                        if (q > 0) {
                            System.out.println("Total:" + q);
                        }
                    }
                }
            }

            for (File d : directories) {
                showFiles(d.getPath());
            }

        } catch (Exception e) {
            System.out.println("Erro ao percorrer pasta: " + path);
        }

    }

    private static boolean isTargetFile(String f) {
        if (f.contains(".")) {
            String[] v = f.split("\\.");
            return extension.contains(v[1].toLowerCase());
        } else {
            return false;
        }
    }

    private static int openFile(String path) {
        int qtd = 0;
        try {
            boolean shownFile = false;
            try (BufferedReader buffRead = new BufferedReader(new FileReader(path))) {
                String linha = "";
                int l = 0;
                while (true) {
                    linha = buffRead.readLine();
                    l++;
                    if (linha != null && linha.contains(text)) {
                        qtd++;
                        if (!shownFile) {
                            System.out.println("");
                            System.out.println(path);
                            shownFile = true;
                        }
                        System.out.println(l + ":" + linha);
                        total++;
                    }
                    if (linha == null) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo: " + path);
        }
        return qtd;
    }

    public static void escritor(String path) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        String linha = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Escreva algo: ");
        linha = in.nextLine();
        buffWrite.append(linha + "\n");
        buffWrite.close();
    }

}
