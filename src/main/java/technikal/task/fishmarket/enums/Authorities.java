package technikal.task.fishmarket.enums;

public enum Authorities {
    USER,
    ADMIN;

    public final String getAuthority() {
        return this.name();
    }
}
