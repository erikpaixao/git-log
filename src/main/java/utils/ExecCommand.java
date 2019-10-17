package utils;

import models.LineStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ExecCommand {

    private static File file;
    private static String branch;
    private static String path;

    private ExecCommand(){}

    public static void execCommand(String command, String branch1, String path1){

        Process procces;
        String s;

        file = new File(path1);
        branch = branch1;
        path = path1;

        Set<LineStatus> lines = new HashSet<>();

        try {
            procces = Runtime.getRuntime().exec(command, null, file);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(procces.getInputStream()));
            String task = null;
            while ((s = br.readLine()) != null) {
                if (s.toLowerCase().startsWith("task")) {
                    task = s;
                } else {
                    try {
                        lines.add(new LineStatus(s.split("\t")[0], s.split("\t")[1], task));
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
            procces.waitFor();
            procces.destroy();
            separarLinhas(lines.parallelStream()
                    .filter(lineStatus -> lineStatus.getTask()
                            .contains(branch)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void separarLinhas(Set<LineStatus> lineStatuses){
        TreeSet<String> adicionados = new TreeSet<>();
        TreeSet<String> deletados = new TreeSet<>();
        TreeSet<String> modificados = new TreeSet<>();

        lineStatuses.parallelStream().filter(lineStatus ->
                lineStatus.getStatus().equals("D"))
                .forEach(lineStatus -> deletados.add(lineStatus.getArquivo()));

        lineStatuses.parallelStream().filter(lineStatus ->
                lineStatus.getStatus().equals("A") && !deletados.contains(lineStatus.getArquivo()))
                .forEach(lineStatus -> adicionados.add(lineStatus.getArquivo()));

        lineStatuses.parallelStream().filter(lineStatus ->
                lineStatus.getStatus().equals("M") &&
                        (!deletados.contains(lineStatus.getArquivo()) &&
                        !adicionados.contains(lineStatus.getArquivo())))
                .forEach(lineStatus -> modificados.add(lineStatus.getArquivo()));

        gravarLinhas(adicionados, "adicionados.txt");
        gravarLinhas(modificados, "modificados.txt");
        gravarLinhas(deletados, "deletados.txt");

    }

    private static void gravarLinhas(SortedSet<String> listas, String filename){
        try (FileWriter writer = new FileWriter(path + filename)){
            writer.write("Projeto: " + file.getName() + System.lineSeparator());
            writer.write("Branch: " + branch + System.lineSeparator());
            writer.write(System.lineSeparator());
            for(String str: listas) {
                writer.write(file.getName() + "/" + str + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
