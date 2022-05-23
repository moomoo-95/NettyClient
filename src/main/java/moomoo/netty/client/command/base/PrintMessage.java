package moomoo.netty.client.command.base;

public class PrintMessage {

    public static final String PRINT_LINE = "--------------------------------------------------------------------------\n";

    private PrintMessage() {
        // nothing
    }

    // 입력 받을 때 나오는 출력문
    public static final String PRINT_INPUT_MODE =
            PRINT_LINE +
            "Please input command. (QUIT = q)\n" +
            PRINT_LINE;

    public static void printConsole(String str) {
        System.out.println(str);
    }
}
