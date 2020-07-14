package ht.stanj.kontaksync;

public class Contact {

    private int id;
    private String name;
    private String phone;
    private boolean status;

    public Contact() {
    }

    public Contact(int id, String name, String phone, boolean status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
