package wifi4eu.wifi4eu.common.enums;

public enum RegistrationUsersStatus {
    UNREGISTERED(0),
    REGISTERED(1);

    private int status;

    RegistrationUsersStatus(int status) {
        this.status = status;
    }

    public int getValue() {
        return this.status;
    }

}
