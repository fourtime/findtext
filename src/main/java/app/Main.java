package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String EXTENSION = "php,txt,java,json,xls,xlsx,log";
    private static String text;
    private static String extension;
    private static int total = 0;
    private static String changeTo = null;

    public static void main(String[] args) {
        try {
            extension = EXTENSION;
            text = args[1];
            if (args.length >= 3) {
                extension = args[2];
            }
            if (args.length >= 4) {
                changeTo = args[3];
            }
            System.out.println("Directory: " + args[0]);
            System.out.println("Find: " + text);
            System.out.println("Extensions: " + extension);
            showFiles(args[0]);
            System.out.println();
            System.out.println("Total de textos encontrados: " + total);
        } catch (Exception e) {
            System.out.println("java -jar findtext.jar PATH TEXT");
            System.out.println("java -jar findtext.jar PATH TEXT EXTENSIONS(php,txt,java,json,xls,xlsx,log)");
            System.out.println("java -jar findtext.jar PATH TEXT EXTENSIONS(php,txt,java,json,xls,xlsx,log) CHANGETO");
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
                            modifyFile(f, text, changeTo);
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
            return extension.contains(v[v.length - 1].toLowerCase());
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
                        System.out.println(l + ": " + linha);
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

    public static String readFile(File file) throws IOException {
        int len = (int) file.length();
        byte[] bytes = new byte[len];
        try (FileInputStream fis = new FileInputStream(file)) {
            assert len == fis.read(bytes);
        }
        return new String(bytes, "UTF-8");
    }

    private static void modifyFileOld(File f, String oldString, String newString) throws Exception {
        String content = readFile(f);
        String newContent = content.replaceAll(oldString, newString);
        try (FileWriter writer = new FileWriter(f)) {
            writer.write(newContent);
        }
    }

    private static void modifyFile(File file, String oldString, String newString) {
        StringBuilder oldContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                oldContent.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
            String content = oldContent.toString();
            String newContent = content.replaceAll(oldString, newString);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(newContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
