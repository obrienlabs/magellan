package global.packet.magellan.controller;


public class Api {

    private long id;
    private String content;

    public Api(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

