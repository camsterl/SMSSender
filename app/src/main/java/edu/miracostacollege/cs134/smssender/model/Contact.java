package edu.miracostacollege.cs134.smssender.model;

public class Contact {
    private long mId;
    private String mName;
    private String mPhone;

    public Contact(){
        this(-1L, "", "");
    }

    public Contact(long id, String name, String phone)
    {
        mId = id;
        mName = name;
        mPhone = phone;
    }

    public Contact(String name, String phone) {
        mId = -1;
        mName = name;
        mPhone = phone;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) { mId = id; }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "Id=" + mId +
                ", Name='" + mName + '\'' +
                ", Phone='" + mPhone + '\'' +
                '}';
    }
}
