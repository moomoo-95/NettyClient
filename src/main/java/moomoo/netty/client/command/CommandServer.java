package moomoo.netty.client.command;

import moomoo.netty.client.channel.NettyChannelManager;
import moomoo.netty.client.command.handler.ControlCommandHandler;
import moomoo.netty.client.message.TcpMngLoginReqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static moomoo.netty.client.command.base.CommandType.COMMAND_LOGIN_REQ;
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
        switch (inputCommand) {
            case COMMAND_Q:
                ControlCommandHandler.processQuitCommand();
                break;
            case COMMAND_LOGIN_REQ:
                TcpMngLoginReqMessage loginReqMessage = new TcpMngLoginReqMessage(0xbbbb, 0xaaaa, 1);
                log.debug("{}", loginReqMessage);

                NettyChannelManager.getInstance().getTcpClientChannel().sendMessage(loginReqMessage.getData());
                break;
            default:
                byte[] bytesMsg = inputCommand.getBytes(StandardCharsets.UTF_8);
                NettyChannelManager.getInstance().getTcpClientChannel().sendMessage(bytesMsg);
                break;
        }
    }

    public void stop() {
        isQuit = true;
    }
}