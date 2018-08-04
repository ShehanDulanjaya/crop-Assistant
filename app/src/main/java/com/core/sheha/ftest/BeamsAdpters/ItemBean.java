package com.core.sheha.ftest.BeamsAdpters;

/**
 * Created by Ahsan Ali.
 */
public class ItemBean {
    private  String itemname;
    private  String itemodesc;
    private String url;
    private String price;
    private String owner;

    public String getItemodesc() {
        return itemodesc;
    }

    public void setItemodesc(String itemodesc) {
        this.itemodesc = itemodesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    private String ownerid;
    private String itemid;


    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemdesc() {
        return itemodesc;
    }

    public void setItemdesc(String itemodesc) {
        this.itemodesc = itemodesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ItemBean(String itemname, String itemodesc, String url, String price, String owner, String ownerid, String itemid) {
        this.itemname = itemname;
        this.itemodesc = itemodesc;
        this.url = url;
        this.price = price;
        this.owner = owner;
        this.ownerid = ownerid;
        this.itemid = itemid;
    }
}
