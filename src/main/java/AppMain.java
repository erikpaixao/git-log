import utils.ExecCommand;

public class AppMain {
    public static void main(String[] args){
        ExecCommand.execCommand("git log --name-status --pretty=tformat:%B", args[0], args[1]);
        //ExecCommand.execCommand("git log --name-status --pretty=tformat:%B", "1152078", "/kdi/git/aic-gestao-componentes-estatico/");

    }
}
