import utils.ExecCommand;

public class AppMain {
    public static void main(String[] args){
        ExecCommand.execCommand("git log --name-status --pretty=format: ", args[0], args[1]);
    }
}
