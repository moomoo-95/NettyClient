package moomoo.netty.client.command.handler;

import moomoo.netty.client.service.ServiceManager;

import static moomoo.netty.client.command.base.PrintMessage.printConsole;

/**
 * control command 명령어에 대한 처리하는 메서드
 */
public class ControlCommandHandler {

    private ControlCommandHandler() {
        // nothing
    }

    /**
     * q 명령어에 대한 처리, 프로세스 종료
     */
    public static void processQuitCommand() {
        // 수정 필요 exit 말고 자연스럽게 종료되도록
        printConsole("Process is about to quit");
        ServiceManager.getInstance().setQuit(true);
        ServiceManager.getInstance().stopService();

    }
}
