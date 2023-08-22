package moomoo.netty.client.command;

import moomoo.netty.client.AppInstance;
import moomoo.netty.client.channel.NettyChannelManager;
import moomoo.netty.client.channel.message.outgoing.HbReq;
import moomoo.netty.client.command.handler.ControlCommandHandler;
import moomoo.netty.client.message.TcpLoginReqMessage;
import moomoo.netty.client.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static moomoo.netty.client.command.base.CommandType.COMMAND_LOGIN_REQ;
import static moomoo.netty.client.command.base.CommandType.COMMAND_Q;
import static moomoo.netty.client.command.base.PrintMessage.PRINT_INPUT_MODE;
import static moomoo.netty.client.command.base.PrintMessage.printConsole;

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
        int systemId = AppInstance.SYSTEM_ID;
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

                // TODO 임시값
                NettyChannelManager.getInstance().getClientChannel("임시값").sendMessage(new HbReq());
                AppInstance.getInstance().setLogin(true);
                break;
            default:
        }
    }

    public void stop() {
        isQuit = true;
    }

    private static byte[] pcmTestData = new byte[] {
            -128, -127, -126, -125, -124, -123, -122, -121, -120, -119,
            -118, -117, -116, -115, -114, -113, -112, -111, -110, -109,
            -108, -107, -106, -105, -104, -103, -102, -101, -100, -99,
            -98, -97, -96, -95, -94, -93, -92, -91, -90, -89,
            -88, -87, -86, -85, -84, -83, -82, -81, -80, -79,
            -78, -77, -76, -75, -74, -73, -72, -71, -70, -69,
            -68, -67, -66, -65, -64, -63, -62, -61, -60, -59,
            -58, -57, -56, -55, -54, -53, -52, -51, -50, -49,
            -48, -47, -46, -45, -44, -43, -42, -41, -40, -39,
            -38, -37, -36, -35, -34, -33, -32, -31, -30, -29,

            -28, -27, -26, -25, -24, -23, -22, -21, -20, -19,
            -18, -17, -16, -15, -14, -13, -12, -11, -10, -9,
            -8, -7, -6, -5, -4, -3, -2, -1, 0, 1,
            2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
            12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
            22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
            32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
            42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
            62, 63, 64, 65, 66, 67, 68, 69, 70, 71,

            72, 73, 74, 75, 76, 77, 78, 79, 80, 81,
            82, 83, 84, 85, 86, 87, 88, 89, 90, 91,
            92, 93, 94, 95, 96, 97, 98, 99, 100, 101,
            102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
            112, 113, 114, 115, 116, 117, 118, 119, 120, 121,
            122, 123, 124, 125, 126, 127, -128, -126, -124, -122,
            -120, -118, -116, -114, -112, -110, -108, -106, -104, -102,
            -100, -98, -96, -94, -92, -90, -88, -86, -84, -82,
            -80, -78, -76, -74, -72, -70, -68, -66, -64, -62,
            -60, -58, -56, -54, -52, -50, -48, -46, -44, -42,

            -40, -38, -36, -34, -32, -30, -28, -26, -24, -22,
            -20, -18, -16, -14, -12, -10, -8, -6, -4, -2,
    };
}