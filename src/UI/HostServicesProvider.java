package UI;

import javafx.application.HostServices;

public class HostServicesProvider {
    private static HostServices hostServices;

    public static void setHostServices(HostServices hostServices) {
        HostServicesProvider.hostServices = hostServices;
    }

    public static HostServices getHostServices() {
        return hostServices;
    }
}
