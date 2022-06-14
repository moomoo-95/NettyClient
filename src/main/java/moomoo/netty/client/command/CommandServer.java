package moomoo.netty.client.command;

import moomoo.netty.client.channel.NettyChannelManager;
import moomoo.netty.client.command.handler.ControlCommandHandler;
import moomoo.netty.client.message.TcpDataRptMessage;
import moomoo.netty.client.message.TcpHeartbeatReqMessage;
import moomoo.netty.client.message.TcpLoginReqMessage;
import moomoo.netty.client.message.base.TcpLoginReqBody;
import moomoo.netty.client.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static moomoo.netty.client.command.base.CommandType.*;
import static moomoo.netty.client.command.base.PrintMessage.*;

/**
 * command 를 입력받아 검증 및 commandInfo 로 변경하여 리스트에 넣는 클래스
 */
public class CommandServer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CommandServer.class);

    private final Scanner scanner;

    private boolean isQuit = false;

    private long aiifSeq = 1;

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
        int systemId = 0xbbbb;
        long processArg = 0xaaaa;
        long index = 1;
        switch (inputCommand) {
            case COMMAND_Q:
                ControlCommandHandler.processQuitCommand();
                break;
            case COMMAND_LOGIN_REQ:
                TcpLoginReqMessage loginReqMessage = new TcpLoginReqMessage(systemId, processArg, index);
                log.debug("{} {} {}", ByteUtil.intToBytes(systemId, true), ByteUtil.longToBytes(processArg, true), ByteUtil.longToBytes(index, true));
                log.debug("login req msg : {}", loginReqMessage);
                log.debug("login req byt : {}", loginReqMessage.getData());

                NettyChannelManager.getInstance().getTcpClientChannel().sendMessage(loginReqMessage.getData());
                break;
            case COMMAND_HB:
                TcpHeartbeatReqMessage heartbeatReqMessage = new TcpHeartbeatReqMessage(systemId);
                log.debug("{}", ByteUtil.intToBytes(systemId, true));
                log.debug("hb req msg : {}", heartbeatReqMessage);
                log.debug("hb req byt : {}", heartbeatReqMessage.getData());

                NettyChannelManager.getInstance().getTcpClientChannel().sendMessage(heartbeatReqMessage.getData());
                break;
            case COMMAND_DATA_RPT:
                TcpDataRptMessage dataRptMessage = new TcpDataRptMessage(systemId, 10001, 8 + pcmTestData.length, aiifSeq, pcmTestData);
                aiifSeq++;
                log.debug("{} {} {} {}", ByteUtil.intToBytes(systemId, true),
                        ByteUtil.longToBytes(10001, true),
                        ByteUtil.longToBytes(9, true),
                        ByteUtil.longToBytes(12, true));
                log.debug("data msg : {}", dataRptMessage);
                log.debug("data byt : {}", dataRptMessage.getData());

                NettyChannelManager.getInstance().getTcpClientChannel().sendMessage(dataRptMessage.getData());
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

    private static byte[] pcmTestData = new byte[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
    };
}