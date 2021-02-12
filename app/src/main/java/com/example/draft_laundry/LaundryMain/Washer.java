package com.example.draft_laundry.LaundryMain;

import android.widget.Button;

public class Washer {
    public Button bt;
    public Boolean isError;
    public Boolean isTimer;

    public Washer(Button bt, Boolean isError, Boolean isTimer) {
        this.bt = bt;
        this.isError = isError;
        this.isTimer = isTimer;
    }
}
