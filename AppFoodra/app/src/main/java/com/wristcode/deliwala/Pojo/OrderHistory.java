package com.wristcode.deliwala.Pojo;

/**
 * Created by Ajay Jagadish on 09-Mar-18.
 */

public class OrderHistory
{
    public String oId, oDate, oResId, oResName, oResImage, oItems, oPayType, oTotal, oStatus;

    public OrderHistory() {}

    public String getoId() {return oId;}

    public void setoId(String oId) {this.oId = oId;}

    public String getoDate() {return oDate;}

    public void setoDate(String oDate) {this.oDate = oDate;}

    public String getoResId() {return oResId;}

    public void setoResId(String oResId) {this.oResId = oResId;}

    public String getoResName() {return oResName;}

    public void setoResName(String oResName) {this.oResName = oResName;}

    public String getoResImage() {
        return oResImage;
    }

    public void setoResImage(String oResImage) {
        this.oResImage = oResImage;
    }

    public String getoItems() {
        return oItems;
    }

    public void setoItems(String oItems) {
        this.oItems = oItems;
    }

    public String getoPayType() {
        return oPayType;
    }

    public void setoPayType(String oPayType) {
        this.oPayType = oPayType;
    }

    public String getoTotal() {
        return oTotal;
    }

    public void setoTotal(String oTotal) {
        this.oTotal = oTotal;
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }

    public String getoStatus() {
        return oStatus;
    }
}
