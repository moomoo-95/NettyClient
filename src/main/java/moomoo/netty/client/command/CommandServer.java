package moomoo.netty.client.command;

import moomoo.netty.client.channel.NettyChannelManager;
import moomoo.netty.client.command.handler.ControlCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static moomoo.netty.client.command.base.CommandType.COMMAND_Q;
import static moomoo.netty.client.command.base.PrintMessage.*;

/**
 * command 를 입력받아 검증 및 commandInfo 로 변경하여 리스트에 넣는 클래스
 */
public class CommandServer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CommandServer.class);

    private final Scanner scanner;

    private boolean isQuit = false;

    public CommandServer() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (!isQuit) {
            try {
                printConsole(PRINT_INPUT_MODE);
                String inputCommand = scanner.nextLine();

                if (!inputCommand.isEmpty()) {
                    commandInput(inputCommand);
                }
            } catch (Exception e) {
                log.error("CommandServer.run ", e);
                if (e.getClass() == InterruptedException.class) {
                    stop();
                }
            }
        }
        log.debug("CommandServer stop.");
    }

    /**
     * command를 입력받고 어떤 command인지 파싱하는 메서드
     */
    public void commandInput(String inputCommand) {
        if (inputCommand.equals(COMMAND_Q)) {
            ControlCommandHandler.processQuitCommand();
        } else {
            NettyChannelManager.getInstance().getTcpClientChannel().sendMessage(inputCommand);
        }
    }

    public void stop() {
        isQuit = true;
    }
}